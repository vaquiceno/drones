package com.drones.models.responses;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.AllArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class DroneResponse {
    private Integer droneId;
    private String serialNumber;
    private String model;
    private Integer weightLimit;
    private Integer currentBatteryCapacity;
    private String status;
}
