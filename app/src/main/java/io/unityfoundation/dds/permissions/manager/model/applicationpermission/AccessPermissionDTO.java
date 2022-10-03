package io.unityfoundation.dds.permissions.manager.model.applicationpermission;

import io.micronaut.core.annotation.Introspected;

@Introspected
public class AccessPermissionDTO {
    private final Long topicId;
    private final Long applicationId;
    private final AccessType accessType;
    private final Long id;

    public AccessPermissionDTO(Long id, Long topicId, Long applicationId, AccessType accessType) {
        this.id = id;
        this.topicId = topicId;
        this.applicationId = applicationId;
        this.accessType = accessType;
    }

    public Long getTopicId() {
        return topicId;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public AccessType getAccessType() {
        return accessType;
    }

    public Long getId() {
        return id;
    }
}