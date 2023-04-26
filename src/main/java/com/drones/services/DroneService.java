package com.drones.services;

import com.drones.models.database.Drone;
import com.drones.repositories.DroneRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DroneService {
    private final DroneRepository droneRepository;

    public DroneService(DroneRepository droneRepository) {
        this.droneRepository = droneRepository;
    }

    public List<Drone> findAll() {
        return (List<Drone>) droneRepository.findAll();
    }

    public List<Drone> findAvailableDrones(){
        return droneRepository.findByStatus("IDLE");
    }
}
