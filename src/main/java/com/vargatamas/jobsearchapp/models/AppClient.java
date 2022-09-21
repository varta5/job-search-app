package com.vargatamas.jobsearchapp.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "clients")
public class AppClient {

    @Id
    private String apiKey;
    private String name;
    private String emailAddress;

    public AppClient() {
    }

    // region SETTERS

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    // endregion

}
