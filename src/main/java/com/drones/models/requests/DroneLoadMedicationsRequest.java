package com.drones.models.requests;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.util.List;

public class DroneLoadMedicationsRequest {
    @NotNull(message = "Drone Id is required")
    @Min(value = 1, message = "Drone Id cannot be less than 1")
    private Integer droneId;
    private List<MedicationRequest> medicationRequest;

    public Integer getDroneId() {
        return droneId;
    }

    public void setDroneId(Integer droneId) {
        this.droneId = droneId;
    }

    public List<MedicationRequest> getMedicationRequest() {
        return medicationRequest;
    }

    public void setMedicationRequest(List<MedicationRequest> medicationRequest) {
        this.medicationRequest = medicationRequest;
    }
}
