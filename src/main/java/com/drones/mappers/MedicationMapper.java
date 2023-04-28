package com.drones.mappers;

import com.drones.models.database.Medication;
import com.drones.models.requests.MedicationRequest;
import org.springframework.stereotype.Component;

@Component
public class MedicationMapper {
    /*public DroneResponse toResponse(Drone drone){
        return new DroneResponse(drone.getId(),
                drone.getSerialNumber(),
                drone.getModel().toString(),
                drone.getWeightLimit(),
                drone.getCurrentBatteryCapacity(),
                drone.getStatus().toString());
    }*/

    public Medication toDatabase(MedicationRequest medicationRequest){
        return new Medication(
                medicationRequest.getCode(),
                medicationRequest.getName(),
                medicationRequest.getWeight(),
                medicationRequest.getImageUrl());
    }
}
