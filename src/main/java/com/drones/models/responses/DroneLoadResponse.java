package com.drones.models.responses;

import java.time.LocalDateTime;
import java.util.List;

public class DroneLoadResponse {
    private Integer id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private DroneResponse droneResponse;
    private List<DroneLoadMedicationResponse> droneLoadMedicationsResponse;

    public DroneLoadResponse(Integer id,
                             LocalDateTime startTime,
                             LocalDateTime endTime,
                             DroneResponse droneResponse,
                             List<DroneLoadMedicationResponse> droneLoadMedicationsResponse) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.droneResponse = droneResponse;
        this.droneLoadMedicationsResponse = droneLoadMedicationsResponse;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public DroneResponse getDroneResponse() {
        return droneResponse;
    }

    public void setDroneResponse(DroneResponse droneResponse) {
        this.droneResponse = droneResponse;
    }

    public List<DroneLoadMedicationResponse> getDroneLoadMedicationsResponse() {
        return droneLoadMedicationsResponse;
    }

    public void setDroneLoadMedicationsResponse(List<DroneLoadMedicationResponse> droneLoadMedicationsResponse) {
        this.droneLoadMedicationsResponse = droneLoadMedicationsResponse;
    }
}
