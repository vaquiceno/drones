package com.drones.models.requests;

import com.drones.utils.EnumValidator;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import static com.drones.models.database.Drone.Model;
import static com.drones.utils.Constants.DEFAULT_DRONE_BATTERY_CAPACITY;
import static com.drones.utils.Constants.DEFAULT_DRONE_WEIGHT_LIMIT;

@Data
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
}
