package com.pradnya.smartparking.controller;

import com.pradnya.smartparking.dto.ParkingRevenueReport;
import com.pradnya.smartparking.model.ParkingTicket;
import com.pradnya.smartparking.service.ParkingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ParkingControllerTest {

    private ParkingService parkingService;
    private ParkingController controller;

    @BeforeEach
    void setUp() {
        parkingService = mock(ParkingService.class);
        controller = new ParkingController(parkingService);
    }

    @Test
    void shouldCreateParkingTicket() {
        ParkingTicket ticket = mock(ParkingTicket.class);
        when(parkingService.parkVehicle(10L, 2L)).thenReturn(ticket);

        ResponseEntity<ParkingTicket> response = controller.createParking(10L, 2L);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertSame(ticket, response.getBody());
        verify(parkingService).parkVehicle(10L, 2L);
    }

    @Test
    void shouldGetActiveTickets() {
        List<ParkingTicket> tickets = List.of(mock(ParkingTicket.class));
        when(parkingService.getParkingTicket()).thenReturn(tickets);

        ResponseEntity<List<ParkingTicket>> response = controller.getParkingTicket();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(tickets, response.getBody());
    }

    @Test
    void shouldGetTicketById() {
        ParkingTicket ticket = mock(ParkingTicket.class);
        when(parkingService.getParkingTicketByTicketId(5L)).thenReturn(ticket);

        ResponseEntity<ParkingTicket> response = controller.getParkingTicketByTicketId(5L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(ticket, response.getBody());
    }

    @Test
    void shouldExitVehicle() {
        ParkingTicket ticket = mock(ParkingTicket.class);
        when(parkingService.exitVehicle(5L)).thenReturn(ticket);

        ResponseEntity<ParkingTicket> response = controller.exitVehicle(5L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(ticket, response.getBody());
    }

    @Test
    void shouldGetVehicleParkingHistory() {
        List<ParkingTicket> history = List.of(mock(ParkingTicket.class));
        when(parkingService.getParkingHistory(10L)).thenReturn(history);

        ResponseEntity<List<ParkingTicket>> response = controller.getParkingHistory(10L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(history, response.getBody());
    }

    @Test
    void shouldGetRevenueReport() {
        ParkingRevenueReport report = mock(ParkingRevenueReport.class);
        when(parkingService.getRevenueReport()).thenReturn(report);

        assertSame(report, controller.getRevenueReport());
    }

    @Test
    void shouldSearchHistoryByRegistrationNumber() {
        List<ParkingTicket> history = List.of(mock(ParkingTicket.class));
        when(parkingService.getParkingHistoryByRegistrationNumber("KA01AB1234"))
                .thenReturn(history);

        assertSame(history, controller.getParkingHistoryByRegistrationNumber("KA01AB1234"));
        verify(parkingService).getParkingHistoryByRegistrationNumber("KA01AB1234");
    }
}
