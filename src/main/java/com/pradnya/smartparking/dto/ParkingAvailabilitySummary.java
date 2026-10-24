package com.pradnya.smartparking.dto;

public record ParkingAvailabilitySummary(
        Long floorId,
        long totalSpots,
        long availableSpots,
        long occupiedSpots,
        long outOfServiceSpots
) {
}