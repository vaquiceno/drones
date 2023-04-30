package com.drones.services;

import com.drones.mappers.DroneMapper;
import com.drones.mappers.MedicationMapper;
import com.drones.models.Exceptions.DroneGeneralException;
import com.drones.models.database.Drone;
import com.drones.models.database.DroneLoad;
import com.drones.models.database.DroneLoadMedication;
import com.drones.models.database.Medication;
import com.drones.models.requests.DroneLoadMedicationsRequest;
import com.drones.models.requests.DroneRequest;
import com.drones.models.requests.MedicationRequest;
import com.drones.models.responses.DroneResponse;
import com.drones.repositories.DroneLoadRepository;
import com.drones.repositories.DroneRepository;
import com.drones.repositories.MedicationRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.drones.models.database.Drone.Status.IDLE;
import static com.drones.models.database.Drone.Status.LOADING;
import static com.drones.utils.Constants.ERROR_MESSAGE_DRONE_NOT_FOUND;
import static com.drones.utils.Constants.ERROR_MESSAGE_TOTAL_WEIGHT_MEDICATIONS;
import static com.drones.utils.Constants.ERROR_MESSAGE_WEIGHT_LIMIT;
import static com.drones.utils.Constants.ERROR_MESSAGE_DUPLICATED_CODES_MEDICATIONS;
import static com.drones.utils.Constants.DEFAULT_DRONE_MINIMUM_BATTERY;
import static com.drones.utils.Constants.ERROR_MESSAGE_MINIMUM_BATTERY;
import static com.drones.utils.Constants.ERROR_MESSAGE_NOT_IDLE;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DroneService {
    private final DroneRepository droneRepository;
    private final DroneMapper droneMapper;
    private final MedicationRepository medicationRepository;
    private final MedicationMapper medicationMapper;
    private final DroneLoadRepository droneLoadRepository;

    public DroneService(DroneRepository droneRepository,
                        DroneMapper droneMapper,
                        MedicationRepository medicationRepository,
                        MedicationMapper medicationMapper,
                        DroneLoadRepository droneLoadRepository) {
        this.droneRepository = droneRepository;
        this.droneMapper = droneMapper;
        this.medicationRepository = medicationRepository;
        this.medicationMapper = medicationMapper;
        this.droneLoadRepository = droneLoadRepository;
    }

    public List<DroneResponse> findAll() {
        return StreamSupport.stream(droneRepository.findAll().spliterator(), false)
                .map(droneMapper::toResponse).collect(Collectors.toList());
    }

    public List<DroneResponse> findAvailableDrones(){
        return droneRepository.findByStatus(IDLE).stream()
                .map(droneMapper::toResponse).collect(Collectors.toList());
    }

    public DroneResponse registerDrone(DroneRequest droneRequest){
        return droneMapper.toResponse(
                droneRepository.save(droneMapper.toDatabase(droneRequest))
        );
    }

    @Transactional
    public void loadDrone(DroneLoadMedicationsRequest droneLoadMedicationsRequest) throws DroneGeneralException {
        // check for not repeated codes in medications list
        Set<String> codesSet = droneLoadMedicationsRequest.getMedicationRequest().stream()
                .map(MedicationRequest::getCode)
                .collect(Collectors.toSet());
        if (codesSet.size() < droneLoadMedicationsRequest.getMedicationRequest().size())
            throw new DroneGeneralException(ERROR_MESSAGE_DUPLICATED_CODES_MEDICATIONS);
        //look for Drone
        Drone drone = droneRepository.findById(droneLoadMedicationsRequest.getDroneId())
                .orElseThrow(() -> new DroneGeneralException(ERROR_MESSAGE_DRONE_NOT_FOUND));
        //check drone status
        if (drone.getStatus() != IDLE)
            throw new DroneGeneralException(ERROR_MESSAGE_NOT_IDLE);
        //check battery
        if (drone.getCurrentBatteryCapacity() < DEFAULT_DRONE_MINIMUM_BATTERY)
            throw new DroneGeneralException(ERROR_MESSAGE_MINIMUM_BATTERY);
        Integer totalWeight = droneLoadMedicationsRequest.getMedicationRequest().stream()
                .map(medication -> medication.getWeight()*medication.getAmount())
                .reduce(Integer::sum)
                .orElseThrow(() -> new DroneGeneralException(ERROR_MESSAGE_TOTAL_WEIGHT_MEDICATIONS));
        // check for weight limit
        if (totalWeight > drone.getWeightLimit())
            throw new DroneGeneralException(ERROR_MESSAGE_WEIGHT_LIMIT);
        //save all medications
        List<Medication> medications = droneLoadMedicationsRequest.getMedicationRequest().stream().map(
                medicationMapper::toDatabase
        ).collect(Collectors.toList());
        medicationRepository.saveAll(medications);
        // save Drone Load
        DroneLoad droneLoad = new DroneLoad(LocalDateTime.now(), drone);
        droneLoad = droneLoadRepository.save(droneLoad);
        // save Drone Load Medication
        DroneLoad finalDroneLoad = droneLoad;
        droneLoadMedicationsRequest.getMedicationRequest().forEach(
                medication -> {
                    finalDroneLoad.addDroneLoadMedication(new DroneLoadMedication(finalDroneLoad, medicationMapper.toDatabase(medication), medication.getAmount()));
                }
            );
        drone.addDroneLoad(finalDroneLoad);
        // set drone Status to LOADING
        drone.setStatus(LOADING);
        droneRepository.save(drone);
    }
}
