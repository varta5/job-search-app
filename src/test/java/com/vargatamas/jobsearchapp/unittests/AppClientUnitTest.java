package com.vargatamas.jobsearchapp.unittests;

import com.vargatamas.jobsearchapp.dtos.ClientRegistrationRequestDTO;
import com.vargatamas.jobsearchapp.dtos.ClientRegistrationResponseDTO;
import com.vargatamas.jobsearchapp.exceptions.InvalidInputParameterException;
import com.vargatamas.jobsearchapp.models.AppClient;
import com.vargatamas.jobsearchapp.repositories.AppClientRepository;
import com.vargatamas.jobsearchapp.services.AppClientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class AppClientUnitTest {

    private AppClientRepository appClientRepository;
    private AppClientServiceImpl appClientService;
    private ClientRegistrationRequestDTO clientRegistrationRequestDTO;

    @BeforeEach
    public void setUpAppClientService() {
        appClientRepository = Mockito.mock(AppClientRepository.class);
        appClientService = new AppClientServiceImpl(appClientRepository, new ModelMapper());
        setUpValidClientRegistrationRequestDto();
    }

    // region unit tests for saveClient method

    @Test
    public void saveClient_ClientRegistrationRequestDtoIsNull_ThrowsInvalidInputParameterException() {
        InvalidInputParameterException exception = assertThrowsExactly(InvalidInputParameterException.class,
                () -> appClientService.saveClient(null));
        assertEquals("Please provide request body in JSON format containing fields 'name' and 'emailAddress'",
                exception.getMessage());
    }

    @Test
    public void saveClient_NameIsNull_ThrowsInvalidInputParameterException() {
        ReflectionTestUtils.setField(clientRegistrationRequestDTO, "name", null);
        InvalidInputParameterException exception = assertThrowsExactly(InvalidInputParameterException.class,
                () -> appClientService.saveClient(clientRegistrationRequestDTO));
        assertEquals("Field 'name' is missing or empty", exception.getMessage());
    }

    @Test
    public void saveClient_NameIsEmpty_ThrowsInvalidInputParameterException() {
        ReflectionTestUtils.setField(clientRegistrationRequestDTO, "name", "");
        InvalidInputParameterException exception = assertThrowsExactly(InvalidInputParameterException.class,
                () -> appClientService.saveClient(clientRegistrationRequestDTO));
        assertEquals("Field 'name' is missing or empty", exception.getMessage());
    }

    @Test
    public void saveClient_LengthOfNameIsOverCharacterLimit_ThrowsInvalidInputParameterException() {
        ReflectionTestUtils.setField(clientRegistrationRequestDTO, "name",
                "01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        InvalidInputParameterException exception = assertThrowsExactly(InvalidInputParameterException.class,
                () -> appClientService.saveClient(clientRegistrationRequestDTO));
        assertEquals("Field 'name' should not exceed 100 characters", exception.getMessage());
    }

    @Test
    public void saveClient_EmailAddressIsNull_ThrowsInvalidInputParameterException() {
        ReflectionTestUtils.setField(clientRegistrationRequestDTO, "emailAddress", null);
        InvalidInputParameterException exception = assertThrowsExactly(InvalidInputParameterException.class,
                () -> appClientService.saveClient(clientRegistrationRequestDTO));
        assertEquals("Field 'emailAddress' is missing or empty", exception.getMessage());
    }

    @Test
    public void saveClient_EmailAddressIsEmpty_ThrowsInvalidInputParameterException() {
        ReflectionTestUtils.setField(clientRegistrationRequestDTO, "emailAddress", "");
        InvalidInputParameterException exception = assertThrowsExactly(InvalidInputParameterException.class,
                () -> appClientService.saveClient(clientRegistrationRequestDTO));
        assertEquals("Field 'emailAddress' is missing or empty", exception.getMessage());
    }

    @Test
    public void saveClient_EmailAddressDoesNotContainAtSign_ThrowsInvalidInputParameterException() {
        ReflectionTestUtils.setField(clientRegistrationRequestDTO, "emailAddress", "test#example.com");
        InvalidInputParameterException exception = assertThrowsExactly(InvalidInputParameterException.class,
                () -> appClientService.saveClient(clientRegistrationRequestDTO));
        assertEquals("Please provide valid e-mail address in the 'emailAddress' field", exception.getMessage());
    }

    @Test
    public void saveClient_EmailAddressAlreadySaved_ThrowsInvalidInputParameterException() {
        AppClient existingAppClient = new AppClient();
        ReflectionTestUtils.setField(existingAppClient, "apiKey", "alreadyExistingApiKey");
        ReflectionTestUtils.setField(existingAppClient, "name", "alreadyExistingTestName");
        ReflectionTestUtils.setField(existingAppClient, "emailAddress",
                clientRegistrationRequestDTO.getEmailAddress());
        when(appClientRepository.findByEmailAddress(clientRegistrationRequestDTO.getEmailAddress()))
                .thenReturn(Optional.of(existingAppClient));
        InvalidInputParameterException exception = assertThrowsExactly(InvalidInputParameterException.class,
                () -> appClientService.saveClient(clientRegistrationRequestDTO));
        assertEquals("E-mail address '" + clientRegistrationRequestDTO.getEmailAddress()
                + "' is already in use", exception.getMessage());
    }

    @Test
    public void saveClient_ValidRequest_ReturnsClientRegistrationResponseDto() {
        when(appClientRepository.findByEmailAddress(clientRegistrationRequestDTO.getEmailAddress()))
                .thenReturn(Optional.empty());
        ClientRegistrationResponseDTO responseDTO = appClientService.saveClient(clientRegistrationRequestDTO);
        assertTrue(validateThatStringIsUuid(responseDTO.getApiKey()));
    }

    // endregion

    private void setUpValidClientRegistrationRequestDto() {
        clientRegistrationRequestDTO = new ClientRegistrationRequestDTO();
        ReflectionTestUtils.setField(clientRegistrationRequestDTO, "name", "testName");
        ReflectionTestUtils.setField(clientRegistrationRequestDTO, "emailAddress", "test@example.com");
    }

    private boolean validateThatStringIsUuid(String textToValidate) {
        Pattern uuidPattern = Pattern.compile("^[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}$");
        return uuidPattern.matcher(textToValidate).matches();
    }

}
