
package com.pradnya.smartparking.service;

import com.pradnya.smartparking.model.Vehicle;
import com.pradnya.smartparking.enums.VehicleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FareCalculatorServiceTest {

    private FareCalculatorService fareCalculatorService;

    @BeforeEach
    void setUp() {
        fareCalculatorService = new FareCalculatorService();
    }

    @Test
    void shouldChargeCarForExactlyOneHour() {
        LocalDateTime entry = LocalDateTime.of(2026, 10, 24, 10, 0);
        LocalDateTime exit = entry.plusHours(1);

        long fare = fareCalculatorService.calculateBillableHours(
                entry, exit,
                new Vehicle("KA01AB1234", VehicleType.CAR));

        assertEquals(100L, fare);
    }

    @Test
    void shouldRoundUpPartialHour() {
        LocalDateTime entry = LocalDateTime.of(2026, 10, 24, 10, 0);
        LocalDateTime exit = entry.plusHours(1).plusMinutes(1);

        long fare = fareCalculatorService.calculateBillableHours(
                entry, exit,
                new Vehicle("KA01AB1234", VehicleType.CAR));

        assertEquals(200L, fare);
    }

    @Test
    void shouldChargeMotorcycleForThirtyMinutes() {
        LocalDateTime entry = LocalDateTime.of(2026, 10, 24, 10, 0);
        LocalDateTime exit = entry.plusMinutes(30);

        long fare = fareCalculatorService.calculateBillableHours(
                entry, exit,
                new Vehicle("KA01AB1234", VehicleType.MOTORCYCLE));

        assertEquals(50L, fare);
    }

    @Test
    void shouldChargeBusForTwoHours() {
        LocalDateTime entry = LocalDateTime.of(2026, 10, 24, 10, 0);
        LocalDateTime exit = entry.plusHours(2);

        long fare = fareCalculatorService.calculateBillableHours(
                entry, exit,
                new Vehicle("KA01AB1234", VehicleType.BUS));

        assertEquals(400L, fare);
    }
}
