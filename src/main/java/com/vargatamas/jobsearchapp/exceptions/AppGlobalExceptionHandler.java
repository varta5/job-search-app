package com.vargatamas.jobsearchapp.exceptions;

import com.vargatamas.jobsearchapp.dtos.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppGlobalExceptionHandler {

    @ExceptionHandler(value = {InvalidInputParameterException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorDTO handleInvalidInputParameterException(InvalidInputParameterException exception) {
        return new ErrorDTO(exception.getMessage());
    }

}
