
package com.pradnya.smartparking.dto;

import com.pradnya.smartparking.enums.SpotStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateSpotStatusRequest(
        @NotNull(message = "Spot status is required")
        SpotStatus status
) {}
