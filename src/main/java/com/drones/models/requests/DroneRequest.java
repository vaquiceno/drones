package com.drones.models.requests;

import javax.validation.constraints.NotBlank;
import static com.drones.models.database.Drone.Status;
import static com.drones.models.database.Drone.Model;

public class DroneRequest {
    @NotBlank(message = "serial number required")
    private String serialNumber;
    private Model model;
    private Integer weightLimit;
    private Integer currentBatteryCapacity;
    private Status status;

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Integer getWeightLimit() {
        return weightLimit;
    }

    public void setWeightLimit(Integer weightLimit) {
        this.weightLimit = weightLimit;
    }

    public Integer getCurrentBatteryCapacity() {
        return currentBatteryCapacity;
    }

    public void setCurrentBatteryCapacity(Integer currentBatteryCapacity) {
        this.currentBatteryCapacity = currentBatteryCapacity;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
