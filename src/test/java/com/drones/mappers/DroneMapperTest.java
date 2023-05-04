package com.drones.mappers;

import com.drones.Mocks;
import com.drones.models.requests.DroneRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.drones.models.database.Drone.Model.Lightweight;
import static com.drones.models.database.Drone.Status.IDLE;


import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
class DroneMapperTest {
    @InjectMocks
    private DroneMapper subject;

    private Mocks mocks;

    @BeforeEach
    void beforeEach(){
        mocks = new Mocks();
    }

    @Test
    void toDroneResponse() {
        assertEquals(
                subject.toDroneResponse(mocks.baseDrone(IDLE)),
                mocks.baseDroneResponse(IDLE)
        );
    }

    @Test
    void toDroneLoadResponse() {
        assertEquals(
                subject.toDroneLoadResponse(mocks.baseDroneLoad(IDLE)),
                mocks.baseDroneLoadResponse(IDLE)
        );
    }

    @Test
    void toDroneLoadMedicationResponse() {
        assertEquals(
                subject.toDroneLoadMedicationResponse(mocks.baseDroneLoadMedication),
                mocks.baseDroneLoadMedicationResponse
        );
    }

    @Test
    void toDrone() {
        DroneRequest droneRequest = new DroneRequest();
        droneRequest.setSerialNumber(mocks.DRONE_SERIAL_NUMBER);
        droneRequest.setModel(Lightweight.toString());
        droneRequest.setWeightLimit(mocks.DRONE_WEIGHT_LIMIT);
        droneRequest.setCurrentBatteryCapacity(mocks.DRONE_BATTERY_CAPACITY);
        Mocks mocks = new Mocks();
        assertEquals(
                subject.toDrone(mocks.baseDroneRequest()),
                mocks.baseDroneIdNull
        );
    }

    @Test
    void toDroneLoad() {
        assertEquals(
                subject.toDroneLoad(mocks.baseDrone(IDLE), mocks.DRONE_LOAD_START_TIME),
                mocks.baseDroneLoadMedicationsNull()
        );
    }

    @Test
    void toMedication() {
        assertEquals(
                subject.toMedication(mocks.baseMedicationRequest(
                        mocks.MEDICATION_CODE,
                        mocks.MEDICATION_NAME,
                        mocks.MEDICATION_WEIGHT,
                        mocks.MEDICATION_IMAGE,
                        mocks.MEDICATION_AMOUNT
                        )),
                mocks.baseMedication(
                        mocks.MEDICATION_CODE,
                        mocks.MEDICATION_NAME,
                        mocks.MEDICATION_WEIGHT,
                        mocks.MEDICATION_IMAGE)
        );
    }

    @Test
    void toErrorResponse() {
        assertEquals(
                subject.toErrorResponse(mocks.baseDroneGeneralExceptionWeightLimit),
                mocks.baseErrorResponse
        );
    }
}