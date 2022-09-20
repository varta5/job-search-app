package com.vargatamas.jobsearchapp.controllers;

import com.vargatamas.jobsearchapp.dtos.ClientRegistrationRequestDTO;
import com.vargatamas.jobsearchapp.dtos.ClientRegistrationResponseDTO;
import com.vargatamas.jobsearchapp.services.AppClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppClientController {

    private final AppClientService appClientService;

    @Autowired
    public AppClientController(AppClientService appClientService) {
        this.appClientService = appClientService;
    }

    @PostMapping("/client")
    public ResponseEntity<ClientRegistrationResponseDTO> createClient(
            @RequestBody(required = false) ClientRegistrationRequestDTO clientRegistrationRequestDTO) {
        return ResponseEntity.ok(appClientService.saveClient(clientRegistrationRequestDTO));
    }

}
