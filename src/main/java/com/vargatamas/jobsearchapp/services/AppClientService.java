package com.vargatamas.jobsearchapp.services;

import com.vargatamas.jobsearchapp.dtos.ClientRegistrationRequestDTO;
import com.vargatamas.jobsearchapp.dtos.ClientRegistrationResponseDTO;

public interface AppClientService {

    ClientRegistrationResponseDTO saveClient(ClientRegistrationRequestDTO clientRegistrationRequestDTO);

}
