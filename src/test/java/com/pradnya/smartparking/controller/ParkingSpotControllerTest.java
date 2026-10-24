
package com.pradnya.smartparking.controller;

import tools.jackson.databind.ObjectMapper;
import com.pradnya.smartparking.dto.CreateParkingSpotRequest;
import com.pradnya.smartparking.enums.SpotSize;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.pradnya.smartparking.enums.SpotStatus;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ParkingSpotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateParkingSpot() throws Exception {

        String floorResponse = mockMvc.perform(
                        post("/api/floors")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {"floorNumber": 101}
                                    """))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        long floorId = objectMapper.readTree(floorResponse)
                .get("floorId")
                .asLong();

        CreateParkingSpotRequest request =
                new CreateParkingSpotRequest("A1", SpotSize.CAR);

        mockMvc.perform(post("/api/floors/{floorId}/spots", floorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.spotNumber").value("A1"));
    }

    @Test
    void shouldRejectDuplicateSpotNumberOnSameFloor() throws Exception {

        String floorResponse = mockMvc.perform(
                        post("/api/floors")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {"floorNumber": 102}
                                    """))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        long floorId = objectMapper.readTree(floorResponse)
                .get("floorId")
                .asLong();

        String requestBody = """
            {
                "spotNumber": "A1",
                "spotSize": "CAR"
            }
            """;

        // First request should create the spot.
        mockMvc.perform(post("/api/floors/{floorId}/spots", floorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());

        // Second request with the same spot number should fail.
        mockMvc.perform(post("/api/floors/{floorId}/spots", floorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldGetAvailableSpotsByFloor() throws Exception {

        String floorResponse = mockMvc.perform(
                        post("/api/floors")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {"floorNumber": 103}
                                    """))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        long floorId = objectMapper.readTree(floorResponse)
                .get("floorId")
                .asLong();

        mockMvc.perform(post("/api/floors/{floorId}/spots", floorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "spotNumber": "B1",
                                "spotSize": "CAR"
                            }
                            """))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/floors/{floorId}/spots/available?spotSize=CAR", floorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].spotNumber").value("B1"));
    }

    @Test
    void shouldGetAllSpotsByFloor() throws Exception {

        String floorResponse = mockMvc.perform(
                        post("/api/floors")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {"floorNumber": 104}
                                    """))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        long floorId = objectMapper.readTree(floorResponse)
                .get("floorId")
                .asLong();

        mockMvc.perform(post("/api/floors/{floorId}/spots", floorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "spotNumber": "C1",
                                "spotSize": "CAR"
                            }
                            """))
                .andExpect(status().isCreated());

        mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .get("/api/floors/{floorId}/spots", floorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].spotNumber").value("C1"));
    }


    @Test
    void shouldUpdateSpotStatusToOccupied() throws Exception {
        String floorResponse = mockMvc.perform(
                        post("/api/floors")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                {"floorNumber": 105}
                                """))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        long floorId = objectMapper.readTree(floorResponse)
                .get("floorId").asLong();

        String spotResponse = mockMvc.perform(
                        post("/api/floors/{floorId}/spots", floorId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                {
                                    "spotNumber": "D1",
                                    "spotSize": "CAR"
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        long spotId = objectMapper.readTree(spotResponse)
                .get("spotId").asLong();

        mockMvc.perform(
                        patch("/api/floors/{floorId}/spots/{spotId}/status",
                                floorId, spotId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                {"status": "OCCUPIED"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.spotStatus").value("OCCUPIED"));
    }

    @Test
    void shouldReturnNotFoundForNonexistentSpot() throws Exception {
        String floorResponse = mockMvc.perform(
                        post("/api/floors")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                {"floorNumber": 106}
                                """))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        long floorId = objectMapper.readTree(floorResponse)
                .get("floorId").asLong();

        mockMvc.perform(
                        patch("/api/floors/{floorId}/spots/{spotId}/status",
                                floorId, 999999L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                {"status": "OCCUPIED"}
                                """))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldRejectMissingSpotStatus() throws Exception {
        String floorResponse = mockMvc.perform(
                        post("/api/floors")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                {"floorNumber": 107}
                                """))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        long floorId = objectMapper.readTree(floorResponse)
                .get("floorId").asLong();

        String spotResponse = mockMvc.perform(
                        post("/api/floors/{floorId}/spots", floorId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                {
                                    "spotNumber": "E1",
                                    "spotSize": "CAR"
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        long spotId = objectMapper.readTree(spotResponse)
                .get("spotId").asLong();

        mockMvc.perform(
                        patch("/api/floors/{floorId}/spots/{spotId}/status",
                                floorId, spotId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                {}
                                """))
                .andExpect(status().isBadRequest());
    }

}
