
package com.pradnya.smartparking.controller;

import com.pradnya.smartparking.dto.ParkingRevenueReport;
import com.pradnya.smartparking.model.ParkingTicket;
import com.pradnya.smartparking.service.ParkingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parking")
public class ParkingController {

    private final ParkingService parkingService;

    public ParkingController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }


    @PostMapping("/park/{vehicleId}")
    public ResponseEntity<ParkingTicket> createParking(
            @PathVariable Long vehicleId,
            @RequestParam(required = false) Long floorId) {

        ParkingTicket ticket =
                parkingService.parkVehicle(vehicleId, floorId);

        return ResponseEntity.status(HttpStatus.CREATED).body(ticket);
    }


    @GetMapping("/active")
    public ResponseEntity<List<ParkingTicket>> getParkingTicket() {
        List<ParkingTicket> parkingTickets =
                parkingService.getParkingTicket();

        return ResponseEntity.ok(parkingTickets);
    }


    @GetMapping("/tickets/{ticketId}")
    public ResponseEntity<ParkingTicket> getParkingTicketByTicketId(
            @PathVariable Long ticketId) {

        ParkingTicket parkingTicket =
                parkingService.getParkingTicketByTicketId(ticketId);

        return ResponseEntity.ok(parkingTicket);
    }


    @PostMapping("/exit/{ticketId}")
    public ResponseEntity<ParkingTicket> exitVehicle(
            @PathVariable Long ticketId) {

        ParkingTicket ticket = parkingService.exitVehicle(ticketId);

        return ResponseEntity.ok(ticket);
    }


    @GetMapping("/vehicles/{vehicleId}/history")
    public ResponseEntity<List<ParkingTicket>> getParkingHistory(
            @PathVariable Long vehicleId) {

        List<ParkingTicket> history =
                parkingService.getParkingHistory(vehicleId);

        return ResponseEntity.ok(history);
    }

    @GetMapping("/revenue")
    public ParkingRevenueReport getRevenueReport() {
        return parkingService.getRevenueReport();
    }
    @GetMapping("/vehicles/search/history")
    public List<ParkingTicket> getParkingHistoryByRegistrationNumber(
            @RequestParam String registrationNumber) {

        return parkingService.getParkingHistoryByRegistrationNumber(
                registrationNumber);
    }

}
