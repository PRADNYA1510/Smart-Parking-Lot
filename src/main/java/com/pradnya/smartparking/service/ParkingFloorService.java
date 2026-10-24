package com.pradnya.smartparking.service;

import com.pradnya.smartparking.model.ParkingFloor;
import com.pradnya.smartparking.repository.ParkingFloorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkingFloorService {
    private final  ParkingFloorRepository parkingFloorRepository;

    public ParkingFloorService(ParkingFloorRepository parkingFloorRepository){
        this.parkingFloorRepository = parkingFloorRepository;
    }

    public ParkingFloor  createFloor(int floorNumber){
        ParkingFloor parkingFloor= new ParkingFloor(floorNumber);
        if (parkingFloorRepository.existsByFloorNumber(floorNumber)) {
            throw new IllegalArgumentException(
                    "Floor number already exists: " + floorNumber
            );
        }
        return parkingFloorRepository.save(parkingFloor);
    }

    public List<ParkingFloor> getAllFloors(){
        return parkingFloorRepository.findAll();
    }
}
