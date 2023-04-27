package com.drones.mappers;

import com.drones.models.database.Drone;
import com.drones.models.requests.DroneRequest;
import com.drones.models.responses.DroneResponse;
import org.springframework.stereotype.Component;

import static com.drones.utils.Constants.DEFAULT_DRONE_BATTERY_CAPACITY;
import static com.drones.models.database.Drone.Status.IDLE;

@Component
public class DroneMapper {
    public DroneResponse toResponse(Drone drone){
        return new DroneResponse(drone.getId(),
                drone.getSerialNumber(),
                drone.getModel().toString(),
                drone.getCurrentBatteryCapacity(),
                drone.getStatus() != null ? drone.getStatus().toString() : null);
    }

    public Drone toDatabase(DroneRequest droneRequest){
        return new Drone(droneRequest.getSerialNumber(),
                droneRequest.getModel(),
                droneRequest.getCurrentBatteryCapacity() != null ? droneRequest.getCurrentBatteryCapacity() : DEFAULT_DRONE_BATTERY_CAPACITY,
                droneRequest.getStatus() != null ? droneRequest.getStatus() : IDLE);
    }
}
