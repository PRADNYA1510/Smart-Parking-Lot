
package com.pradnya.smartparking.controller;
import tools.jackson.databind.ObjectMapper;
import com.pradnya.smartparking.dto.CreateFloorRequest;
import com.pradnya.smartparking.model.ParkingFloor;
import com.pradnya.smartparking.service.ParkingFloorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ParkingFloorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetAllFloors() throws Exception {
        mockMvc.perform(get("/api/floors"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldCreateFloor() throws Exception {
        CreateFloorRequest request = new CreateFloorRequest(99);

        mockMvc.perform(post("/api/floors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.floorNumber").value(99));
    }
    @Test
    void shouldRejectFloorNumberZero() throws Exception {
        mockMvc.perform(post("/api/floors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"floorNumber": 0}
                            """))
                .andExpect(status().isBadRequest());
    }
}
