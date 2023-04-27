package com.drones.services;

import com.drones.mappers.DroneMapper;
import com.drones.models.requests.DroneRequest;
import com.drones.models.responses.DroneResponse;
import com.drones.repositories.DroneRepository;
import org.springframework.stereotype.Service;
import static com.drones.models.database.Drone.Status.IDLE;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DroneService {
    private final DroneRepository droneRepository;
    private final DroneMapper droneMapper;

    public DroneService(DroneRepository droneRepository, DroneMapper droneMapper) {
        this.droneRepository = droneRepository;
        this.droneMapper = droneMapper;
    }

    public List<DroneResponse> findAll() {
        return StreamSupport.stream(droneRepository.findAll().spliterator(), false)
                .map(droneMapper::toResponse).collect(Collectors.toList());
    }

    public List<DroneResponse> findAvailableDrones(){
        return StreamSupport.stream(droneRepository.findByStatus(IDLE).spliterator(), false)
                .map(droneMapper::toResponse).collect(Collectors.toList());
    }

    public DroneResponse registerDrone(DroneRequest droneRequest){
        return droneMapper.toResponse(
                droneRepository.save(droneMapper.toDatabase(droneRequest))
        );
    }
}
