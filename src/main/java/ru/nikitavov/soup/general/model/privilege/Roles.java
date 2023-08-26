package ru.nikitavov.soup.general.model.privilege;

public enum Roles {
    UNVERIFIED, VERIFIED, EMPLOYEE_OF_TRAINING_UNIT, CLASS_TEACHER, ADMIN;

    public String roleName() {
        return this.name().toLowerCase();
    }
}
