package com.vargatamas.jobsearchapp.services;

import com.vargatamas.jobsearchapp.dtos.PostPositionRequestDTO;
import com.vargatamas.jobsearchapp.dtos.PostPositionResponseDTO;
import com.vargatamas.jobsearchapp.models.AppClient;

public interface PositionService {

    PostPositionResponseDTO savePosition(PostPositionRequestDTO postPositionRequestDTO, AppClient appClient);

}
