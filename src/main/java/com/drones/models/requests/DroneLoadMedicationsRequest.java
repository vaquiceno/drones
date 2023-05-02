package com.drones.models.requests;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.util.List;

@Data
public class DroneLoadMedicationsRequest {
    @NotNull(message = "Drone Id is required")
    @Min(value = 1, message = "Drone Id cannot be less than 1")
    private Integer droneId;

    @NotNull(message = "medications are required")
    @Valid
    private List<MedicationRequest> medicationRequest;
}
