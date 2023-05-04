package com.drones;

import com.drones.models.database.Drone;
import com.drones.models.database.DroneLoad;
import com.drones.models.database.DroneLoadMedication;
import com.drones.models.database.Medication;
import com.drones.models.exceptions.DroneGeneralException;
import com.drones.models.requests.DroneLoadMedicationsRequest;
import com.drones.models.requests.DroneRequest;
import com.drones.models.requests.MedicationRequest;
import com.drones.models.responses.DroneLoadMedicationResponse;
import com.drones.models.responses.DroneLoadResponse;
import com.drones.models.responses.DroneResponse;
import com.drones.models.responses.ErrorResponse;

import java.time.LocalDateTime;
import java.util.List;

import static com.drones.models.database.Drone.Model.Lightweight;

import static com.drones.models.database.Drone.Status;
import static com.drones.models.database.Drone.Status.IDLE;
import static com.drones.utils.Constants.ERROR_MESSAGE_DRONE_NOT_FOUND;
import static com.drones.utils.Constants.ERROR_MESSAGE_WEIGHT_LIMIT;
import static com.drones.utils.Constants.ERROR_MESSAGE_DUPLICATED_CODES_MEDICATIONS;
import static com.drones.utils.Constants.ERROR_MESSAGE_MINIMUM_BATTERY;
import static com.drones.utils.Constants.ERROR_MESSAGE_NOT_IDLE;
import static com.drones.utils.Constants.ERROR_MESSAGE_NOT_LOADING;
import static com.drones.utils.Constants.ERROR_MESSAGE_NOT_LOADED;
import static com.drones.utils.Constants.ERROR_MESSAGE_NOT_DELIVERING;
import static com.drones.utils.Constants.ERROR_MESSAGE_NOT_DELIVERED;
import static com.drones.utils.Constants.ERROR_MESSAGE_NOT_RETURNING;
import static com.drones.utils.Constants.ERROR_MESSAGE_ONE_MORE_ACTIVE_LOADS;
import static com.drones.utils.Constants.ERROR_MESSAGE_ZERO_MORE_ONE_ACTIVE_LOADS;


public class Mocks {

    public final Integer DRONE_ID = 1;
    public final String DRONE_SERIAL_NUMBER = "sn1";
    public final Integer DRONE_WEIGHT_LIMIT = 500;
    public final Integer DRONE_BATTERY_CAPACITY = 100;
    public final Integer DRONE_LOAD_ID = 1;
    public final LocalDateTime DRONE_LOAD_START_TIME = LocalDateTime.of(
            2023,
            3,
            8,
            19,
            27,
            18);
    public final LocalDateTime DRONE_LOAD_END_TIME = LocalDateTime.of(
            2023,
            3,
            8,
            19,
            28,
            18);

    public final String MEDICATION_CODE = "XYZ";
    public final String MEDICATION_NAME = "Meat";
    public final Integer MEDICATION_WEIGHT = 10;
    public final String MEDICATION_IMAGE = "/image/image.png";
    public final Integer MEDICATION_AMOUNT = 2;

    public final Integer DRONE_BATTERY_CAPACITY_LOW = 20;

    public Drone baseDrone(Status status){
        return Drone
                .builder()
                .id(DRONE_ID)
                .serialNumber(DRONE_SERIAL_NUMBER)
                .model(Lightweight)
                .weightLimit(DRONE_WEIGHT_LIMIT)
                .currentBatteryCapacity(DRONE_BATTERY_CAPACITY)
                .status(status)
                .build();
    }

    public Drone baseDrone(Status status, Integer batteryCapacity){
        return Drone
                .builder()
                .id(DRONE_ID)
                .serialNumber(DRONE_SERIAL_NUMBER)
                .model(Lightweight)
                .weightLimit(DRONE_WEIGHT_LIMIT)
                .currentBatteryCapacity(batteryCapacity)
                .status(status)
                .build();
    }

    public final Drone baseDroneIdNull = Drone
            .builder()
            .serialNumber(DRONE_SERIAL_NUMBER)
            .model(Lightweight)
            .weightLimit(DRONE_WEIGHT_LIMIT)
            .currentBatteryCapacity(DRONE_BATTERY_CAPACITY)
            .build();

    public Medication baseMedication(String code,
                                            String name,
                                            Integer weight,
                                            String imageUrl){
        return Medication
                .builder()
                .code(code)
                .name(name)
                .weight(weight)
                .imageUrl(imageUrl)
                .build();
    }

