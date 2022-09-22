package com.vargatamas.jobsearchapp.controllers;

import com.vargatamas.jobsearchapp.dtos.PostPositionRequestDTO;
import com.vargatamas.jobsearchapp.dtos.PostPositionResponseDTO;
import com.vargatamas.jobsearchapp.services.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PositionController {

    private final PositionService positionService;

    @Autowired
    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    @PostMapping("/position")
    public ResponseEntity<PostPositionResponseDTO> createPosition(
            @RequestBody(required = false) PostPositionRequestDTO requestDTO) {
        return ResponseEntity.ok(positionService.savePosition(requestDTO));
    }

}
