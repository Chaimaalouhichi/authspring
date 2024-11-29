package tn.staffonlyproject.staffonlyprojectbackend.email;

import lombok.Getter;

@Getter
public enum EmailTemplateName {

    ACTIVATE_ACCOUNT("activate_account"),
    ADD_ENCADRANT("add_encadrant"),
    RESET_PASSWORD("reset_password")
    ;


    private final String name;
    EmailTemplateName(String name) {
        this.name = name;
    }
}