    public final DroneLoadMedication baseDroneLoadMedication = DroneLoadMedication
            .builder()
            .medication(baseMedication(
                    MEDICATION_CODE,
                    MEDICATION_NAME,
                    MEDICATION_WEIGHT,
                    MEDICATION_IMAGE))
            .amount(MEDICATION_AMOUNT)
            .build();

    public DroneLoad baseDroneLoad(Status status, Integer batteryCapacity){
        return DroneLoad
                .builder()
                .id(DRONE_LOAD_ID)
                .startTime(DRONE_LOAD_START_TIME)
                .drone(baseDrone(status, batteryCapacity))
                .droneLoadMedications(List.of(baseDroneLoadMedication))
                .build();
    }

    public DroneLoad baseDroneLoad(Status status){
        return DroneLoad
                .builder()
                .id(DRONE_LOAD_ID)
                .startTime(DRONE_LOAD_START_TIME)
                .drone(baseDrone(status))
                .droneLoadMedications(List.of(baseDroneLoadMedication))
                .build();
    }

    public DroneLoad baseDroneLoadMedicationsNull(){
        return DroneLoad
                .builder()
                .startTime(DRONE_LOAD_START_TIME)
                .drone(baseDrone(IDLE))
                .build();
    }

    public DroneLoad baseDroneLoadMedicationsNull(Integer id){
        return DroneLoad
                .builder()
                .id(id)
                .startTime(DRONE_LOAD_START_TIME)
                .drone(baseDrone(IDLE))
                .build();
    }

    public DroneLoad baseDroneLoad(Integer id, Status status, List<DroneLoadMedication> droneLoadMedications){
        return DroneLoad
                .builder()
                .id(id)
                .startTime(DRONE_LOAD_START_TIME)
                .drone(baseDrone(status))
                .droneLoadMedications(droneLoadMedications)
                .build();
    }

    public DroneRequest baseDroneRequest(){
        DroneRequest droneRequest = new DroneRequest();
        droneRequest.setSerialNumber(DRONE_SERIAL_NUMBER);
        droneRequest.setModel(Lightweight.toString());
        droneRequest.setWeightLimit(DRONE_WEIGHT_LIMIT);
        droneRequest.setCurrentBatteryCapacity(DRONE_BATTERY_CAPACITY);
        return droneRequest;
    }

    public MedicationRequest baseMedicationRequest(String code,
                                                          String name,
                                                          Integer weight,
                                                          String imageUrl,
                                                          Integer amount){
        MedicationRequest medicationRequest = new MedicationRequest();
        medicationRequest.setCode(code);
        medicationRequest.setName(name);
        medicationRequest.setWeight(weight);
        medicationRequest.setImageUrl(imageUrl);
        medicationRequest.setAmount(amount);
        return medicationRequest;
    }

    public DroneLoadMedicationsRequest baseDroneLoadMedicationsRequest(List<MedicationRequest> medicationRequest){
        DroneLoadMedicationsRequest droneLoadMedicationsRequest = new DroneLoadMedicationsRequest();
        droneLoadMedicationsRequest.setDroneId(DRONE_ID);
        droneLoadMedicationsRequest.setMedicationRequest(medicationRequest);
        return droneLoadMedicationsRequest;
    }

    public DroneResponse baseDroneResponse(Status status){
        return DroneResponse
                .builder()
                .droneId(DRONE_ID)
                .serialNumber(DRONE_SERIAL_NUMBER)
                .model(Lightweight.toString())
                .weightLimit(DRONE_WEIGHT_LIMIT)
                .currentBatteryCapacity(DRONE_BATTERY_CAPACITY)
                .status(status.toString())
                .build();
    }

    public DroneResponse baseDroneResponse(Integer id, Status status){
        return DroneResponse
                .builder()
                .droneId(id)
                .serialNumber(DRONE_SERIAL_NUMBER)
                .model(Lightweight.toString())
                .weightLimit(DRONE_WEIGHT_LIMIT)
                .currentBatteryCapacity(DRONE_BATTERY_CAPACITY)
                .status(status.toString())
                .build();
    }

    public DroneResponse baseDroneResponse(
            Integer droneId,
            String serialNumber,
            String model,
            Integer weightLimit,
            Integer currentBatteryCapacity,
            String status){
        return DroneResponse
                .builder()
                .droneId(droneId)
                .serialNumber(serialNumber)
                .model(model)
                .weightLimit(weightLimit)
                .currentBatteryCapacity(currentBatteryCapacity)
                .status(status)
                .build();
    }

