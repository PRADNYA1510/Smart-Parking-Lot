package com.pradnya.smartparking.dto;

import com.pradnya.smartparking.enums.SpotSize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateParkingSpotRequest(
        @NotBlank(message = "Spot number is required")
        @Size(max = 20, message = "Spot number cannot exceed 20 characters")
        String spotNumber,

        @NotNull(message = "Spot size is required")
        SpotSize spotSize
) {
}