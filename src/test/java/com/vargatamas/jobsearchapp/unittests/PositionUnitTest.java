package com.vargatamas.jobsearchapp.unittests;

import com.vargatamas.jobsearchapp.dtos.GetPositionResponseDTO;
import com.vargatamas.jobsearchapp.dtos.PostPositionRequestDTO;
import com.vargatamas.jobsearchapp.dtos.PostPositionResponseDTO;
import com.vargatamas.jobsearchapp.exceptions.InvalidInputParameterException;
import com.vargatamas.jobsearchapp.models.AppClient;
import com.vargatamas.jobsearchapp.models.Position;
import com.vargatamas.jobsearchapp.repositories.PositionRepository;
import com.vargatamas.jobsearchapp.services.PositionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.modelmapper.ModelMapper;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class PositionUnitTest {

    private PositionRepository positionRepository;
    private PositionServiceImpl positionService;

    @BeforeEach
    public void setUpPositionService() {
        positionRepository = Mockito.mock(PositionRepository.class);
        positionService = new PositionServiceImpl(new ModelMapper(), positionRepository);
    }

    // region unit test for savePosition method

    @Test
    public void savePosition_MissingRequestDto_ThrowsInvalidInputParameterException() {
        InvalidInputParameterException exception = assertThrowsExactly(InvalidInputParameterException.class,
                () -> positionService.savePosition(null, getValidAppClient()));
        assertEquals("Please provide request body in JSON format containing fields 'name' and 'location'",
                exception.getMessage());
    }

    @Test
    public void savePosition_NameIsNull_ThrowsInvalidInputParameterException() {
        PostPositionRequestDTO postPositionRequestDTO = getValidPostPositionRequestDTO();
        ReflectionTestUtils.setField(postPositionRequestDTO, "name", null);
        InvalidInputParameterException exception = assertThrowsExactly(InvalidInputParameterException.class,
                () -> positionService.savePosition(postPositionRequestDTO, getValidAppClient()));
        assertEquals("Field 'name' is missing or empty", exception.getMessage());
    }

    @Test
    public void savePosition_NameIsEmpty_ThrowsInvalidInputParameterException() {
        PostPositionRequestDTO postPositionRequestDTO = getValidPostPositionRequestDTO();
        ReflectionTestUtils.setField(postPositionRequestDTO, "name", "");
        InvalidInputParameterException exception = assertThrowsExactly(InvalidInputParameterException.class,
                () -> positionService.savePosition(postPositionRequestDTO, getValidAppClient()));
        assertEquals("Field 'name' is missing or empty", exception.getMessage());
    }

    @Test
    public void savePosition_NameIsLongerThanFiftyCharacters_ThrowsInvalidInputParameterException() {
        PostPositionRequestDTO postPositionRequestDTO = getValidPostPositionRequestDTO();
        ReflectionTestUtils.setField(postPositionRequestDTO, "name",
                "012345678901234567890123456789012345678901234567890");
        InvalidInputParameterException exception = assertThrowsExactly(InvalidInputParameterException.class,
                () -> positionService.savePosition(postPositionRequestDTO, getValidAppClient()));
        assertEquals("Field 'name' should not exceed 50 characters", exception.getMessage());
    }

    @Test
    public void savePosition_LocationIsNull_ThrowsInvalidInputParameterException() {
        PostPositionRequestDTO postPositionRequestDTO = getValidPostPositionRequestDTO();
        ReflectionTestUtils.setField(postPositionRequestDTO, "jobLocation", null);
        InvalidInputParameterException exception = assertThrowsExactly(InvalidInputParameterException.class,
                () -> positionService.savePosition(postPositionRequestDTO, getValidAppClient()));
        assertEquals("Field 'location' is missing or empty", exception.getMessage());
    }

    @Test
    public void savePosition_LocationIsEmpty_ThrowsInvalidInputParameterException() {
        PostPositionRequestDTO postPositionRequestDTO = getValidPostPositionRequestDTO();
        ReflectionTestUtils.setField(postPositionRequestDTO, "jobLocation", "");
        InvalidInputParameterException exception = assertThrowsExactly(InvalidInputParameterException.class,
                () -> positionService.savePosition(postPositionRequestDTO, getValidAppClient()));
        assertEquals("Field 'location' is missing or empty", exception.getMessage());
    }

    @Test
    public void savePosition_LocationIsLongerThanFiftyCharacters_ThrowsInvalidInputParameterException() {
        PostPositionRequestDTO postPositionRequestDTO = getValidPostPositionRequestDTO();
        ReflectionTestUtils.setField(postPositionRequestDTO, "jobLocation",
                "012345678901234567890123456789012345678901234567890");
        InvalidInputParameterException exception = assertThrowsExactly(InvalidInputParameterException.class,
                () -> positionService.savePosition(postPositionRequestDTO, getValidAppClient()));
        assertEquals("Field 'location' should not exceed 50 characters", exception.getMessage());
    }

    @Test
    public void savePosition_ValidRequest_ThrowsInvalidInputParameterException() {
        PostPositionRequestDTO postPositionRequestDTO = getValidPostPositionRequestDTO();
        when(positionRepository.save(any(Position.class))).thenAnswer((Answer<Position>) invocation -> {
            Object arg = invocation.getArgument(0);
            Position position = (Position)arg;
            ReflectionTestUtils.setField(position, "id", 3L);
            return position;
        });
        PostPositionResponseDTO responseDTO = positionService.savePosition(postPositionRequestDTO, getValidAppClient());
        assertNotNull(responseDTO.getPositionUrl().substring(responseDTO.getPositionUrl().indexOf("/position/") + 10));
    }

    // endregion

    // region unit test for findPositionById method

    @Test
    public void findPositionById_IdIsNull_ThrowsInvalidInputParameterException() {
        InvalidInputParameterException exception = assertThrowsExactly(InvalidInputParameterException.class,
                () -> positionService.findPositionById(null));
        assertEquals("Please provide the ID of the position after '/position/'", exception.getMessage());
    }

    @Test
    public void findPositionById_NoExistingPositionWithId_ThrowsInvalidInputParameterException() {
        Long inputId = 1L;
        when(positionRepository.findById(inputId)).thenReturn(Optional.empty());
        InvalidInputParameterException exception = assertThrowsExactly(InvalidInputParameterException.class,
                () -> positionService.findPositionById(inputId));
        assertEquals("No position exists in our database with the ID provided", exception.getMessage());
    }

    @Test
    public void findPositionById_ValidRequest_ReturnsGetPositionResponseDto() {
        Long inputId = 1L;
        Position position = getValidPosition();
        when(positionRepository.findById(inputId)).thenReturn(Optional.of(position));
        GetPositionResponseDTO responseDTO = positionService.findPositionById(inputId);
        assertTrue(responseDTO.getId().equals(position.getId())
                && responseDTO.getName().equals(position.getName())
                && responseDTO.getJobLocation().equals(position.getJobLocation())
                && responseDTO.getAppClientName().equals(position.getAppClient().getName()));
    }

    // endregion

    private AppClient getValidAppClient() {
        AppClient appClient = new AppClient();
        appClient.setApiKey("f5736401-bcf0-4f04-b896-01777e5d0bb1");
        appClient.setName("Test Client Name");
        appClient.setEmailAddress("test@example.com");
        return appClient;
    }

    private PostPositionRequestDTO getValidPostPositionRequestDTO() {
        PostPositionRequestDTO postPositionRequestDTO = new PostPositionRequestDTO();
        ReflectionTestUtils.setField(postPositionRequestDTO, "name", "Test Position Name");
        ReflectionTestUtils.setField(postPositionRequestDTO, "jobLocation", "Test Position Location");
        return postPositionRequestDTO;
    }

    private Position getValidPosition() {
        Position position = new Position();
        ReflectionTestUtils.setField(position, "name", "Test Position Name");
        ReflectionTestUtils.setField(position, "jobLocation", "Test Position Location");
        ReflectionTestUtils.setField(position, "id", 1L);
        position.setAppClient(getValidAppClient());
        return position;
    }

}
