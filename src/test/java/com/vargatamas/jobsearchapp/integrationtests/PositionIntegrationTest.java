package com.vargatamas.jobsearchapp.integrationtests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vargatamas.jobsearchapp.dtos.PostPositionRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(value = {"/create_clients.sql", "/create_positions.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/delete_positions.sql", "/delete_clients.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class PositionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final MediaType mediaType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype());

    private final ObjectMapper mapper = new ObjectMapper();

    // region integration tests for POST /position endpoint

    @Test
    public void postPosition_WithoutHeaderOrBody_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/position"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(mediaType))
                .andExpect(jsonPath("$.error").value("Field 'apiKey' is missing or empty"));
    }

    @Test
    public void postPosition_ApiKeyIsNotRegistered_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(post("/position")
                        .header("Authorization", "e5736401-bcf0-4f04-b896-01777e5d0bb1")
                        .contentType(mediaType)
                        .content(getValidPostPositionRequestBody()))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(mediaType))
                .andExpect(jsonPath("$.error").value(
                        "'apiKey' is not registered in the system. Please provide already saved identifier"));
    }

    @Test
    public void postPosition_NameIsTooLong_ReturnsBadRequest() throws Exception {
        String requestBody = getValidPostPositionRequestBody();
        requestBody = requestBody.replace("\"Test Position Name\"",
                "\"012345678901234567890123456789012345678901234567890\"");
        mockMvc.perform(post("/position")
                        .header("Authorization", "f5736401-bcf0-4f04-b896-01777e5d0bb1")
                        .contentType(mediaType)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(mediaType))
                .andExpect(jsonPath("$.error").value("Field 'name' should not exceed 50 characters"));
    }

    @Test
    public void postPosition_ValidRequest_ReturnsOkWithUrl() throws Exception {
        mockMvc.perform(post("/position")
                        .header("Authorization", "f5736401-bcf0-4f04-b896-01777e5d0bb1")
                        .contentType(mediaType)
                        .content(getValidPostPositionRequestBody()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(mediaType))
                .andExpect(content().string(containsString("/position/")));
    }

    // endregion

    // region integration tests for GET /position/{id} endpoint

    @Test
    public void getPosition_WithoutAuthenticationHeader_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/position/2"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(mediaType))
                .andExpect(jsonPath("$.error").value("Field 'apiKey' is missing or empty"));
    }

    @Test
    public void getPosition_ApiKeyIsNotRegistered_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/position/2")
                        .header("Authorization", "e5736401-bcf0-4f04-b896-01777e5d0bb1"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(mediaType))
                .andExpect(jsonPath("$.error").value(
                        "'apiKey' is not registered in the system. Please provide already saved identifier"));
    }

    @Test
    public void getPosition_PositionIdIsNotExisting_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/position/1")
                        .header("Authorization", "f5736401-bcf0-4f04-b896-01777e5d0bb1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(mediaType))
                .andExpect(jsonPath("$.error").value("No position exists in our database with the ID provided"));
    }

    @Test
    public void getPosition_ValidRequest_ReturnsOkWithPositionDetails() throws Exception {
        mockMvc.perform(get("/position/2")
                        .header("Authorization", "f5736401-bcf0-4f04-b896-01777e5d0bb1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(mediaType))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.nameOfPosition").value("Existing Test Position name"))
                .andExpect(jsonPath("$.location").value("Location of Existing Test Position"))
                .andExpect(jsonPath("$.nameOfClientPostingTheJob").value("existingTestName"));
    }

    // endregion

    private String getValidPostPositionRequestBody() throws JsonProcessingException {
        PostPositionRequestDTO requestDTO = new PostPositionRequestDTO();
        ReflectionTestUtils.setField(requestDTO, "name", "Test Position Name");
        ReflectionTestUtils.setField(requestDTO, "jobLocation", "Test Position Location");
        return mapper.writeValueAsString(requestDTO);
    }

}
