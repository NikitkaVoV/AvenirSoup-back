package ru.nikitavov.soup.web.security.permission;

public enum Permissions {

    PROFILE_ME(Roles.USER),
    PROFILE_UPDATING_MAIL(Roles.USER),
    PROFILE_UPDATING_PASSWORD(Roles.USER),
    PROFILE_UPDATING_REMOVE_LINKED_NETWORK(Roles.USER),
    PROFILE_UPDATING_MAILING(Roles.VERIFIED),
    PROFILE_UPDATING_LINKED_SCHEDULE(Roles.VERIFIED),
    SCHEDULE_UPDATING_SAVED(Roles.SCHEDULE_MANAGER);

    private final Roles role;

    Permissions(Roles role) {
        this.role = role;
    }
}
