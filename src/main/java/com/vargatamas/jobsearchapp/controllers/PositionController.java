package com.vargatamas.jobsearchapp.controllers;

import com.vargatamas.jobsearchapp.dtos.PostPositionRequestDTO;
import com.vargatamas.jobsearchapp.dtos.PostPositionResponseDTO;
import com.vargatamas.jobsearchapp.models.AppClient;
import com.vargatamas.jobsearchapp.services.AppClientService;
import com.vargatamas.jobsearchapp.services.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PositionController {

    private final AppClientService appClientService;
    private final PositionService positionService;

    @Autowired
    public PositionController(AppClientService appClientService, PositionService positionService) {
        this.appClientService = appClientService;
        this.positionService = positionService;
    }

    @PostMapping("/position")
    public ResponseEntity<PostPositionResponseDTO> createPosition(
            @RequestHeader(name = "Authorization", required = false) String apiKey,
            @RequestBody(required = false) PostPositionRequestDTO requestDTO) {
        AppClient appClient = appClientService.getAppClientById(apiKey);
        return ResponseEntity.ok(positionService.savePosition(requestDTO, appClient));
    }

}
