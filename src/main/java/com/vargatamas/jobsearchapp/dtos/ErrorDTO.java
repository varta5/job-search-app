package com.vargatamas.jobsearchapp.dtos;

public class ErrorDTO {

    private final String error;

    public ErrorDTO(String message) {
        error = message;
    }

    public String getError() {
        return error;
    }

}
