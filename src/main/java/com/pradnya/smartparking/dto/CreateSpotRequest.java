package com.pradnya.smartparking.dto;

import com.pradnya.smartparking.enums.SpotSize;

public record CreateSpotRequest(String spotNumber , SpotSize spotSize) {
}
