package com.vargatamas.jobsearchapp.services;

import com.vargatamas.jobsearchapp.dtos.PostPositionRequestDTO;
import com.vargatamas.jobsearchapp.dtos.PostPositionResponseDTO;

public interface PositionService {

    PostPositionResponseDTO savePosition(PostPositionRequestDTO postPositionRequestDTO);

}
