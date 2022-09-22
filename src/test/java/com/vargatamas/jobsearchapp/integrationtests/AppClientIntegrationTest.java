package com.vargatamas.jobsearchapp.integrationtests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vargatamas.jobsearchapp.dtos.ClientRegistrationRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AppClientIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final MediaType mediaType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype());

    private final ObjectMapper mapper = new ObjectMapper();

    // region integration tests for POST /client endpoint

    @Test
    public void postClient_WithoutBody_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/client"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(mediaType))
                .andExpect(jsonPath("$.error").value(
                        "Please provide request body in JSON format containing fields 'name' and 'emailAddress'"));
    }

    @Test
    public void postClient_WithEmailAddressWithoutAtSign_ReturnsBadRequest() throws Exception {
        ClientRegistrationRequestDTO requestDTO = setUpValidRequestBody();
        ReflectionTestUtils.setField(requestDTO, "emailAddress", "test#example.com");
        String requestBody = mapper.writeValueAsString(requestDTO);
        mockMvc.perform(post("/client")
                        .contentType(mediaType)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(mediaType))
                .andExpect(jsonPath("$.error").value(
                        "Please provide valid e-mail address in the 'emailAddress' field"));
    }

    @Test
    @Sql(value = {"/create_clients.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/delete_clients.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void postClient_WithEmailAddressAlreadySaved_ReturnsBadRequest() throws Exception {
        ClientRegistrationRequestDTO requestDTO = setUpValidRequestBody();
        String requestBody = mapper.writeValueAsString(requestDTO);
        mockMvc.perform(post("/client")
                        .contentType(mediaType)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(mediaType))
                .andExpect(jsonPath("$.error").value(
                        "E-mail address '" + requestDTO.getEmailAddress() + "' is already in use"));
    }

    @Test
    public void postClient_ValidRequest_ReturnsOkWithApiKey() throws Exception {
        ClientRegistrationRequestDTO requestDTO = setUpValidRequestBody();
        String requestBody = mapper.writeValueAsString(requestDTO);
        mockMvc.perform(post("/client")
                        .contentType(mediaType)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(mediaType))
                .andExpect(jsonPath("$.apiKey").isString());
    }

    // endregion

    private ClientRegistrationRequestDTO setUpValidRequestBody() {
        ClientRegistrationRequestDTO requestDTO = new ClientRegistrationRequestDTO();
        ReflectionTestUtils.setField(requestDTO, "name", "testName");
        ReflectionTestUtils.setField(requestDTO, "emailAddress", "test@example.com");
        return requestDTO;
    }

}
