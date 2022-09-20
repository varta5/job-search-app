package com.vargatamas.jobsearchapp.controllers;

import com.vargatamas.jobsearchapp.dtos.ClientRegistrationRequestDTO;
import com.vargatamas.jobsearchapp.dtos.ClientRegistrationResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppClientController {

    @PostMapping("/client")
    public ResponseEntity<ClientRegistrationResponseDTO> createClient(
            @RequestBody(required = false) ClientRegistrationRequestDTO clientRegistrationRequestDTO) {
        return ResponseEntity.ok(new ClientRegistrationResponseDTO("Dummy API key"));
    }

}
