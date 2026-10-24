
package com.pradnya.smartparking.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ParkingEntryRequest(
        @NotNull(message = "Vehicle ID is required")
        @Positive(message = "Vehicle ID must be positive")
        Long vehicleId
) {}
