package io.unityfoundation.dds.permissions.manager.model.groupuser;

import io.micronaut.core.annotation.Introspected;

@Introspected
public class GroupUserDTO {
    private String email;
    private long permissionsGroup;
    private boolean isGroupAdmin = false;
    private boolean isTopicAdmin = false;
    private boolean isApplicationAdmin = false;

    public GroupUserDTO() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getPermissionsGroup() {
        return permissionsGroup;
    }

    public void setPermissionsGroup(long permissionsGroup) {
        this.permissionsGroup = permissionsGroup;
    }

    public boolean isGroupAdmin() {
        return isGroupAdmin;
    }

    public void setGroupAdmin(boolean groupAdmin) {
        isGroupAdmin = groupAdmin;
    }

    public boolean isTopicAdmin() {
        return isTopicAdmin;
    }

    public void setTopicAdmin(boolean topicAdmin) {
        isTopicAdmin = topicAdmin;
    }

    public boolean isApplicationAdmin() {
        return isApplicationAdmin;
    }

    public void setApplicationAdmin(boolean applicationAdmin) {
        isApplicationAdmin = applicationAdmin;
    }
}