package com.vargatamas.jobsearchapp.services;

import com.vargatamas.jobsearchapp.dtos.ClientRegistrationRequestDTO;
import com.vargatamas.jobsearchapp.dtos.PostPositionRequestDTO;
import com.vargatamas.jobsearchapp.dtos.PostPositionResponseDTO;
import com.vargatamas.jobsearchapp.exceptions.InvalidInputParameterException;
import com.vargatamas.jobsearchapp.models.Position;
import com.vargatamas.jobsearchapp.repositories.PositionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PositionServiceImpl implements PositionService {

    private final ModelMapper modelMapper;
    private final PositionRepository positionRepository;

    @Autowired
    public PositionServiceImpl(ModelMapper modelMapper, PositionRepository positionRepository) {
        this.modelMapper = modelMapper;
        this.positionRepository = positionRepository;
    }

    @Override
    public PostPositionResponseDTO savePosition(PostPositionRequestDTO postPositionRequestDTO) {
        throwExceptionIfRequestBodyDtoIsMissing(postPositionRequestDTO);
        throwExceptionIfFieldIsMissing("name", postPositionRequestDTO.getName());
        throwExceptionIfValueIsLongerThanFiftyCharacters("name", postPositionRequestDTO.getName());
        throwExceptionIfFieldIsMissing("location", postPositionRequestDTO.getJobLocation());
        throwExceptionIfValueIsLongerThanFiftyCharacters("location", postPositionRequestDTO.getJobLocation());
        Position position = modelMapper.map(postPositionRequestDTO, Position.class);
        positionRepository.save(position);
        return new PostPositionResponseDTO("/position/" + position.getId());
    }

    private void throwExceptionIfRequestBodyDtoIsMissing(PostPositionRequestDTO postPositionRequestDTO) {
        if (postPositionRequestDTO == null) {
            throw new InvalidInputParameterException(
                    "Please provide request body in JSON format containing fields 'name' and 'location'");
        }
    }

    private void throwExceptionIfFieldIsMissing(String nameOfField, String valueOfField) {
        if (valueOfField == null || valueOfField.isEmpty()) {
            throw new InvalidInputParameterException("Field '" + nameOfField + "' is missing or empty");
        }
    }

    private void throwExceptionIfValueIsLongerThanFiftyCharacters(String nameOfField, String valueOfField) {
        if (valueOfField.length() > 50) {
            throw new InvalidInputParameterException("Field '" + nameOfField + "' should not exceed 50 characters");
        }
    }

}
