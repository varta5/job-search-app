package com.vargatamas.jobsearchapp.dtos;

public class ClientRegistrationRequestDTO {

    private String name;
    private String emailAddress;

    public ClientRegistrationRequestDTO() {
    }

    // region GETTERS

    public String getName() {
        return name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    // endregion

}
