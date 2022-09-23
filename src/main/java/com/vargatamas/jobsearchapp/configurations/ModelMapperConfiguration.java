package com.vargatamas.jobsearchapp.configurations;

import com.vargatamas.jobsearchapp.dtos.GetPositionResponseDTO;
import com.vargatamas.jobsearchapp.models.Position;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        TypeMap<Position, GetPositionResponseDTO> getPositionResponseDtoTypeMap = modelMapper.createTypeMap(Position.class, GetPositionResponseDTO.class);
        getPositionResponseDtoTypeMap.addMappings(mapper -> mapper.map(src -> src.getAppClient().getName(), GetPositionResponseDTO::setAppClientName));
        return modelMapper;
    }

}
