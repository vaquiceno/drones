package com.drones.models.responses;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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
