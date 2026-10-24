package com.pradnya.smartparking.repository;

import com.pradnya.smartparking.enums.VehicleType;
import com.pradnya.smartparking.model.Vehicle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class VehicleRepositoryTest {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Test
    void shouldSaveAndFindVehicleById() {
        Vehicle saved = vehicleRepository.saveAndFlush(
                new Vehicle("KA01TEST901", VehicleType.CAR));

        assertNotNull(saved.getVehicleId());

        Optional<Vehicle> found = vehicleRepository.findById(saved.getVehicleId());

        assertTrue(found.isPresent());
        assertEquals("KA01TEST901", found.get().getRegistrationNumber());
    }

    @Test
    void shouldCheckRegistrationNumberExists() {
        vehicleRepository.saveAndFlush(
                new Vehicle("KA01TEST902", VehicleType.MOTORCYCLE));

        assertTrue(vehicleRepository.existsByRegistrationNumber("KA01TEST902"));
        assertFalse(vehicleRepository.existsByRegistrationNumber("NOT-REGISTERED"));
    }

    @Test
    void shouldFindVehicleByRegistrationNumber() {
        vehicleRepository.saveAndFlush(
                new Vehicle("KA01TEST903", VehicleType.BUS));

        Optional<Vehicle> found =
                vehicleRepository.findByRegistrationNumber("KA01TEST903");

        assertTrue(found.isPresent());
        assertEquals(VehicleType.BUS, found.get().getVehicleType());
    }
}
