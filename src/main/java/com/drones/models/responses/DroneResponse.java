package com.drones.models.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DroneResponse {
    private Integer droneId;
    private String serialNumber;
    private String model;
    private Integer weightLimit;
    private Integer currentBatteryCapacity;
    private String status;
}
