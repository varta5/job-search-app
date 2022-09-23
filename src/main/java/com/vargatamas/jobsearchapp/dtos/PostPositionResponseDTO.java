package com.vargatamas.jobsearchapp.dtos;

public class PostPositionResponseDTO {

    private String positionUrl;

    public PostPositionResponseDTO() {
    }

    public PostPositionResponseDTO(String positionUrl) {
        this.positionUrl = positionUrl;
    }

    public String getPositionUrl() {
        return positionUrl;
    }

}
