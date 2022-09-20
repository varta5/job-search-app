package com.vargatamas.jobsearchapp.services;

import com.vargatamas.jobsearchapp.dtos.ClientRegistrationRequestDTO;
import com.vargatamas.jobsearchapp.dtos.ClientRegistrationResponseDTO;
import com.vargatamas.jobsearchapp.models.AppClient;
import com.vargatamas.jobsearchapp.repositories.AppClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class AppClientServiceImpl implements AppClientService {

    private final AppClientRepository appClientRepository;

    @Autowired
    public AppClientServiceImpl(AppClientRepository appClientRepository) {
        this.appClientRepository = appClientRepository;
    }

    @Override
    public ClientRegistrationResponseDTO saveClient(ClientRegistrationRequestDTO clientRegistrationRequestDTO) {
        // TODO global exception handling
        // TODO organize to private methods
        // TODO API key generation
        if (clientRegistrationRequestDTO.getName() == null || clientRegistrationRequestDTO.getName().isEmpty()) {
            return new ClientRegistrationResponseDTO("Field 'name' is missing or empty");
        }
        if (clientRegistrationRequestDTO.getEmailAddress() == null
                || clientRegistrationRequestDTO.getEmailAddress().isEmpty()) {
            return new ClientRegistrationResponseDTO("Field 'emailAddress' is missing or empty");
        }
        if (clientRegistrationRequestDTO.getName().length() > 100) {
            return new ClientRegistrationResponseDTO("Field 'name' should not exceed 100 characters");
        }
        Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
        if (!emailPattern.matcher(clientRegistrationRequestDTO.getEmailAddress()).matches()) {
            return new ClientRegistrationResponseDTO("Please provide valid e-mail address in the 'emailAddress' field");
        }
        AppClient appClient = new AppClient();
        appClient.setName(clientRegistrationRequestDTO.getName());
        appClient.setEmailAddress(clientRegistrationRequestDTO.getEmailAddress());
        String randomKey = "Random key " + String.valueOf((int) (Math.random() * 1000000));
        appClient.setApiKey(randomKey);
        appClientRepository.save(appClient);
        return new ClientRegistrationResponseDTO(randomKey);
    }

}
