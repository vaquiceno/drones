package com.drones.models.responses;

import java.time.LocalDateTime;
import java.util.List;
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
public class DroneLoadResponse {
    private Integer loadId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private DroneResponse drone;
    private List<DroneLoadMedicationResponse> medications;
}
