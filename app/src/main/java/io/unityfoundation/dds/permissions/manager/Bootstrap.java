package io.unityfoundation.dds.permissions.manager;

import io.micronaut.runtime.event.annotation.EventListener;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import io.unityfoundation.dds.permissions.manager.model.groupuser.GroupUser;
import io.unityfoundation.dds.permissions.manager.model.groupuser.GroupUserRepository;
import io.unityfoundation.dds.permissions.manager.model.group.Group;
import io.unityfoundation.dds.permissions.manager.model.group.GroupRepository;
import io.unityfoundation.dds.permissions.manager.model.user.User;
import io.unityfoundation.dds.permissions.manager.model.user.UserRepository;
import jakarta.inject.Singleton;

@Singleton
public class Bootstrap {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final GroupUserRepository groupUserRepository;

    public Bootstrap(UserRepository userRepository, GroupRepository groupRepository,
            GroupUserRepository groupUserRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.groupUserRepository = groupUserRepository;
    }

    @EventListener
    public void devData(ServerStartupEvent event) {
        User justin = userRepository.save(new User("Justin", "Wilson", "jwilson@test.test", true));
        User kevin = userRepository.save(new User("Kevin", "Stanley", "kstanley@test.test"));
        User max = userRepository.save(new User("Max", "Montes", "montesm@test.test"));
        userRepository.save(new User("Jeff", "Brown", "jeff@test.test"));
        userRepository.save(new User("Julian", "Gracia", "jgracia@test.test"));
        userRepository.save(new User("Daniel", "Bellone", "belloned@test.test", true));

        Group alphaGroup = groupRepository.save(new Group("Alpha"));

        GroupUser alphaJustin = new GroupUser(alphaGroup.getId(), justin.getId());
        GroupUser alphaKevin = new GroupUser(alphaGroup.getId(), kevin.getId());
        GroupUser alphaMax = new GroupUser(alphaGroup.getId(), max.getId());

        groupUserRepository.save(alphaJustin);
        groupUserRepository.save(alphaKevin);
        groupUserRepository.save(alphaMax);

        groupRepository.save(new Group("Beta"));
        groupRepository.save(new Group("Gamma"));
        groupRepository.save(new Group("Delta"));
    }
}
