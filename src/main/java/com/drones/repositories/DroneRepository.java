package com.drones.repositories;

import com.drones.models.database.Drone;
import com.drones.models.database.Drone.Status;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DroneRepository extends CrudRepository<Drone, Integer> {
    List<Drone> findByStatus(Status status);
}
