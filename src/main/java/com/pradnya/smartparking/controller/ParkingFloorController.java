package com.pradnya.smartparking.controller;

import com.pradnya.smartparking.dto.CreateFloorRequest;
import com.pradnya.smartparking.model.ParkingFloor;
import com.pradnya.smartparking.service.ParkingFloorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/floors")
public class ParkingFloorController {

    private final ParkingFloorService parkingFloorService;

    public ParkingFloorController(ParkingFloorService parkingFloorService) {
        this.parkingFloorService = parkingFloorService;
    }

    @GetMapping
    public ResponseEntity<List<ParkingFloor>> getAllFloors(){
        List<ParkingFloor> parkingFloors = parkingFloorService.getAllFloors();
        return ResponseEntity.ok(parkingFloors);
    }
    @PostMapping
    public ResponseEntity<ParkingFloor> createFloor(
            @Valid @RequestBody CreateFloorRequest parkingFloor) {

        int parkingFloorNumber = parkingFloor.floorNumber();
        ParkingFloor savedFloor =
                parkingFloorService.createFloor(parkingFloorNumber);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedFloor);
    }




}
