package com.hackerrank.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackerrank.api.model.Event;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:data.sql")
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getEventById_Success() throws Exception {
        mockMvc.perform(get("/event/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", notNullValue()))
                .andExpect(jsonPath("$.location", notNullValue()))
                .andExpect(jsonPath("$.cost", notNullValue()))
                .andExpect(jsonPath("$.duration", notNullValue()));
    }

    @Test
    void getEventById_NotFound() throws Exception {
        mockMvc.perform(get("/event/{id}", 999))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("not found")));
    }

    @Test
    void getTop3ByCost_Success() throws Exception {
        mockMvc.perform(get("/event/top3").param("by", "cost"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(lessThanOrEqualTo(3))))
                .andExpect(jsonPath("$[0].cost", notNullValue()));
    }

    @Test
    void getTop3ByDuration_Success() throws Exception {
        mockMvc.perform(get("/event/top3").param("by", "duration"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(lessThanOrEqualTo(3))))
                .andExpect(jsonPath("$[0].duration", notNullValue()));
    }

    @Test
    void getTop3_InvalidParam() throws Exception {
        mockMvc.perform(get("/event/top3").param("by", "invalid"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("must be either 'cost' or 'duration'")));
    }

    @Test
    void getTotalByCost_Success() throws Exception {
        mockMvc.perform(get("/event/total").param("by", "cost"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", isA(Number.class)));
    }

    @Test
    void getTotalByDuration_Success() throws Exception {
        mockMvc.perform(get("/event/total").param("by", "duration"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", isA(Number.class)));
    }

    @Test
    void getTotal_InvalidParam() throws Exception {
        mockMvc.perform(get("/event/total").param("by", "invalid"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("must be either 'cost' or 'duration'")));
    }

    @Test
    void createEvent_Success() throws Exception {
        Event event = Event.builder()
                .name("Test Event")
                .location("Test Location")
                .cost(100)
                .duration(60)
                .build();

        mockMvc.perform(post("/event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Test Event")))
                .andExpect(jsonPath("$.location", is("Test Location")))
                .andExpect(jsonPath("$.cost", is(100)))
                .andExpect(jsonPath("$.duration", is(60)));
    }
}
