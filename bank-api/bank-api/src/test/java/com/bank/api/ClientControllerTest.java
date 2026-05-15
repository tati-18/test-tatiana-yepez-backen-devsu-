package com.bank.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

import com.bank.api.dto.ClientDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
@Transactional
@Rollback
public class ClientControllerTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateClient() throws Exception {

        long ts = System.currentTimeMillis();

        ClientDTO dto = new ClientDTO(
                "John Doe",
                "Male",
                30,
                "ID-" + ts,
                "Main Street",
                "099999999",
                "client-" + ts,
                "1234",
                true
        );

        mockMvc.perform(post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isCreated());
    }
}