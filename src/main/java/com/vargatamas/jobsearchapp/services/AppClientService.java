package com.vargatamas.jobsearchapp.services;

import com.vargatamas.jobsearchapp.dtos.ClientRegistrationRequestDTO;
import com.vargatamas.jobsearchapp.dtos.ClientRegistrationResponseDTO;
import com.vargatamas.jobsearchapp.models.AppClient;

public interface AppClientService {

    ClientRegistrationResponseDTO saveClient(ClientRegistrationRequestDTO clientRegistrationRequestDTO);

    AppClient getAppClientById(String apiKey);

}
