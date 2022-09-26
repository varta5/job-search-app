package com.vargatamas.jobsearchapp;

import com.vargatamas.jobsearchapp.services.AppClientService;
import com.vargatamas.jobsearchapp.services.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JobSearchAppApplication implements CommandLineRunner {

    private final AppClientService appClientService;
    private final PositionService positionService;

    @Autowired
    public JobSearchAppApplication(AppClientService appClientService, PositionService positionService) {
        this.appClientService = appClientService;
        this.positionService = positionService;
    }

    public static void main(String[] args) {
        SpringApplication.run(JobSearchAppApplication.class, args);
    }

    @Override
    public void run(String... args) {
        appClientService.loadInitialClients();
        positionService.loadInitialPositions(
                appClientService.getAppClientById("49b14be4-e927-4f60-9a83-59b940a2cb2e"),
                appClientService.getAppClientById("c3150bcd-9d10-4f16-80f8-aa0fb21a6f14"));
    }

}
