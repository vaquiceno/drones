package com.drones.controllers;

import com.drones.models.Exceptions.DroneGeneralException;
import com.drones.models.requests.DroneLoadMedicationsRequest;
import com.drones.models.requests.DroneRequest;
import com.drones.models.responses.DroneResponse;
import com.drones.services.DroneService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/drones/")
public class DroneController {
    private final DroneService droneService;

    public DroneController(DroneService droneService) {
        this.droneService = droneService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<DroneResponse>> getAllDrones(){
        return new ResponseEntity<>(droneService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/available")
    public ResponseEntity<List<DroneResponse>> getAvailableDrones(){
        return new ResponseEntity<>(droneService.findAvailableDrones(), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<DroneResponse> registerDrone(@Valid @RequestBody final DroneRequest request){
        return new ResponseEntity<>(droneService.registerDrone(request), HttpStatus.OK);
    }

    @PostMapping("/load")
    public ResponseEntity<DroneResponse> loadDrone(@Valid @RequestBody final DroneLoadMedicationsRequest request)
            throws DroneGeneralException {
        return new ResponseEntity<>(droneService.loadDrone(request), HttpStatus.OK);
    }
}
