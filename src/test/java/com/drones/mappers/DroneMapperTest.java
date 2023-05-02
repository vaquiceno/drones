package com.drones.mappers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static com.drones.models.database.Drone.Status.IDLE;
import static com.drones.Mocks.baseDrone;
import static com.drones.Mocks.baseDroneResponse;
import static com.drones.Mocks.baseDroneLoad;
import static com.drones.Mocks.baseDroneLoadResponse;
import static com.drones.Mocks.baseDroneLoadMedication;
import static com.drones.Mocks.baseDroneLoadMedicationResponse;
import static com.drones.Mocks.baseDroneRequest;
import static com.drones.Mocks.baseDroneIdNull;
import static com.drones.Mocks.baseDroneLoadIdNullMedicationsNull;
import static com.drones.Mocks.baseMedicationRequest;
import static com.drones.Mocks.baseMedication;
import static com.drones.Mocks.baseDroneGeneralExceptionWeightLimit;
import static com.drones.Mocks.baseErrorResponse;
import static com.drones.Mocks.DRONE_LOAD_START_TIME;


import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
class DroneMapperTest {
    @InjectMocks
    private DroneMapper subject;

    @Test
    void toDroneResponse() {
        assertEquals(
                subject.toDroneResponse(baseDrone(IDLE)),
                baseDroneResponse(IDLE)
        );
    }

    @Test
    void toDroneLoadResponse() {
        assertEquals(
                subject.toDroneLoadResponse(baseDroneLoad(IDLE)),
                baseDroneLoadResponse(IDLE)
        );
    }

    @Test
    void toDroneLoadMedicationResponse() {
        assertEquals(
                subject.toDroneLoadMedicationResponse(baseDroneLoadMedication),
                baseDroneLoadMedicationResponse
        );
    }

    @Test
    void toDrone() {
        assertEquals(
                subject.toDrone(baseDroneRequest()),
                baseDroneIdNull
        );
    }

    @Test
    void toDroneLoad() {
        assertEquals(
                subject.toDroneLoad(baseDrone(IDLE), DRONE_LOAD_START_TIME),
                baseDroneLoadIdNullMedicationsNull
        );
    }

    @Test
    void toMedication() {
        assertEquals(
                subject.toMedication(baseMedicationRequest()),
                baseMedication
        );
    }

    @Test
    void toErrorResponse() {
        assertEquals(
                subject.toErrorResponse(baseDroneGeneralExceptionWeightLimit),
                baseErrorResponse
        );
    }
}