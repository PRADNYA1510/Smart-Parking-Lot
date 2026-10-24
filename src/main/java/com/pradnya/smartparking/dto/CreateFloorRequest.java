package com.pradnya.smartparking.dto;

import jakarta.validation.constraints.Min;

public record CreateFloorRequest(
        @Min(value = 1, message = "Floor number must be at least 1")
        int floorNumber
) {
}