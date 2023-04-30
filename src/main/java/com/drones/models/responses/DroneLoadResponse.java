package com.drones.models.responses;

import java.time.LocalDateTime;
import java.util.List;

public class DroneLoadResponse {
    private Integer loadId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private DroneResponse drone;
    private List<DroneLoadMedicationResponse> medications;

    public DroneLoadResponse(Integer loadId,
                             LocalDateTime startTime,
                             LocalDateTime endTime,
                             DroneResponse drone,
                             List<DroneLoadMedicationResponse> medications) {
        this.loadId = loadId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.drone = drone;
        this.medications = medications;
    }

    public Integer getLoadId() {
        return loadId;
    }

    public void setLoadId(Integer loadId) {
        this.loadId = loadId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public DroneResponse getDrone() {
        return drone;
    }

    public void setDrone(DroneResponse drone) {
        this.drone = drone;
    }

    public List<DroneLoadMedicationResponse> getMedications() {
        return medications;
    }

    public void setMedications(List<DroneLoadMedicationResponse> medications) {
        this.medications = medications;
    }
}
