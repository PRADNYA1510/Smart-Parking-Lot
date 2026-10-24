package com.pradnya.smartparking.repository;

import com.pradnya.smartparking.model.ParkingFloor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ParkingFloorRepositoryTest {

    @Autowired
    private ParkingFloorRepository parkingFloorRepository;

    @Test
    void shouldSaveAndRetrieveParkingFloor() {
        ParkingFloor saved = parkingFloorRepository.saveAndFlush(new ParkingFloor(901));

        assertNotNull(saved.getFloorId());
        assertEquals(901, saved.getFloorNumber());

        Optional<ParkingFloor> found =
                parkingFloorRepository.findById(saved.getFloorId());

        assertTrue(found.isPresent());
        assertEquals(901, found.get().getFloorNumber());
    }

    @Test
    void shouldFindAllSavedFloors() {
        parkingFloorRepository.saveAndFlush(new ParkingFloor(902));
        parkingFloorRepository.saveAndFlush(new ParkingFloor(903));

        List<ParkingFloor> floors = parkingFloorRepository.findAll();

        assertTrue(floors.stream().anyMatch(f -> f.getFloorNumber() == 902));
        assertTrue(floors.stream().anyMatch(f -> f.getFloorNumber() == 903));
    }
}
