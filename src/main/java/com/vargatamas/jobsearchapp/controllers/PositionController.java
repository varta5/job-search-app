package com.vargatamas.jobsearchapp.controllers;

import com.vargatamas.jobsearchapp.dtos.GetPositionResponseDTO;
import com.vargatamas.jobsearchapp.dtos.PostPositionRequestDTO;
import com.vargatamas.jobsearchapp.dtos.PostPositionResponseDTO;
import com.vargatamas.jobsearchapp.models.AppClient;
import com.vargatamas.jobsearchapp.services.AppClientService;
import com.vargatamas.jobsearchapp.services.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/position")
public class PositionController {

    private final AppClientService appClientService;
    private final PositionService positionService;

    @Autowired
    public PositionController(AppClientService appClientService, PositionService positionService) {
        this.appClientService = appClientService;
        this.positionService = positionService;
    }

    @PostMapping("")
    public ResponseEntity<PostPositionResponseDTO> createPosition(
            @RequestHeader(name = "Authorization", required = false) String apiKey,
            @RequestBody(required = false) PostPositionRequestDTO requestDTO) {
        AppClient appClient = appClientService.getAppClientById(apiKey);
        return ResponseEntity.ok(positionService.savePosition(requestDTO, appClient));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetPositionResponseDTO> getPosition(
            @RequestHeader(name = "Authorization", required = false) String apiKey,
            @PathVariable("id") Long positionId) {
        appClientService.getAppClientById(apiKey);
        return ResponseEntity.ok(positionService.findPositionById(positionId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<String>> searchPositions(
            @RequestHeader(name = "Authorization", required = false) String apiKey,
            @RequestParam(name = "name", required = false) String positionName,
            @RequestParam(name = "location", required = false) String jobLocation) {
        appClientService.getAppClientById(apiKey);
        return ResponseEntity.ok(positionService.findPositionsByNameAndLocation(positionName, jobLocation));
    }

}
