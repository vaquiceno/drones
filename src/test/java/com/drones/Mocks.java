package com.drones;

import com.drones.models.database.Drone;
import com.drones.models.database.DroneLoad;
import com.drones.models.database.DroneLoadMedication;
import com.drones.models.database.Medication;
import com.drones.models.exceptions.DroneGeneralException;
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
import static com.drones.utils.Constants.ERROR_MESSAGE_TOTAL_WEIGHT_MEDICATIONS;
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


public final class Mocks {

    public static final Integer DRONE_ID = 1;
    public static final String DRONE_SERIAL_NUMBER = "ABC";
    public static final Integer DRONE_WEIGHT_LIMIT = 50;
    public static final Integer DRONE_BATTERY_CAPACITY = 80;
    public static final Integer DRONE_LOAD_ID = 1;
    public static final LocalDateTime DRONE_LOAD_START_TIME = LocalDateTime.of(
            2023,
            3,
            8,
            19,
            27,
            18);

    public static final String MEDICATION_CODE = "XYZ";
    public static final String MEDICATION_NAME = "Bread";
    public static final Integer MEDICATION_WEIGHT = 10;
    public static final String MEDICATION_IMAGE = "/image/image.png";
    public static final Integer MEDICATION_AMOUNT = 2;

    public static final Integer DRONE_BATTERY_CAPACITY_LOW = 20;

    public static Drone baseDrone(Status status){
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

    public static Drone baseDrone(Status status, Integer batteryCapacity){
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

    public static final Drone baseDroneIdNull = Drone
            .builder()
            .serialNumber(DRONE_SERIAL_NUMBER)
            .model(Lightweight)
            .weightLimit(DRONE_WEIGHT_LIMIT)
            .currentBatteryCapacity(DRONE_BATTERY_CAPACITY)
            .build();

    public static final Medication baseMedication = Medication
            .builder()
            .code(MEDICATION_CODE)
            .name(MEDICATION_NAME)
            .weight(MEDICATION_WEIGHT)
            .imageUrl(MEDICATION_IMAGE)
            .build();

    public static final DroneLoadMedication baseDroneLoadMedication = DroneLoadMedication
            .builder()
            .medication(baseMedication)
            .amount(MEDICATION_AMOUNT)
            .build();

    public static DroneLoad baseDroneLoad(Status status, Integer batteryCapacity){
        return DroneLoad
                .builder()
                .id(DRONE_LOAD_ID)
                .startTime(DRONE_LOAD_START_TIME)
                .drone(baseDrone(status, batteryCapacity))
                .droneLoadMedications(List.of(baseDroneLoadMedication))
                .build();
    }

    public static DroneLoad baseDroneLoad(Status status){
        return DroneLoad
                .builder()
                .id(DRONE_LOAD_ID)
                .startTime(DRONE_LOAD_START_TIME)
                .drone(baseDrone(status))
                .droneLoadMedications(List.of(baseDroneLoadMedication))
                .build();
    }

    public static final DroneLoad baseDroneLoadIdNullMedicationsNull = DroneLoad
            .builder()
            .startTime(DRONE_LOAD_START_TIME)
            .drone(baseDrone(IDLE))
            .build();

    public static DroneRequest baseDroneRequest(){
        DroneRequest droneRequest = new DroneRequest();
        droneRequest.setSerialNumber(DRONE_SERIAL_NUMBER);
        droneRequest.setModel(Lightweight.toString());
        droneRequest.setWeightLimit(DRONE_WEIGHT_LIMIT);
        droneRequest.setCurrentBatteryCapacity(DRONE_BATTERY_CAPACITY);
        return droneRequest;
    }

    public static MedicationRequest baseMedicationRequest(){
        MedicationRequest medicationRequest = new MedicationRequest();
        medicationRequest.setCode(MEDICATION_CODE);
        medicationRequest.setName(MEDICATION_NAME);
        medicationRequest.setWeight(MEDICATION_WEIGHT);
        medicationRequest.setImageUrl(MEDICATION_IMAGE);
        medicationRequest.setAmount(MEDICATION_AMOUNT);
        return medicationRequest;
    }

    public static DroneResponse baseDroneResponse(Status status){
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

    public static final DroneLoadMedicationResponse baseDroneLoadMedicationResponse = DroneLoadMedicationResponse
            .builder()
            .code(MEDICATION_CODE)
            .name(MEDICATION_NAME)
            .weight(MEDICATION_WEIGHT)
            .imageUrl(MEDICATION_IMAGE)
            .amount(MEDICATION_AMOUNT)
            .build();

    public static DroneLoadResponse baseDroneLoadResponse(Status status){
        return DroneLoadResponse
                .builder()
                .loadId(DRONE_LOAD_ID)
                .startTime(DRONE_LOAD_START_TIME)
                .drone(baseDroneResponse(status))
                .medications(List.of(baseDroneLoadMedicationResponse))
                .build();
    }

    public static final DroneGeneralException baseDroneGeneralExceptionWeightLimit
            = new DroneGeneralException(ERROR_MESSAGE_WEIGHT_LIMIT);

    public static final DroneGeneralException baseDroneGeneralExceptionDroneNotFound
            = new DroneGeneralException(ERROR_MESSAGE_DRONE_NOT_FOUND);

    public static final DroneGeneralException baseDroneGeneralExceptionTotalWeightMedications
            = new DroneGeneralException(ERROR_MESSAGE_TOTAL_WEIGHT_MEDICATIONS);

    public static final DroneGeneralException baseDroneGeneralExceptionDuplicatedCodesMedications
            = new DroneGeneralException(ERROR_MESSAGE_DUPLICATED_CODES_MEDICATIONS);

    public static final DroneGeneralException baseDroneGeneralExceptionMinimumBattery
            = new DroneGeneralException(ERROR_MESSAGE_MINIMUM_BATTERY);

    public static final DroneGeneralException baseDroneGeneralExceptionNotIdle
            = new DroneGeneralException(ERROR_MESSAGE_NOT_IDLE);

    public static final DroneGeneralException baseDroneGeneralExceptionNotLoading
            = new DroneGeneralException(ERROR_MESSAGE_NOT_LOADING);

    public static final DroneGeneralException baseDroneGeneralExceptionNotLoaded
            = new DroneGeneralException(ERROR_MESSAGE_NOT_LOADED);

    public static final DroneGeneralException baseDroneGeneralExceptionNotDelivering
            = new DroneGeneralException(ERROR_MESSAGE_NOT_DELIVERING);

    public static final DroneGeneralException baseDroneGeneralExceptionNotDelivered
            = new DroneGeneralException(ERROR_MESSAGE_NOT_DELIVERED);

    public static final DroneGeneralException baseDroneGeneralExceptionNotReturning
            = new DroneGeneralException(ERROR_MESSAGE_NOT_RETURNING);

    public static final DroneGeneralException baseDroneGeneralExceptionOneMoreActiveLoads
            = new DroneGeneralException(ERROR_MESSAGE_ONE_MORE_ACTIVE_LOADS);

    public static final DroneGeneralException baseDroneGeneralExceptionZeroMoreActiveLoads
            = new DroneGeneralException(ERROR_MESSAGE_ZERO_MORE_ONE_ACTIVE_LOADS);

    public static final ErrorResponse baseErrorResponse
            = new ErrorResponse(ERROR_MESSAGE_WEIGHT_LIMIT);

}
