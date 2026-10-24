
package com.pradnya.smartparking.dto;

import com.pradnya.smartparking.enums.ParkingTicketStatus;
import com.pradnya.smartparking.model.ParkingTicket;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ParkingTicketResponse(
        Long ticketId,
        Long vehicleId,
        String registrationNumber,
        Long floorId,
        Long spotId,
        String spotNumber,
        LocalDateTime entryTime,
        LocalDateTime exitTime,
        long fair,
        ParkingTicketStatus status
) {
    public static ParkingTicketResponse from(ParkingTicket ticket) {
        return new ParkingTicketResponse(
                ticket.getTicketId(),
                ticket.getVehicle().getVehicleId(),
                ticket.getVehicle().getRegistrationNumber(),
                ticket.getParkingSpot().getParkingFloor().getFloorId(),
                ticket.getParkingSpot().getSpotId(),
                ticket.getParkingSpot().getSpotNumber(),
                ticket.getEntryTime(),
                ticket.getExitTime(),
                ticket.getFare(),
                ticket.getStatus()
        );
    }
}
