package com.pradnya.smartparking.service;

import com.pradnya.smartparking.enums.VehicleType;
import com.pradnya.smartparking.exception.DuplicateRegistrationNumberException;
import com.pradnya.smartparking.model.Vehicle;
import com.pradnya.smartparking.repository.VehicleRepository;
import org.springframework.stereotype.Service;

@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository){
        this.vehicleRepository = vehicleRepository;
    }

    public Vehicle registerVehicle(String registrationNumber, VehicleType vehicleType){
        String normalizedRegistrationNumber =
                registrationNumber.trim().toUpperCase();
        if(vehicleRepository.existsByRegistrationNumber(normalizedRegistrationNumber)){
            throw new DuplicateRegistrationNumberException("Registration number already exists.");
        }
        Vehicle vehicle = new Vehicle(normalizedRegistrationNumber,vehicleType);
        return vehicleRepository.save(vehicle);
    }
}
