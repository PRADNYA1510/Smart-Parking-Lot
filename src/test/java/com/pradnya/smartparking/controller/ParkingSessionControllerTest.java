package com.pradnya.smartparking.controller;

import com.pradnya.smartparking.dto.ParkingEntryRequest;
import com.pradnya.smartparking.dto.ParkingTicketResponse;
import com.pradnya.smartparking.service.ParkingSessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ParkingSessionControllerTest {

    private ParkingSessionService parkingSessionService;
    private ParkingSessionController controller;

    @BeforeEach
    void setUp() {
        parkingSessionService = mock(ParkingSessionService.class);
        controller = new ParkingSessionController(parkingSessionService);
    }

    @Test
    void shouldCreateVehicleEntry() {
        ParkingTicketResponse ticket = mock(ParkingTicketResponse.class);
        when(parkingSessionService.vehicleEntry(12L)).thenReturn(ticket);

        ResponseEntity<ParkingTicketResponse> response =
                controller.vehicleEntry(new ParkingEntryRequest(12L));

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertSame(ticket, response.getBody());
        verify(parkingSessionService).vehicleEntry(12L);
    }

    @Test
    void shouldExitVehicle() {
        ParkingTicketResponse ticket = mock(ParkingTicketResponse.class);
        when(parkingSessionService.vehicleExit(7L)).thenReturn(ticket);

        ResponseEntity<ParkingTicketResponse> response = controller.vehicleExit(7L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(ticket, response.getBody());
    }

    @Test
    void shouldGetVehicleTickets() {
        List<ParkingTicketResponse> tickets = List.of(mock(ParkingTicketResponse.class));
        when(parkingSessionService.getVehicleTickets(12L)).thenReturn(tickets);

        ResponseEntity<List<ParkingTicketResponse>> response =
                controller.getVehicleTickets(12L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(tickets, response.getBody());
    }
}
