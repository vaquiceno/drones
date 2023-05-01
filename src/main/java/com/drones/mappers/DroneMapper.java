package com.drones.mappers;

import com.drones.models.exceptions.DroneGeneralException;
import com.drones.models.database.Drone;
import com.drones.models.database.DroneLoad;
import com.drones.models.database.DroneLoadMedication;
import com.drones.models.requests.DroneRequest;
import com.drones.models.responses.DroneLoadMedicationResponse;
import com.drones.models.responses.DroneLoadResponse;
import com.drones.models.responses.DroneResponse;
import com.drones.models.responses.ErrorResponse;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import static com.drones.models.database.Drone.Model;

@Component
public class DroneMapper {
    public DroneResponse toResponse(Drone drone){
        return new DroneResponse(drone.getId(),
                drone.getSerialNumber(),
                drone.getModel().toString(),
                drone.getWeightLimit(),
                drone.getCurrentBatteryCapacity(),
                drone.getStatus().toString());
    }

    public DroneLoadResponse toResponse(DroneLoad droneLoad){
        return new DroneLoadResponse(
                droneLoad.getId(),
                droneLoad.getStartTime(),
                droneLoad.getEndTime(),
                toResponse(droneLoad.getDrone()),
                droneLoad.getDroneLoadMedications()
                        .stream()
                        .map(this::toResponse)
                        .collect(Collectors.toList())
        );
    }

    public DroneLoadMedicationResponse toResponse(DroneLoadMedication droneLoadMedication){
        return new DroneLoadMedicationResponse(
                droneLoadMedication.getMedication().getCode(),
                droneLoadMedication.getMedication().getName(),
                droneLoadMedication.getMedication().getWeight(),
                droneLoadMedication.getMedication().getImageUrl(),
                droneLoadMedication.getAmount()
        );
    }

    public Drone toDatabase(DroneRequest droneRequest){
        return new Drone(droneRequest.getSerialNumber(),
                Model.valueOf(droneRequest.getModel()),
                droneRequest.getWeightLimit(),
                droneRequest.getCurrentBatteryCapacity());
    }

    public ErrorResponse toErrorResponse(DroneGeneralException e){
        return new ErrorResponse(e.getMessage());
    }
}
