package com.pradnya.smartparking.controller;

import com.pradnya.smartparking.dto.RegisterVehicleRequest;
import com.pradnya.smartparking.model.Vehicle;
import com.pradnya.smartparking.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService){
        this.vehicleService = vehicleService;
    }

    @PostMapping
    public Vehicle registerVehicle(
            @Valid @RequestBody RegisterVehicleRequest request) {

        return vehicleService.registerVehicle(
                request.registrationNumber(),
                request.vehicleType()
        );
    }

}
