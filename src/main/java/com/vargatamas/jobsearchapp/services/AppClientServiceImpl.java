package com.vargatamas.jobsearchapp.services;

import com.vargatamas.jobsearchapp.dtos.ClientRegistrationRequestDTO;
import com.vargatamas.jobsearchapp.dtos.ClientRegistrationResponseDTO;
import com.vargatamas.jobsearchapp.exceptions.InvalidInputParameterException;
import com.vargatamas.jobsearchapp.models.AppClient;
import com.vargatamas.jobsearchapp.repositories.AppClientRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class AppClientServiceImpl implements AppClientService {

    private final AppClientRepository appClientRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public AppClientServiceImpl(AppClientRepository appClientRepository, ModelMapper modelMapper) {
        this.appClientRepository = appClientRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ClientRegistrationResponseDTO saveClient(ClientRegistrationRequestDTO clientRegistrationRequestDTO) {
        // TODO avoid duplication of e-mail address
        throwExceptionIfFieldIsMissing("name", clientRegistrationRequestDTO.getName());
        throwExceptionIfFieldIsMissing("emailAddress", clientRegistrationRequestDTO.getEmailAddress());
        throwExceptionIfFieldIsTooLong("name", clientRegistrationRequestDTO.getName(), 100);
        throwExceptionIfNotValidEmailAddress(clientRegistrationRequestDTO.getEmailAddress());
        AppClient appClient = modelMapper.map(clientRegistrationRequestDTO, AppClient.class);
        String apiKeyGeneratedRandomly;
        do {
            apiKeyGeneratedRandomly = UUID.randomUUID().toString();
        } while (appClientRepository.findById(apiKeyGeneratedRandomly).isPresent());
        appClient.setApiKey(apiKeyGeneratedRandomly);
        appClientRepository.save(appClient);
        return new ClientRegistrationResponseDTO(apiKeyGeneratedRandomly);
    }

    private void throwExceptionIfFieldIsMissing(String nameOfField, String valueOfField) {
        if (valueOfField == null || valueOfField.isEmpty()) {
            throw new InvalidInputParameterException("Field '" + nameOfField + "' is missing or empty");
        }
    }

    private void throwExceptionIfFieldIsTooLong(String nameOfField, String valueOfField, int characterLimit) {
        if (valueOfField.length() > characterLimit) {
            throw new InvalidInputParameterException("Field '" + nameOfField + "' should not exceed " + characterLimit + " characters");
        }
    }

    private void throwExceptionIfNotValidEmailAddress(String emailAddress) {
        Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
        if (!emailPattern.matcher(emailAddress).matches()) {
            throw new InvalidInputParameterException("Please provide valid e-mail address in the 'emailAddress' field");
        }
    }

}
