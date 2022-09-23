package com.vargatamas.jobsearchapp.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "clients")
public class AppClient {

    @Id
    private String apiKey;
    private String name;
    private String emailAddress;

    @OneToMany(mappedBy = "appClient")
    private List<Position> positions;

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