    public final DroneLoadMedicationResponse baseDroneLoadMedicationResponse = DroneLoadMedicationResponse
            .builder()
            .code(MEDICATION_CODE)
            .name(MEDICATION_NAME)
            .weight(MEDICATION_WEIGHT)
            .imageUrl(MEDICATION_IMAGE)
            .amount(MEDICATION_AMOUNT)
            .build();

    public DroneLoadResponse baseDroneLoadResponse(Status status){
        return DroneLoadResponse
                .builder()
                .loadId(DRONE_LOAD_ID)
                .startTime(DRONE_LOAD_START_TIME)
                .drone(baseDroneResponse(status))
                .medications(List.of(baseDroneLoadMedicationResponse))
                .build();
    }

    public DroneLoadResponse baseDroneLoadResponse(Status status, List<DroneLoadMedicationResponse> medications){
        return DroneLoadResponse
                .builder()
                .loadId(DRONE_LOAD_ID)
                .startTime(DRONE_LOAD_START_TIME)
                .drone(baseDroneResponse(status))
                .medications(medications)
                .build();
    }

    public DroneLoadResponse baseDroneLoadResponse(Integer droneId, Status status, List<DroneLoadMedicationResponse> medications){
        return DroneLoadResponse
                .builder()
                .loadId(DRONE_LOAD_ID)
                .startTime(DRONE_LOAD_START_TIME)
                .drone(baseDroneResponse(droneId, status))
                .medications(medications)
                .build();
    }

    public DroneLoadResponse baseDroneLoadResponse(Integer droneId, Status status, List<DroneLoadMedicationResponse> medications, LocalDateTime endTime){
        return DroneLoadResponse
                .builder()
                .loadId(DRONE_LOAD_ID)
                .startTime(DRONE_LOAD_START_TIME)
                .endTime(endTime)
                .drone(baseDroneResponse(droneId, status))
                .medications(medications)
                .build();
    }

    public final DroneGeneralException baseDroneGeneralExceptionWeightLimit
            = new DroneGeneralException(ERROR_MESSAGE_WEIGHT_LIMIT);

    public final DroneGeneralException baseDroneGeneralExceptionDroneNotFound
            = new DroneGeneralException(ERROR_MESSAGE_DRONE_NOT_FOUND);

    public final DroneGeneralException baseDroneGeneralExceptionDuplicatedCodesMedications
            = new DroneGeneralException(ERROR_MESSAGE_DUPLICATED_CODES_MEDICATIONS);

    public final DroneGeneralException baseDroneGeneralExceptionMinimumBattery
            = new DroneGeneralException(ERROR_MESSAGE_MINIMUM_BATTERY);

    public final DroneGeneralException baseDroneGeneralExceptionNotIdle
            = new DroneGeneralException(ERROR_MESSAGE_NOT_IDLE);

    public final DroneGeneralException baseDroneGeneralExceptionNotLoading
            = new DroneGeneralException(ERROR_MESSAGE_NOT_LOADING);

    public final DroneGeneralException baseDroneGeneralExceptionNotLoaded
            = new DroneGeneralException(ERROR_MESSAGE_NOT_LOADED);

    public final DroneGeneralException baseDroneGeneralExceptionNotDelivering
            = new DroneGeneralException(ERROR_MESSAGE_NOT_DELIVERING);

    public final DroneGeneralException baseDroneGeneralExceptionNotDelivered
            = new DroneGeneralException(ERROR_MESSAGE_NOT_DELIVERED);

    public final DroneGeneralException baseDroneGeneralExceptionNotReturning
            = new DroneGeneralException(ERROR_MESSAGE_NOT_RETURNING);

    public final DroneGeneralException baseDroneGeneralExceptionOneMoreActiveLoads
            = new DroneGeneralException(ERROR_MESSAGE_ONE_MORE_ACTIVE_LOADS);

    public final DroneGeneralException baseDroneGeneralExceptionZeroMoreActiveLoads
            = new DroneGeneralException(ERROR_MESSAGE_ZERO_MORE_ONE_ACTIVE_LOADS);

    public final ErrorResponse baseErrorResponse
            = new ErrorResponse(ERROR_MESSAGE_WEIGHT_LIMIT);

}
