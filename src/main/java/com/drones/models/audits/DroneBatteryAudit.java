package com.drones.models.audits;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Builder
public class DroneBatteryAudit {
    private Integer droneId;
    private String serialNumber;
    private Integer currentBatteryCapacity;
}
