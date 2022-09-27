package io.unityfoundation.dds.permissions.manager;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.unityfoundation.dds.permissions.manager.model.application.Application;
import io.unityfoundation.dds.permissions.manager.model.application.ApplicationRepository;
import io.unityfoundation.dds.permissions.manager.model.applicationpermission.AccessType;
import io.unityfoundation.dds.permissions.manager.model.group.Group;
import io.unityfoundation.dds.permissions.manager.model.group.GroupRepository;
import io.unityfoundation.dds.permissions.manager.model.topic.Topic;
import io.unityfoundation.dds.permissions.manager.model.topic.TopicKind;
import io.unityfoundation.dds.permissions.manager.model.topic.TopicRepository;
import io.unityfoundation.dds.permissions.manager.model.user.User;
import io.unityfoundation.dds.permissions.manager.model.user.UserRepository;
import io.unityfoundation.dds.permissions.manager.testing.util.DbCleanup;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class ApplicationPermissionApiSecurityTest {

    private BlockingHttpClient blockingClient;

    @Inject
    MockSecurityService mockSecurityService;

    @Inject
    GroupRepository groupRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    ApplicationRepository applicationRepository;

    @Inject
    TopicRepository topicRepository;

    @Inject
    DbCleanup dbCleanup;

    @Inject
    @Client("/api")
    HttpClient client;
    @Inject
    MockAuthenticationFetcher mockAuthenticationFetcher;

    @BeforeEach
    void setup() {
        blockingClient = client.toBlocking();
    }

    @Nested
    class WhenAsAdmin {

        private Group testGroup;
        private Topic testTopic;
        private Application applicationOne;

        @BeforeEach
        void setup() {
            dbCleanup.cleanup();
            userRepository.save(new User("montesm@test.test"));

            mockSecurityService.postConstruct();
            mockAuthenticationFetcher.setAuthentication(mockSecurityService.getAuthentication().get());

            testGroup = groupRepository.save(new Group("TestGroup"));
            testTopic = topicRepository.save(new Topic("TestTopic", TopicKind.B, testGroup.getId()));
            applicationOne = applicationRepository.save(new Application("ApplicationOne", testGroup.getId()));
        }

        @Test
        public void canUpdatePermissions() {
            Long applicationOneId = applicationOne.getId();
            Long testTopicId = testTopic.getId();

            addReadPermission(applicationOneId, testTopicId);

            HttpRequest<?> request = HttpRequest.GET("/application_permissions?applicationId=" + applicationOneId);
            HashMap<String, Object> responseMap = blockingClient.retrieve(request, HashMap.class);
            assertNotNull(responseMap);
            List<Map> content = (List<Map>) responseMap.get("content");
            assertEquals(1, content.size());
            assertTrue(content.stream().anyMatch((m) -> "READ".equals(m.get("accessType"))));
            assertFalse(content.stream().anyMatch((m) -> "READ_WRITE".equals(m.get("accessType"))));
            assertFalse(content.stream().anyMatch((m) -> "WRITE".equals(m.get("accessType"))));

            addReadWritePermission(applicationOneId, testTopicId);

            request = HttpRequest.GET("/application_permissions?applicationId=" + applicationOneId);
            responseMap = blockingClient.retrieve(request, HashMap.class);
            assertNotNull(responseMap);
            content = (List<Map>) responseMap.get("content");
            assertEquals(2, content.size());
            assertTrue(content.stream().anyMatch((m) -> "READ".equals(m.get("accessType"))));
            assertTrue(content.stream().anyMatch((m) -> "READ_WRITE".equals(m.get("accessType"))));
            assertFalse(content.stream().anyMatch((m) -> "WRITE".equals(m.get("accessType"))));

            request = HttpRequest.DELETE("/application_permissions/" + applicationOneId + "/" + testTopicId + "/" + AccessType.READ.name());
            HttpResponse<Object> response = blockingClient.exchange(request);
            assertEquals(HttpStatus.NO_CONTENT, response.getStatus());

            request = HttpRequest.GET("/application_permissions?applicationId=" + applicationOneId);
            responseMap = blockingClient.retrieve(request, HashMap.class);
            assertNotNull(responseMap);
            content = (List<Map>) responseMap.get("content");
            assertEquals(1, content.size());
            assertFalse(content.stream().anyMatch((m) -> "READ".equals(m.get("accessType"))));
            assertTrue(content.stream().anyMatch((m) -> "READ_WRITE".equals(m.get("accessType"))));
            assertFalse(content.stream().anyMatch((m) -> "WRITE".equals(m.get("accessType"))));
        }

        private void addReadWritePermission(Long applicationOneId, Long testTopicId) {
            addPermission(applicationOneId, testTopicId, AccessType.READ_WRITE);
        }

        private void addReadPermission(Long applicationOneId, Long testTopicId) {
            addPermission(applicationOneId, testTopicId, AccessType.READ);
        }

        private void addPermission(Long applicationOneId, Long testTopicId, AccessType accessType) {
            HttpRequest<?> request;
            HashMap<String, Object> responseMap;

            request = HttpRequest.POST("/application_permissions/" + applicationOneId + "/" + testTopicId + "/" + accessType.name(), Map.of());
            responseMap = blockingClient.retrieve(request, HashMap.class);

            assertNotNull(responseMap);

            assertEquals(accessType.name(), responseMap.get("accessType"));
            assertEquals(applicationOneId.intValue(), responseMap.get("applicationId"));
            assertEquals(testTopicId.intValue(), responseMap.get("topicId"));
        }
    }
}
