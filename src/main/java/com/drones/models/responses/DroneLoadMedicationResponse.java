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
public class DroneLoadMedicationResponse {
    private String code;
    private String name;
    private Integer weight;
    private String imageUrl;
    private Integer amount;
}
