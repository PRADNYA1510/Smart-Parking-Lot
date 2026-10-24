
package com.pradnya.smartparking.controller;

import com.pradnya.smartparking.dto.ParkingEntryRequest;
import com.pradnya.smartparking.dto.ParkingTicketResponse;
import com.pradnya.smartparking.service.ParkingSessionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parking")
public class ParkingSessionController {

    private final ParkingSessionService parkingSessionService;

    public ParkingSessionController(
            ParkingSessionService parkingSessionService) {
        this.parkingSessionService = parkingSessionService;
    }

    @PostMapping("/entry")
    public ResponseEntity<ParkingTicketResponse> vehicleEntry(
            @Valid @RequestBody ParkingEntryRequest request) {

        ParkingTicketResponse ticket =
                parkingSessionService.vehicleEntry(request.vehicleId());

        return ResponseEntity.status(HttpStatus.CREATED).body(ticket);
    }

    @PostMapping("/tickets/{ticketId}/exit")
    public ResponseEntity<ParkingTicketResponse> vehicleExit(
            @PathVariable Long ticketId) {

        ParkingTicketResponse ticket =
                parkingSessionService.vehicleExit(ticketId);

        return ResponseEntity.ok(ticket);
    }

    @GetMapping("/vehicles/{vehicleId}/tickets")
    public ResponseEntity<List<ParkingTicketResponse>> getVehicleTickets(
            @PathVariable Long vehicleId) {

        return ResponseEntity.ok(
                parkingSessionService.getVehicleTickets(vehicleId));
    }
}
