package com.drones.repositories;


import com.drones.models.database.DroneLoad;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DroneLoadRepository extends CrudRepository<DroneLoad, Integer> {
}
