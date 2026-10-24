
package com.pradnya.smartparking.service;

import com.pradnya.smartparking.dto.ParkingTicketResponse;
import com.pradnya.smartparking.enums.*;
import com.pradnya.smartparking.exception.*;
import com.pradnya.smartparking.model.*;
import com.pradnya.smartparking.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ParkingSessionService {

    private final VehicleRepository vehicleRepository;
    private final ParkingSpotRepository parkingSpotRepository;
    private final ParkingTicketRepository parkingTicketRepository;

    public ParkingSessionService(
            VehicleRepository vehicleRepository,
            ParkingSpotRepository parkingSpotRepository,
            ParkingTicketRepository parkingTicketRepository) {
        this.vehicleRepository = vehicleRepository;
        this.parkingSpotRepository = parkingSpotRepository;
        this.parkingTicketRepository = parkingTicketRepository;
    }

    @Transactional
    public ParkingTicketResponse vehicleEntry(Long vehicleId) {

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Vehicle not found with ID: " + vehicleId));

        if (parkingTicketRepository.existsByVehicle_VehicleIdAndStatus(
                vehicleId, ParkingTicketStatus.ACTIVE)) {
            throw new IllegalStateException(
                    "Vehicle already has an active parking ticket");
        }

        SpotSize requiredSize =
                SpotSize.valueOf(vehicle.getVehicleType().name());

        List<ParkingSpot> availableSpots =
                parkingSpotRepository.findAvailableSpotsForUpdate(
                        SpotStatus.AVAILABLE, requiredSize);

        if (availableSpots.isEmpty()) {
            throw new IllegalStateException(
                    "No available parking spot for vehicle type: "
                            + vehicle.getVehicleType());
        }

        ParkingSpot spot = availableSpots.get(0);
        spot.setSpotStatus(SpotStatus.OCCUPIED);
        parkingSpotRepository.save(spot);

        ParkingTicket ticket = new ParkingTicket(vehicle, spot,LocalDateTime.now());
        ParkingTicket savedTicket =
                parkingTicketRepository.save(ticket);

        return ParkingTicketResponse.from(savedTicket);
    }

    @Transactional
    public ParkingTicketResponse vehicleExit(Long ticketId) {

        ParkingTicket ticket =
                parkingTicketRepository.findByTicketIdAndStatus(
                                ticketId, ParkingTicketStatus.ACTIVE)
                        .orElseThrow(() -> new EntityNotFoundException(
                                "Active parking ticket not found with ID: "
                                        + ticketId));

        LocalDateTime exitTime = LocalDateTime.now();

        long minutes = Math.max(
                1,
                Duration.between(ticket.getEntryTime(), exitTime).toMinutes()
        );

        // Initial pricing: ₹20 per started hour.
        long hours = Math.max(1, (minutes + 59) / 60);
        long fare = hours * 20L;

        ticket.complete(exitTime, fare);

        ParkingSpot spot = ticket.getParkingSpot();
        spot.setSpotStatus(SpotStatus.AVAILABLE);
        parkingSpotRepository.save(spot);

        ParkingTicket savedTicket =
                parkingTicketRepository.save(ticket);

        return ParkingTicketResponse.from(savedTicket);
    }

    @Transactional(readOnly = true)
    public List<ParkingTicketResponse> getVehicleTickets(Long vehicleId) {

        if (!vehicleRepository.existsById(vehicleId)) {
            throw new EntityNotFoundException(
                    "Vehicle not found with ID: " + vehicleId);
        }

        return parkingTicketRepository
                .findByVehicle_VehicleIdOrderByEntryTimeDesc(vehicleId)
                .stream()
                .map(ParkingTicketResponse::from)
                .toList();
    }
}
