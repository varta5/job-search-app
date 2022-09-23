package com.vargatamas.jobsearchapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostPositionRequestDTO {

    private String name;

    @JsonProperty("location")
    private String jobLocation;

    public PostPositionRequestDTO() {
    }

    // region GETTERS

    public String getName() {
        return name;
    }

    public String getJobLocation() {
        return jobLocation;
    }

    // endregion

}
