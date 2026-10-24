package com.pradnya.smartparking.controller;

import com.pradnya.smartparking.dto.RegisterVehicleRequest;
import com.pradnya.smartparking.enums.VehicleType;
import com.pradnya.smartparking.model.Vehicle;
import com.pradnya.smartparking.service.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

class VehicleControllerTest {

    private VehicleService vehicleService;
    private VehicleController controller;

    @BeforeEach
    void setUp() {
        vehicleService = mock(VehicleService.class);
        controller = new VehicleController(vehicleService);
    }

    @Test
    void shouldRegisterVehicle() {
        Vehicle vehicle = mock(Vehicle.class);
        when(vehicleService.registerVehicle("KA01AB1234", VehicleType.CAR))
                .thenReturn(vehicle);

        Vehicle result = controller.registerVehicle(
                new RegisterVehicleRequest("KA01AB1234", VehicleType.CAR));

        assertSame(vehicle, result);
        verify(vehicleService).registerVehicle("KA01AB1234", VehicleType.CAR);
    }
}
