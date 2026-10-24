
package com.pradnya.smartparking.service;

import com.pradnya.smartparking.enums.VehicleType;
import com.pradnya.smartparking.exception.DuplicateRegistrationNumberException;
import com.pradnya.smartparking.model.Vehicle;
import com.pradnya.smartparking.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleService vehicleService;

    @Test
    void shouldRegisterVehicleSuccessfully() {
        Vehicle vehicle = new Vehicle("KA01AB1234", VehicleType.CAR);

        when(vehicleRepository.existsByRegistrationNumber("KA01AB1234"))
                .thenReturn(false);

        when(vehicleRepository.save(any(Vehicle.class)))
                .thenReturn(vehicle);

        Vehicle result = vehicleService.registerVehicle(
                "KA01AB1234", VehicleType.CAR);

        assertNotNull(result);
        assertEquals("KA01AB1234", result.getRegistrationNumber());
        assertEquals(VehicleType.CAR, result.getVehicleType());

        verify(vehicleRepository)
                .existsByRegistrationNumber("KA01AB1234");
        verify(vehicleRepository).save(any(Vehicle.class));
    }


    @Test
    void shouldThrowExceptionWhenRegistrationNumberAlreadyExists() {
        when(vehicleRepository.existsByRegistrationNumber("KA01AB1234"))
                .thenReturn(true);

        assertThrows(
                DuplicateRegistrationNumberException.class,
                () -> vehicleService.registerVehicle(
                        "KA01AB1234", VehicleType.CAR)
        );

        verify(vehicleRepository)
                .existsByRegistrationNumber("KA01AB1234");

        verify(vehicleRepository, never())
                .save(any(Vehicle.class));
    }

}
