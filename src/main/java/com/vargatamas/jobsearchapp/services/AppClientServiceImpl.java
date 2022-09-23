package com.vargatamas.jobsearchapp.services;

import com.vargatamas.jobsearchapp.dtos.ClientRegistrationRequestDTO;
import com.vargatamas.jobsearchapp.dtos.ClientRegistrationResponseDTO;
import com.vargatamas.jobsearchapp.exceptions.ClientAuthenticationException;
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
        throwExceptionIfRequestBodyDtoIsMissing(clientRegistrationRequestDTO);
        throwExceptionIfFieldIsMissing("name", clientRegistrationRequestDTO.getName());
        throwExceptionIfFieldIsMissing("emailAddress", clientRegistrationRequestDTO.getEmailAddress());
        throwExceptionIfNameIsTooLong(clientRegistrationRequestDTO.getName());
        throwExceptionIfNotValidEmailAddress(clientRegistrationRequestDTO.getEmailAddress());
        throwExceptionIfEmailAddressAlreadySaved(clientRegistrationRequestDTO.getEmailAddress());
        AppClient appClient = modelMapper.map(clientRegistrationRequestDTO, AppClient.class);
        String apiKeyGeneratedRandomly;
        do {
            apiKeyGeneratedRandomly = UUID.randomUUID().toString();
        } while (appClientRepository.findById(apiKeyGeneratedRandomly).isPresent());
        appClient.setApiKey(apiKeyGeneratedRandomly);
        appClientRepository.save(appClient);
        return new ClientRegistrationResponseDTO(apiKeyGeneratedRandomly);
    }

    @Override
    public AppClient getAppClientById(String apiKey) {
        throwExceptionIfFieldIsMissing("apiKey", apiKey);
        throwExceptionIfApiKeyIsNotUuidFormat(apiKey);
        return appClientRepository.findById(apiKey.toLowerCase()).orElseThrow(() -> new ClientAuthenticationException(
                "'apiKey' is not registered in the system. Please provide already saved identifier"));
    }

    private void throwExceptionIfRequestBodyDtoIsMissing(ClientRegistrationRequestDTO clientRegistrationRequestDTO) {
        if (clientRegistrationRequestDTO == null) {
            throw new InvalidInputParameterException(
                    "Please provide request body in JSON format containing fields 'name' and 'emailAddress'");
        }
    }

    private void throwExceptionIfFieldIsMissing(String nameOfField, String valueOfField) {
        if (valueOfField == null || valueOfField.isEmpty()) {
            throw new InvalidInputParameterException("Field '" + nameOfField + "' is missing or empty");
        }
    }

    private void throwExceptionIfNameIsTooLong(String name) {
        if (name.length() > 100) {
            throw new InvalidInputParameterException("Field 'name' should not exceed 100 characters");
        }
    }

    private void throwExceptionIfNotValidEmailAddress(String emailAddress) {
        Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
        if (!emailPattern.matcher(emailAddress).matches()) {
            throw new InvalidInputParameterException("Please provide valid e-mail address in the 'emailAddress' field");
        }
    }

    private void throwExceptionIfEmailAddressAlreadySaved(String emailAddress) {
        if (appClientRepository.findByEmailAddress(emailAddress).isPresent()) {
            throw new InvalidInputParameterException("E-mail address '" + emailAddress + "' is already in use");
        }
    }

    private void throwExceptionIfApiKeyIsNotUuidFormat(String apiKey) {
        Pattern uuidPattern = Pattern.compile("^[{]?[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}[}]?$");
        if (!uuidPattern.matcher(apiKey).matches()) {
            throw new InvalidInputParameterException(
                    "Provided apiKey does not have the valid format of UUID. Please provide apiKey in valid format: "
                    + "XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX each X representing a hexadecimal digit");
        }
    }

}
