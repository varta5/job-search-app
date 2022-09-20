package com.vargatamas.jobsearchapp.models;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AppClient {

    @Id
    private Long id;

    private String apiKey;
    private String name;
    private String emailAddress;

    public AppClient() {
    }

}
