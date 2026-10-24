package com.pradnya.smartparking.controller;

import com.pradnya.smartparking.dto.CreateParkingSpotRequest;
import com.pradnya.smartparking.dto.ParkingAvailabilitySummary;
import com.pradnya.smartparking.dto.ParkingLotStatistics;
import com.pradnya.smartparking.dto.UpdateSpotStatusRequest;
import com.pradnya.smartparking.enums.SpotSize;
import com.pradnya.smartparking.model.ParkingSpot;
import com.pradnya.smartparking.service.ParkingSpotService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/floors/{floorId}/spots")
public class ParkingSpotController {

    private final ParkingSpotService parkingSpotService;
    public ParkingSpotController(ParkingSpotService parkingSpotService){
        this.parkingSpotService = parkingSpotService;
    }
    @PostMapping
    public ResponseEntity<ParkingSpot> addSpot(
            @PathVariable Long floorId,
            @Valid @RequestBody CreateParkingSpotRequest request) {

        ParkingSpot savedSpot = parkingSpotService.addSpot(
                floorId,
                request.spotNumber(),
                request.spotSize()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(savedSpot);
    }

    @GetMapping("/available")
    public ResponseEntity<List<ParkingSpot>> getAvailableSpots(@PathVariable Long floorId,@RequestParam SpotSize spotSize){
        List<ParkingSpot> availableSpot = parkingSpotService.getAvailableSpots(floorId,spotSize);
        return ResponseEntity.ok(availableSpot);
    }

    @GetMapping
    public ResponseEntity<List<ParkingSpot>> getAllSpotsByFloor(
            @PathVariable Long floorId) {

        List<ParkingSpot> spots =
                parkingSpotService.getAllSpotsByFloor(floorId);

        return ResponseEntity.ok(spots);
    }



    @PatchMapping("/{spotId}/status")
    public ResponseEntity<ParkingSpot> updateSpotStatus(
            @PathVariable Long floorId,
            @PathVariable Long spotId,
            @Valid @RequestBody UpdateSpotStatusRequest request) {

        ParkingSpot updatedSpot = parkingSpotService.updateSpotStatus(
                floorId,
                spotId,
                request.status()
        );

        return ResponseEntity.ok(updatedSpot);
    }

    @GetMapping("/summary")
    public ParkingAvailabilitySummary getAvailabilitySummary(
            @PathVariable Long floorId) {
        return parkingSpotService.getAvailabilitySummary(floorId);
    }

    @GetMapping("/parking/statistics")
    public ParkingLotStatistics getParkingLotStatistics() {
        return parkingSpotService.getParkingLotStatistics();
    }


}
