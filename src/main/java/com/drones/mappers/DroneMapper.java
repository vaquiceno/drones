package com.drones.mappers;

import com.drones.models.database.Medication;
import com.drones.models.exceptions.DroneGeneralException;
import com.drones.models.database.Drone;
import com.drones.models.database.DroneLoad;
import com.drones.models.database.DroneLoadMedication;
import com.drones.models.requests.DroneRequest;
import com.drones.models.requests.MedicationRequest;
import com.drones.models.responses.DroneLoadMedicationResponse;
import com.drones.models.responses.DroneLoadResponse;
import com.drones.models.responses.DroneResponse;
import com.drones.models.responses.ErrorResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import static com.drones.models.database.Drone.Model;

@Component
public class DroneMapper {
    public DroneResponse toDroneResponse(Drone drone){
        return DroneResponse
                .builder()
                .droneId(drone.getId())
                .serialNumber(drone.getSerialNumber())
                .model(drone.getModel().toString())
                .weightLimit(drone.getWeightLimit())
                .currentBatteryCapacity(drone.getCurrentBatteryCapacity())
                .status(drone.getStatus().toString())
                .build();
    }

    public DroneLoadResponse toDroneLoadResponse(DroneLoad droneLoad){
        return DroneLoadResponse
                .builder()
                .loadId(droneLoad.getId())
                .startTime(droneLoad.getStartTime())
                .endTime(droneLoad.getEndTime())
                .drone(toDroneResponse(droneLoad.getDrone()))
                .medications(droneLoad.getDroneLoadMedications()
                        .stream()
                        .map(this::toDroneLoadMedicationResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    public DroneLoadMedicationResponse toDroneLoadMedicationResponse(DroneLoadMedication droneLoadMedication){
        return DroneLoadMedicationResponse.builder()
                .code(droneLoadMedication.getMedication().getCode())
                .name(droneLoadMedication.getMedication().getName())
                .weight(droneLoadMedication.getMedication().getWeight())
                .imageUrl(droneLoadMedication.getMedication().getImageUrl())
                .amount(droneLoadMedication.getAmount())
                .build();
    }

    public Drone toDrone(DroneRequest droneRequest){
        return Drone
                .builder()
                .serialNumber(droneRequest.getSerialNumber())
                .model(Model.valueOf(droneRequest.getModel()))
                .weightLimit(droneRequest.getWeightLimit())
                .currentBatteryCapacity(droneRequest.getCurrentBatteryCapacity())
                .build();
    }

    public DroneLoad toDroneLoad(Drone drone){
        return DroneLoad
                .builder()
                .startTime(LocalDateTime.now())
                .drone(drone)
                .build();
    }

    public Medication toMedication(MedicationRequest medicationRequest){
        return Medication
                .builder()
                .code(medicationRequest.getCode())
                .name(medicationRequest.getName())
                .weight(medicationRequest.getWeight())
                .imageUrl(medicationRequest.getImageUrl())
                .build();
    }

    public ErrorResponse toErrorResponse(DroneGeneralException e){
        return new ErrorResponse(e.getMessage());
    }
}
