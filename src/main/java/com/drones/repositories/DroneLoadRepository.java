package com.drones.repositories;


import com.drones.models.database.Drone;
import com.drones.models.database.DroneLoad;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DroneLoadRepository extends CrudRepository<DroneLoad, Integer> {
    List<DroneLoad> findByDroneAndEndTimeNull(Drone drone);
}
