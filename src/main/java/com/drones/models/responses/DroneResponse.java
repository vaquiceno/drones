package com.drones.models.responses;

public class DroneResponse {
    private Integer droneId;
    private String serialNumber;
    private String model;
    private Integer weightLimit;
    private Integer currentBatteryCapacity;
    private String status;

    public DroneResponse(Integer id, String serialNumber, String model, Integer weightLimit, Integer currentBatteryCapacity, String status) {
        this.droneId = id;
        this.serialNumber = serialNumber;
        this.model = model;
        this.weightLimit = weightLimit;
        this.currentBatteryCapacity = currentBatteryCapacity;
        this.status = status;
    }

    public Integer getDroneId() {
        return droneId;
    }

    public void setDroneId(Integer droneId) {
        this.droneId = droneId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
