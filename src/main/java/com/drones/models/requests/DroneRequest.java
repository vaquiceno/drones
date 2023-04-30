package com.drones.models.requests;

import com.drones.utils.EnumValidator;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import static com.drones.models.database.Drone.Status.IDLE;
import static com.drones.models.database.Drone.Status;
import static com.drones.models.database.Drone.Model;
import static com.drones.utils.Constants.DEFAULT_DRONE_BATTERY_CAPACITY;
import static com.drones.utils.Constants.DEFAULT_DRONE_WEIGHT_LIMIT;

public class DroneRequest {
    @NotBlank(message = "Serial number is required")
    @Size(max = 100, message = "serial number can have max 100 characters")
    private String serialNumber;
    @EnumValidator(
        enumClazz = Model.class,
        message = "Invalid Model. valid options: Lightweight, Middleweight, Cruiserweight, Heavyweight"
    )
    private String model;
    @Max(value = 500, message = "Weight limit cannot be more than 500g")
    @Min(value = 100, message = "Weight limit cannot be less than 100g")
    private Integer weightLimit = DEFAULT_DRONE_WEIGHT_LIMIT;
    @Max(value = 100, message = "Battery capacity cannot be more than 100%")
    @Min(value = 0, message = "Battery capacity cannot be less than 0%")
    private Integer currentBatteryCapacity = DEFAULT_DRONE_BATTERY_CAPACITY;

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
}
