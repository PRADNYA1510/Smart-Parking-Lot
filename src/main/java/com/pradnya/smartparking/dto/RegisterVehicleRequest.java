
package com.pradnya.smartparking.dto;

import com.pradnya.smartparking.enums.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterVehicleRequest(

        @NotBlank(message = "Registration number is required")
        @Size(max = 20, message = "Registration number cannot exceed 20 characters")
        String registrationNumber,

        @NotNull(message = "Vehicle type is required")
        VehicleType vehicleType

) {
}
