package com.vargatamas.jobsearchapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetPositionResponseDTO {

    private Long id;

    @JsonProperty(value = "nameOfPosition")
    private String name;

    @JsonProperty(value = "location")
    private String jobLocation;

    @JsonProperty(value = "nameOfClientPostingTheJob")
    private String appClientName;

    public GetPositionResponseDTO() {
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

    public String getAppClientName() {
        return appClientName;
    }

    // endregion

    // region SETTERS

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation;
    }

    public void setAppClientName(String appClientName) {
        this.appClientName = appClientName;
    }

    // endregion

}
