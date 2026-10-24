package com.pradnya.smartparking.dto;

public record ParkingRevenueReport(
        long totalRevenue,
        long completedSessions,
        double averageRevenuePerSession
) {
}