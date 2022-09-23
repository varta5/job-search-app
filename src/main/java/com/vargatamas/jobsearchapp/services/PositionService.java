package com.vargatamas.jobsearchapp.services;

import com.vargatamas.jobsearchapp.dtos.GetPositionResponseDTO;
import com.vargatamas.jobsearchapp.dtos.PostPositionRequestDTO;
import com.vargatamas.jobsearchapp.dtos.PostPositionResponseDTO;
import com.vargatamas.jobsearchapp.models.AppClient;

import java.util.List;

public interface PositionService {

    PostPositionResponseDTO savePosition(PostPositionRequestDTO postPositionRequestDTO, AppClient appClient);

    GetPositionResponseDTO findPositionById(Long id);

    List<String> findPositionsByNameAndLocation(String name, String location);

}
