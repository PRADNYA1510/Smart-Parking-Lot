
package com.pradnya.smartparking.service;

import com.pradnya.smartparking.model.Vehicle;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class FareCalculatorService {

    public long calculateBillableHours(
            LocalDateTime entryTime,
            LocalDateTime exitTime,
            Vehicle vehicle) {

        long totalMinutes = Duration.between(entryTime, exitTime).toMinutes();

        // Round partial hours up to the next hour
        long billableHours = (totalMinutes + 59) / 60;

        // Minimum parking charge is for one hour
        billableHours = Math.max(1, billableHours);

        return billableHours * getVehicleFarePerHour(vehicle);
    }

    public long getVehicleFarePerHour(Vehicle vehicle) {
        return switch (vehicle.getVehicleType()) {
            case MOTORCYCLE -> 50L;
            case CAR -> 100L;
            case BUS -> 200L;
        };
    }
}
