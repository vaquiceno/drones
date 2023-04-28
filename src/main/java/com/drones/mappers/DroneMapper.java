package com.drones.mappers;

import com.drones.models.Exceptions.DroneGeneralException;
import com.drones.models.database.Drone;
import com.drones.models.requests.DroneRequest;
import com.drones.models.responses.DroneResponse;
import com.drones.models.responses.ErrorResponse;
import org.springframework.stereotype.Component;

import static com.drones.models.database.Drone.Model;
import static com.drones.models.database.Drone.Status;

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

    public Drone toDatabase(DroneRequest droneRequest){
        return new Drone(droneRequest.getSerialNumber(),
                Model.valueOf(droneRequest.getModel()),
                droneRequest.getWeightLimit(),
                droneRequest.getCurrentBatteryCapacity(),
                Status.valueOf(droneRequest.getStatus()));
    }

    public ErrorResponse toErrorResponse(DroneGeneralException e){
        return new ErrorResponse(e.getMessage());
    }
}
