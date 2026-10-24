package com.pradnya.smartparking.dto;

public record ParkingLotStatistics(
        long totalSpots,
        long availableSpots,
        long occupiedSpots,
        long outOfServiceSpots,
        double occupancyPercentage
) {
}