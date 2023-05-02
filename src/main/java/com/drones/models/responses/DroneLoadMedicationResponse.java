package com.drones.models.responses;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class DroneLoadMedicationResponse {
    private String code;
    private String name;
    private Integer weight;
    private String imageUrl;
    private Integer amount;
}
