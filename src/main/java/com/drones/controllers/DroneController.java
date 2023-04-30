package com.drones.controllers;

import com.drones.models.Exceptions.DroneGeneralException;
import com.drones.models.requests.DroneLoadMedicationsRequest;
import com.drones.models.requests.DroneRequest;
import com.drones.models.responses.DroneLoadResponse;
import com.drones.models.responses.DroneResponse;
import com.drones.services.DroneService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @PostMapping("/loading")
    public ResponseEntity<DroneLoadResponse> loadingDrone(@Valid @RequestBody final DroneLoadMedicationsRequest request)
            throws DroneGeneralException {
        return new ResponseEntity<>(droneService.loadingDrone(request), HttpStatus.OK);
    }

    @PutMapping("/loaded/{droneId}")
    public ResponseEntity<DroneLoadResponse> loadedDrone(@PathVariable("droneId") final Integer droneId)
            throws DroneGeneralException {
        return new ResponseEntity<>(droneService.loadedDrone(droneId), HttpStatus.OK);
    }

    @PutMapping("/delivering/{droneId}")
    public ResponseEntity<DroneLoadResponse> deliveringDrone(@PathVariable("droneId") final Integer droneId)
            throws DroneGeneralException {
        return new ResponseEntity<>(droneService.deliveringDrone(droneId), HttpStatus.OK);
    }

    @PutMapping("/delivered/{droneId}")
    public ResponseEntity<DroneLoadResponse> deliveredDrone(@PathVariable("droneId") final Integer droneId)
            throws DroneGeneralException {
        return new ResponseEntity<>(droneService.deliveredDrone(droneId), HttpStatus.OK);
    }

    @PutMapping("/returning/{droneId}")
    public ResponseEntity<DroneLoadResponse> returningDrone(@PathVariable("droneId") final Integer droneId)
            throws DroneGeneralException {
        return new ResponseEntity<>(droneService.returningDrone(droneId), HttpStatus.OK);
    }

    @PutMapping("/idle/{droneId}")
    public ResponseEntity<DroneLoadResponse> idleDrone(@PathVariable("droneId") final Integer droneId)
            throws DroneGeneralException {
        return new ResponseEntity<>(droneService.idleDrone(droneId), HttpStatus.OK);
    }
}
