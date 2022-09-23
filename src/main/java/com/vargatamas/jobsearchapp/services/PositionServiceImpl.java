package com.vargatamas.jobsearchapp.services;

import com.vargatamas.jobsearchapp.dtos.GetPositionResponseDTO;
import com.vargatamas.jobsearchapp.dtos.PostPositionRequestDTO;
import com.vargatamas.jobsearchapp.dtos.PostPositionResponseDTO;
import com.vargatamas.jobsearchapp.exceptions.InvalidInputParameterException;
import com.vargatamas.jobsearchapp.models.AppClient;
import com.vargatamas.jobsearchapp.models.Position;
import com.vargatamas.jobsearchapp.repositories.PositionRepository;
import io.github.cdimascio.dotenv.Dotenv;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PositionServiceImpl implements PositionService {

    private final Dotenv dotenv;
    private final ModelMapper modelMapper;
    private final PositionRepository positionRepository;

    @Autowired
    public PositionServiceImpl(ModelMapper modelMapper, PositionRepository positionRepository) {
        dotenv = Dotenv.load();
        this.modelMapper = modelMapper;
        this.positionRepository = positionRepository;
    }

    @Override
    public PostPositionResponseDTO savePosition(PostPositionRequestDTO postPositionRequestDTO, AppClient appClient) {
        throwExceptionIfRequestBodyDtoIsMissing(postPositionRequestDTO);
        throwExceptionIfFieldIsMissing("name", postPositionRequestDTO.getName());
        throwExceptionIfValueIsLongerThanFiftyCharacters("name", postPositionRequestDTO.getName());
        throwExceptionIfFieldIsMissing("location", postPositionRequestDTO.getJobLocation());
        throwExceptionIfValueIsLongerThanFiftyCharacters("location", postPositionRequestDTO.getJobLocation());
        Position position = modelMapper.map(postPositionRequestDTO, Position.class);
        position.setAppClient(appClient);
        positionRepository.save(position);
        return new PostPositionResponseDTO(dotenv.get("API_BASE_URL") + "/position/" + position.getId());
    }

    @Override
    public GetPositionResponseDTO findPositionById(Long id) {
        if (id == null) {
            throw new InvalidInputParameterException("Please provide the ID of the position after '/position/'");
        }
        Position position = positionRepository.findById(id).orElseThrow(
                () -> new InvalidInputParameterException("No position exists in our database with the ID provided"));
        return modelMapper.map(position, GetPositionResponseDTO.class);
    }

    @Override
    public List<String> findPositionsByNameAndLocation(String name, String location) {
        if (name == null) {
            name = "";
        }
        if (location == null) {
            location = "";
        }
        List<Position> positionsFound = positionRepository.findByNameContainsOrJobLocationContains(name, location);
        List<String> urlList = new ArrayList<>();
        for (Position position : positionsFound) {
            urlList.add(dotenv.get("API_BASE_URL") + "/position/" + position.getId());
        }
        return urlList;
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
