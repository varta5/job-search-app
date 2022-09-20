package com.vargatamas.jobsearchapp.dtos;

public class ClientRegistrationResponseDTO {

    private String apiKey;

    public ClientRegistrationResponseDTO() {
    }

    public ClientRegistrationResponseDTO(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiKey() {
        return apiKey;
    }

}
