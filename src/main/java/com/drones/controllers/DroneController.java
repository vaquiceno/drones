package com.drones.controllers;

import com.drones.models.database.Drone;
import com.drones.services.DroneService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/drones/")
public class DroneController {
    private final DroneService droneService;

    public DroneController(DroneService droneService) {
        this.droneService = droneService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Drone>> getAllDrones(){
        return new ResponseEntity<>(droneService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/available")
    public ResponseEntity<List<Drone>> getAvailableDrones(){
        return new ResponseEntity<>(droneService.findAvailableDrones(), HttpStatus.OK);
    }
}
