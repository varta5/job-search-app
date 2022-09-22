package com.vargatamas.jobsearchapp.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "positions")
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String jobLocation;

    public Position() {
    }

    // region GETTERS

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getJobLocation() {
        return jobLocation;
    }

    // endregion

    // region SETTERS

    public void setName(String name) {
        this.name = name;
    }

    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation;
    }

    // endregion

}
