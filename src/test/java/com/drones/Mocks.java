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

import static com.drones.models.database.Drone.Status.IDLE;
import static com.drones.utils.Constants.ERROR_MESSAGE_DRONE_NOT_FOUND;


public final class Mocks {

    public static final Integer DRONE_ID = 123;
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

    public static final Drone baseDrone = Drone
            .builder()
            .id(DRONE_ID)
            .serialNumber(DRONE_SERIAL_NUMBER)
            .model(Lightweight)
            .weightLimit(DRONE_WEIGHT_LIMIT)
            .currentBatteryCapacity(DRONE_BATTERY_CAPACITY)
            .build();

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

    public static final DroneLoad baseDroneLoad = DroneLoad
            .builder()
            .id(DRONE_LOAD_ID)
            .startTime(DRONE_LOAD_START_TIME)
            .drone(baseDrone)
            .droneLoadMedications(List.of(baseDroneLoadMedication))
            .build();

    public static final DroneLoad baseDroneLoadIdNullMedicationsNull = DroneLoad
            .builder()
            .startTime(DRONE_LOAD_START_TIME)
            .drone(baseDrone)
            .build();

    public static final DroneRequest baseDroneRequest = DroneRequest
            .builder()
            .serialNumber(DRONE_SERIAL_NUMBER)
            .model(Lightweight.toString())
            .weightLimit(DRONE_WEIGHT_LIMIT)
            .currentBatteryCapacity(DRONE_BATTERY_CAPACITY)
            .build();

    public static final MedicationRequest baseMedicationRequest = MedicationRequest
            .builder()
            .code(MEDICATION_CODE)
            .name(MEDICATION_NAME)
            .weight(MEDICATION_WEIGHT)
            .imageUrl(MEDICATION_IMAGE)
            .amount(MEDICATION_AMOUNT)
            .build();

    public static final DroneResponse baseDroneResponse = DroneResponse
            .builder()
            .droneId(DRONE_ID)
            .serialNumber(DRONE_SERIAL_NUMBER)
            .model(Lightweight.toString())
            .weightLimit(DRONE_WEIGHT_LIMIT)
            .currentBatteryCapacity(DRONE_BATTERY_CAPACITY)
            .status(IDLE.toString())
            .build();

    public static final DroneLoadMedicationResponse baseDroneLoadMedicationResponse = DroneLoadMedicationResponse
            .builder()
            .code(MEDICATION_CODE)
            .name(MEDICATION_NAME)
            .weight(MEDICATION_WEIGHT)
            .imageUrl(MEDICATION_IMAGE)
            .amount(MEDICATION_AMOUNT)
            .build();

    public static final DroneLoadResponse baseDroneLoadResponse = DroneLoadResponse
            .builder()
            .loadId(DRONE_LOAD_ID)
            .startTime(DRONE_LOAD_START_TIME)
            .drone(baseDroneResponse)
            .medications(List.of(baseDroneLoadMedicationResponse))
            .build();

    public static final DroneGeneralException baseDroneGeneralException
            = new DroneGeneralException(ERROR_MESSAGE_DRONE_NOT_FOUND);

    public static final ErrorResponse baseErrorResponse
            = new ErrorResponse(ERROR_MESSAGE_DRONE_NOT_FOUND);

}
