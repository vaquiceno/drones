package com.drones.services;

import com.drones.mappers.DroneMapper;
import com.drones.models.audits.DroneBatteryAudit;
import com.drones.models.exceptions.DroneGeneralException;
import com.drones.models.database.Drone;
import com.drones.models.database.DroneLoad;
import com.drones.models.database.DroneLoadMedication;
import com.drones.models.database.Medication;
import com.drones.models.requests.DroneLoadMedicationsRequest;
import com.drones.models.requests.DroneRequest;
import com.drones.models.requests.MedicationRequest;
import com.drones.models.responses.DroneLoadResponse;
import com.drones.models.responses.DroneResponse;
import com.drones.repositories.DroneLoadRepository;
import com.drones.repositories.DroneRepository;
import com.drones.repositories.MedicationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.drones.models.database.Drone.Status.IDLE;
import static com.drones.models.database.Drone.Status.LOADING;
import static com.drones.models.database.Drone.Status.LOADED;
import static com.drones.models.database.Drone.Status.DELIVERING;
import static com.drones.models.database.Drone.Status.DELIVERED;
import static com.drones.models.database.Drone.Status.RETURNING;
import static com.drones.utils.Constants.ERROR_MESSAGE_DRONE_NOT_FOUND;
import static com.drones.utils.Constants.ERROR_MESSAGE_WEIGHT_LIMIT;
import static com.drones.utils.Constants.ERROR_MESSAGE_DUPLICATED_CODES_MEDICATIONS;
import static com.drones.utils.Constants.DEFAULT_DRONE_MINIMUM_BATTERY;
import static com.drones.utils.Constants.ERROR_MESSAGE_MINIMUM_BATTERY;
import static com.drones.utils.Constants.ERROR_MESSAGE_NOT_IDLE;
import static com.drones.utils.Constants.ERROR_MESSAGE_NOT_LOADING;
import static com.drones.utils.Constants.ERROR_MESSAGE_NOT_LOADED;
import static com.drones.utils.Constants.ERROR_MESSAGE_NOT_DELIVERING;
import static com.drones.utils.Constants.ERROR_MESSAGE_NOT_DELIVERED;
import static com.drones.utils.Constants.ERROR_MESSAGE_NOT_RETURNING;
import static com.drones.utils.Constants.ERROR_MESSAGE_ONE_MORE_ACTIVE_LOADS;
import static com.drones.utils.Constants.ERROR_MESSAGE_ZERO_MORE_ONE_ACTIVE_LOADS;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class DroneService {
    private final DroneRepository droneRepository;
    private final DroneMapper droneMapper;
    private final MedicationRepository medicationRepository;
    private final DroneLoadRepository droneLoadRepository;

    public DroneService(DroneRepository droneRepository,
                        DroneMapper droneMapper,
                        MedicationRepository medicationRepository,
                        DroneLoadRepository droneLoadRepository) {
        this.droneRepository = droneRepository;
        this.droneMapper = droneMapper;
        this.medicationRepository = medicationRepository;
        this.droneLoadRepository = droneLoadRepository;
    }

    public List<DroneResponse> findAll() {
        return StreamSupport.stream(droneRepository.findAll().spliterator(), false)
                .map(droneMapper::toDroneResponse).collect(Collectors.toList());
    }

    public List<DroneResponse> findAvailableDrones(){
        return droneRepository.findByStatus(IDLE).stream()
                .map(droneMapper::toDroneResponse).collect(Collectors.toList());
    }

    public DroneResponse findDrone(Integer droneId) throws DroneGeneralException {
        return droneMapper.toDroneResponse(
                droneRepository.findById(droneId)
                        .orElseThrow(() -> new DroneGeneralException(ERROR_MESSAGE_DRONE_NOT_FOUND))
        );
    }

    public DroneResponse registerDrone(DroneRequest droneRequest){
        return droneMapper.toDroneResponse(
                droneRepository.save(droneMapper.toDrone(droneRequest))
        );
    }

    public void auditDrones() {
        List<DroneBatteryAudit> auditDrones = StreamSupport.stream(droneRepository.findAll().spliterator(), false)
                .map(drone -> DroneBatteryAudit
                        .builder()
                        .droneId(drone.getId())
                        .serialNumber(drone.getSerialNumber())
                        .currentBatteryCapacity(drone.getCurrentBatteryCapacity())
                        .build())
                .collect(Collectors.toList());
        log.info("current drones battery level: {}", auditDrones);
    }

    @Transactional
    public DroneLoadResponse loadingDrone(DroneLoadMedicationsRequest droneLoadMedicationsRequest) throws DroneGeneralException {
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
                .orElse(0);
        // check for weight limit
        if (totalWeight > drone.getWeightLimit())
            throw new DroneGeneralException(ERROR_MESSAGE_WEIGHT_LIMIT);
        //check all DroneLoads are finished for this Drone (endTime not null)
        List<DroneLoad> notFinishedLoads = droneLoadRepository.findByDroneAndEndTimeNull(drone);
        if (notFinishedLoads.size() >= 1)
            throw new DroneGeneralException(ERROR_MESSAGE_ONE_MORE_ACTIVE_LOADS);
        //all validations have passed
        //save all medications
        List<Medication> medications = droneLoadMedicationsRequest.getMedicationRequest().stream().map(
                droneMapper::toMedication
        ).collect(Collectors.toList());
        medicationRepository.saveAll(medications);
        // save Drone Load
        DroneLoad droneLoad = droneMapper.toDroneLoad(drone, LocalDateTime.now());
        droneLoad = droneLoadRepository.save(droneLoad);
        // save Drone Load Medication
        DroneLoad finalDroneLoad = droneLoad;
        droneLoadMedicationsRequest.getMedicationRequest().forEach(
                medication -> {
                    finalDroneLoad.addDroneLoadMedication(
                            DroneLoadMedication
                                    .builder()
                                    .droneLoad(finalDroneLoad)
                                    .medication(droneMapper.toMedication(medication))
                                    .amount(medication.getAmount())
                                    .build()
                    );
                }
            );
        // set drone Status to LOADING
        drone.setStatus(LOADING);
        return droneMapper.toDroneLoadResponse(droneLoad);
    }

    private DroneLoad validateAndGetDroneLoad(Integer droneId) throws DroneGeneralException {
        //look for Drone
        Drone drone = droneRepository.findById(droneId)
                .orElseThrow(() -> new DroneGeneralException(ERROR_MESSAGE_DRONE_NOT_FOUND));
        //check just one DroneLoad is active for this Drone
        List<DroneLoad> activeLoads = droneLoadRepository.findByDroneAndEndTimeNull(drone);
        if (activeLoads.size() != 1)
            throw new DroneGeneralException(ERROR_MESSAGE_ZERO_MORE_ONE_ACTIVE_LOADS);
        return activeLoads.get(0);
    }

    public DroneLoadResponse loadedMedications(Integer droneId) throws DroneGeneralException {
        return droneMapper.toDroneLoadResponse(validateAndGetDroneLoad(droneId));
    }

    @Transactional
    public DroneLoadResponse loadedDrone(Integer droneId) throws DroneGeneralException {
        DroneLoad droneLoad = validateAndGetDroneLoad(droneId);
        Drone drone = droneLoad.getDrone();
        //check drone status
        if (drone.getStatus() != LOADING)
            throw new DroneGeneralException(ERROR_MESSAGE_NOT_LOADING);
        // set drone Status to LOADED
        drone.setStatus(LOADED);
        return droneMapper.toDroneLoadResponse(droneLoad);
    }

    @Transactional
    public DroneLoadResponse deliveringDrone(Integer droneId) throws DroneGeneralException {
        DroneLoad droneLoad = validateAndGetDroneLoad(droneId);
        Drone drone = droneLoad.getDrone();
        //check drone status
        if (drone.getStatus() != LOADED)
            throw new DroneGeneralException(ERROR_MESSAGE_NOT_LOADED);
        //check battery
        if (drone.getCurrentBatteryCapacity() < DEFAULT_DRONE_MINIMUM_BATTERY)
            throw new DroneGeneralException(ERROR_MESSAGE_MINIMUM_BATTERY);
        // set drone Status to DELIVERING
        drone.setStatus(DELIVERING);
        return droneMapper.toDroneLoadResponse(droneLoad);
    }

    @Transactional
    public DroneLoadResponse deliveredDrone(Integer droneId) throws DroneGeneralException {
        DroneLoad droneLoad = validateAndGetDroneLoad(droneId);
        Drone drone = droneLoad.getDrone();
        //check drone status
        if (drone.getStatus() != DELIVERING)
            throw new DroneGeneralException(ERROR_MESSAGE_NOT_DELIVERING);
        // set drone Status to DELIVERED
        drone.setStatus(DELIVERED);
        return droneMapper.toDroneLoadResponse(droneLoad);
    }

    @Transactional
    public DroneLoadResponse returningDrone(Integer droneId) throws DroneGeneralException {
        DroneLoad droneLoad = validateAndGetDroneLoad(droneId);
        Drone drone = droneLoad.getDrone();
        //check drone status
        if (drone.getStatus() != DELIVERED)
            throw new DroneGeneralException(ERROR_MESSAGE_NOT_DELIVERED);
        //check battery
        if (drone.getCurrentBatteryCapacity() < DEFAULT_DRONE_MINIMUM_BATTERY)
            throw new DroneGeneralException(ERROR_MESSAGE_MINIMUM_BATTERY);
        // set drone Status to RETURNING
        drone.setStatus(RETURNING);
        return droneMapper.toDroneLoadResponse(droneLoad);
    }

    @Transactional
    public DroneLoadResponse idleDrone(Integer droneId) throws DroneGeneralException {
        DroneLoad droneLoad = validateAndGetDroneLoad(droneId);
        Drone drone = droneLoad.getDrone();
        //check drone status
        if (drone.getStatus() != RETURNING)
            throw new DroneGeneralException(ERROR_MESSAGE_NOT_RETURNING);
        // set end time for this load
        droneLoad.setEndTime(LocalDateTime.now());
        // set drone Status to IDLE
        drone.setStatus(IDLE);
        return droneMapper.toDroneLoadResponse(droneLoad);
    }
}
