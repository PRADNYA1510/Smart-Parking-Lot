package com.pradnya.smartparking.service;

import com.pradnya.smartparking.dto.ParkingAvailabilitySummary;
import com.pradnya.smartparking.dto.ParkingLotStatistics;
import com.pradnya.smartparking.enums.SpotSize;
import com.pradnya.smartparking.enums.SpotStatus;
import com.pradnya.smartparking.exception.DuplicateParkingSpotException;
import com.pradnya.smartparking.model.ParkingFloor;
import com.pradnya.smartparking.model.ParkingSpot;
import com.pradnya.smartparking.repository.ParkingFloorRepository;
import com.pradnya.smartparking.repository.ParkingSpotRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkingSpotService {
    private final ParkingSpotRepository parkingSpotRepository;
    private final ParkingFloorRepository parkingFloorRepository;

    public ParkingSpotService(ParkingSpotRepository parkingSpotRepository,ParkingFloorRepository parkingFloorRepository){
        this.parkingSpotRepository = parkingSpotRepository;
        this.parkingFloorRepository = parkingFloorRepository;
    }

    public ParkingSpot addSpot(
            Long floorId,
            String spotNumber,
            SpotSize spotSize) {

        ParkingFloor floor = parkingFloorRepository.findById(floorId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Parking floor not found with ID: " + floorId));

        if (parkingSpotRepository
                .existsByParkingFloor_FloorIdAndSpotNumber(floorId, spotNumber)) {
            throw new DuplicateParkingSpotException(
                    "Spot number already exists on this floor");
        }

        ParkingSpot parkingSpot = new ParkingSpot(spotNumber, spotSize);

        floor.addParkingSpot(parkingSpot);

        return parkingSpotRepository.save(parkingSpot);
    }
//    public List<ParkingSpot> getAvailableSpots(SpotSize spotSize){
//        return parkingSpotRepository.findAll().stream().filter(spot -> spot.getSpotStatus() == SpotStatus.AVAILABLE).toList();
//    }

    public List<ParkingSpot> getAvailableSpots(SpotSize spotSize){
        return parkingSpotRepository.findBySpotStatusAndSpotSize(SpotStatus.AVAILABLE,spotSize);
    }

    public List<ParkingSpot> getAvailableSpots(Long floorId, SpotSize spotSize) {
        return parkingSpotRepository
                .findByParkingFloor_FloorIdAndSpotStatusAndSpotSize(
                        floorId, SpotStatus.AVAILABLE, spotSize);
    }

    public List<ParkingSpot> getAllSpotsByFloor(Long floorId) {
        parkingFloorRepository.findById(floorId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Parking floor not found with ID: " + floorId));

        return parkingSpotRepository.findByParkingFloor_FloorId(floorId);
    }



    public ParkingSpot updateSpotStatus(
            Long floorId,
            Long spotId,
            SpotStatus status) {

        ParkingSpot spot = parkingSpotRepository.findById(spotId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Parking spot not found with ID: " + spotId));

        if (spot.getParkingFloor() == null ||
                !spot.getParkingFloor().getFloorId().equals(floorId)) {
            throw new EntityNotFoundException(
                    "Parking spot not found on floor ID: " + floorId);
        }

        spot.setSpotStatus(status);

        return parkingSpotRepository.save(spot);
    }

    public ParkingAvailabilitySummary getAvailabilitySummary(Long floorId) {

        parkingFloorRepository.findById(floorId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Parking floor not found with ID: " + floorId));

        long available = parkingSpotRepository.countByFloorIdAndStatus(
                floorId, SpotStatus.AVAILABLE);

        long occupied = parkingSpotRepository.countByFloorIdAndStatus(
                floorId, SpotStatus.OCCUPIED);

        long outOfService = parkingSpotRepository.countByFloorIdAndStatus(
                floorId, SpotStatus.OUT_OF_SERVICE);

        long total = available + occupied + outOfService;

        return new ParkingAvailabilitySummary(
                floorId,
                total,
                available,
                occupied,
                outOfService
        );
    }

    public ParkingLotStatistics getParkingLotStatistics() {

        long available = parkingSpotRepository.countBySpotStatus(
                SpotStatus.AVAILABLE);

        long occupied = parkingSpotRepository.countBySpotStatus(
                SpotStatus.OCCUPIED);

        long outOfService = parkingSpotRepository.countBySpotStatus(
                SpotStatus.OUT_OF_SERVICE);

        long total = available + occupied + outOfService;

        double occupancyPercentage = total == 0
                ? 0.0
                : (occupied * 100.0) / total;

        return new ParkingLotStatistics(
                total,
                available,
                occupied,
                outOfService,
                Math.round(occupancyPercentage * 100.0) / 100.0
        );
    }



}
