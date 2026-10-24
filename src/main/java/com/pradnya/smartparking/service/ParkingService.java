package com.pradnya.smartparking.service;

import com.pradnya.smartparking.dto.ParkingRevenueReport;
import com.pradnya.smartparking.enums.ParkingTicketStatus;
import com.pradnya.smartparking.enums.SpotSize;
import com.pradnya.smartparking.enums.SpotStatus;
import com.pradnya.smartparking.model.ParkingSpot;
import com.pradnya.smartparking.model.ParkingTicket;
import com.pradnya.smartparking.model.Vehicle;
import com.pradnya.smartparking.repository.ParkingSpotRepository;
import com.pradnya.smartparking.repository.ParkingTicketRepository;
import com.pradnya.smartparking.repository.VehicleRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class ParkingService {
    private final ParkingTicketRepository parkingTicketRepository;
    private final ParkingSpotRepository parkingSpotRepository;
    private final VehicleRepository vehicleRepository;
    private final FareCalculatorService fareCalculatorService;

    public ParkingService(ParkingSpotRepository parkingSpotRepository,ParkingTicketRepository parkingTicketRepository,VehicleRepository vehicleRepository,FareCalculatorService fareCalculatorService){
        this.parkingSpotRepository = parkingSpotRepository;
        this.parkingTicketRepository = parkingTicketRepository;
        this.vehicleRepository = vehicleRepository;
        this.fareCalculatorService = fareCalculatorService;
    }

    @Transactional
    public ParkingTicket parkVehicle(Long vehicleId, Long floorId) {

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Vehicle not found"));

        if (parkingTicketRepository
                .existsByVehicle_VehicleIdAndStatus(vehicleId, ParkingTicketStatus.ACTIVE)) {
            throw new IllegalStateException("Vehicle is already parked");
        }

        SpotSize size = switch (vehicle.getVehicleType()) {
            case MOTORCYCLE -> SpotSize.MOTORCYCLE;
            case CAR -> SpotSize.CAR;
            case BUS -> SpotSize.BUS;
        };


        List<ParkingSpot> availableSpots;

        if (floorId != null) {
            availableSpots = parkingSpotRepository
                    .findAvailableSpotsByFloorForUpdate(
                            floorId, SpotStatus.AVAILABLE, size);
        } else {
            availableSpots = parkingSpotRepository
                    .findAvailableSpotsForUpdate(
                            SpotStatus.AVAILABLE, size);
        }

        if (availableSpots.isEmpty()) {
            throw new EntityNotFoundException(
                    "No available " + size + " parking spot"
                            + (floorId != null ? " on floor " + floorId : "")
            );
        }

        ParkingSpot spot = availableSpots.get(0);
        spot.setSpotStatus(SpotStatus.OCCUPIED);
        parkingSpotRepository.save(spot);

        ParkingTicket ticket =
                new ParkingTicket(vehicle, spot, LocalDateTime.now());

        return parkingTicketRepository.save(ticket);
    }


    public List<ParkingTicket> getParkingTicket() {
        return parkingTicketRepository.findByStatus(
                ParkingTicketStatus.ACTIVE
        );
    }
    public ParkingTicket getParkingTicketByTicketId(Long ticketId) {
        return parkingTicketRepository
                .findByTicketIdAndStatus(ticketId, ParkingTicketStatus.ACTIVE)
                .orElseThrow(() ->
                        new EntityNotFoundException("Active parking ticket not found"));
    }


    @Transactional
    public ParkingTicket exitVehicle(Long ticketId) {

        ParkingTicket parkingTicket = getParkingTicketByTicketId(ticketId);


        LocalDateTime exitTime = LocalDateTime.now();


        long fare = fareCalculatorService.calculateBillableHours(
                parkingTicket.getEntryTime(),
                exitTime,
                parkingTicket.getVehicle()
        );


        parkingTicket.complete(exitTime, fare);


        ParkingSpot spot = parkingTicket.getParkingSpot();
        spot.setSpotStatus(SpotStatus.AVAILABLE);
        parkingSpotRepository.save(spot);


        return parkingTicketRepository.save(parkingTicket);
    }


    public List<ParkingTicket> getParkingHistory(Long vehicleId) {
        vehicleRepository.findById(vehicleId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Vehicle not found"));

        return parkingTicketRepository
                .findByVehicle_VehicleIdOrderByEntryTimeDesc(vehicleId);
    }

    public ParkingRevenueReport getRevenueReport() {

        long totalRevenue = parkingTicketRepository.sumFareByStatus(
                ParkingTicketStatus.COMPLETED);

        long completedSessions = parkingTicketRepository.countByStatus(
                ParkingTicketStatus.COMPLETED);

        double averageRevenue = completedSessions == 0
                ? 0.0
                : (double) totalRevenue / completedSessions;

        return new ParkingRevenueReport(
                totalRevenue,
                completedSessions,
                Math.round(averageRevenue * 100.0) / 100.0
        );
    }

    public List<ParkingTicket> getParkingHistoryByRegistrationNumber(
            String registrationNumber) {

        String normalizedNumber = registrationNumber.trim().toUpperCase();

        Vehicle vehicle = vehicleRepository
                .findByRegistrationNumber(normalizedNumber)
                .orElseThrow(() ->
                        new EntityNotFoundException("Vehicle not found"));

        return parkingTicketRepository
                .findByVehicle_RegistrationNumberOrderByEntryTimeDesc(
                        vehicle.getRegistrationNumber());
    }




}
