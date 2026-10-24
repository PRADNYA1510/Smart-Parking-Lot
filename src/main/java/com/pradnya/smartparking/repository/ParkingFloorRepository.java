package com.pradnya.smartparking.repository;

import com.pradnya.smartparking.model.ParkingFloor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingFloorRepository extends JpaRepository<ParkingFloor,Long> {

boolean existsByFloorNumber(int floorNumber);
}
