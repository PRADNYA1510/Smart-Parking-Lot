package com.pradnya.smartparking.repository;

import com.pradnya.smartparking.enums.SpotSize;
import com.pradnya.smartparking.enums.SpotStatus;
import com.pradnya.smartparking.model.ParkingFloor;
import com.pradnya.smartparking.model.ParkingSpot;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ParkingSpotRepositoryTest {

    @Autowired
    private ParkingFloorRepository parkingFloorRepository;

    @Autowired
    private ParkingSpotRepository parkingSpotRepository;

    @Test
    void shouldSaveAndFindSpotsByFloor() {
        ParkingFloor floor = parkingFloorRepository.saveAndFlush(new ParkingFloor(911));
        ParkingSpot spot = new ParkingSpot("TEST-911", SpotSize.CAR);
        floor.addParkingSpot(spot);
        parkingSpotRepository.saveAndFlush(spot);

        List<ParkingSpot> found =
                parkingSpotRepository.findByParkingFloor_FloorId(floor.getFloorId());

        assertTrue(found.stream().anyMatch(s -> "TEST-911".equals(s.getSpotNumber())));
    }

    @Test
    void shouldFindAvailableSpotsBySize() {
        ParkingFloor floor = parkingFloorRepository.saveAndFlush(new ParkingFloor(912));
        ParkingSpot spot = new ParkingSpot("TEST-912", SpotSize.CAR);
        floor.addParkingSpot(spot);
        parkingSpotRepository.saveAndFlush(spot);

        List<ParkingSpot> found =
                parkingSpotRepository.findBySpotStatusAndSpotSize(
                        SpotStatus.AVAILABLE, SpotSize.CAR);

        assertTrue(found.stream().anyMatch(s -> "TEST-912".equals(s.getSpotNumber())));
    }

    @Test
    void shouldCountSpotsByStatus() {
        ParkingFloor floor = parkingFloorRepository.saveAndFlush(new ParkingFloor(913));
        ParkingSpot spot = new ParkingSpot("TEST-913", SpotSize.CAR);
        floor.addParkingSpot(spot);
        parkingSpotRepository.saveAndFlush(spot);

        assertTrue(parkingSpotRepository.countBySpotStatus(SpotStatus.AVAILABLE) >= 1);
    }
}
