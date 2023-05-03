package com.drones.services;

import com.drones.mappers.DroneMapper;
import com.drones.models.database.DroneLoad;
import com.drones.models.exceptions.DroneGeneralException;
import com.drones.repositories.DroneLoadRepository;
import com.drones.repositories.DroneRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;


import static com.drones.Mocks.DRONE_BATTERY_CAPACITY_LOW;
import static com.drones.Mocks.DRONE_ID;
import static com.drones.Mocks.baseDrone;
import static com.drones.Mocks.baseDroneGeneralExceptionMinimumBattery;
import static com.drones.Mocks.baseDroneGeneralExceptionNotDelivered;
import static com.drones.Mocks.baseDroneGeneralExceptionNotDelivering;
import static com.drones.Mocks.baseDroneGeneralExceptionNotLoaded;
import static com.drones.Mocks.baseDroneGeneralExceptionNotLoading;
import static com.drones.Mocks.baseDroneGeneralExceptionNotReturning;
import static com.drones.Mocks.baseDroneGeneralExceptionZeroMoreActiveLoads;
import static com.drones.Mocks.baseDroneIdNull;
import static com.drones.Mocks.baseDroneLoad;
import static com.drones.Mocks.baseDroneLoadResponse;
import static com.drones.Mocks.baseDroneRequest;
import static com.drones.Mocks.baseDroneResponse;
import static com.drones.models.database.Drone.Status.IDLE;
import static com.drones.models.database.Drone.Status.LOADING;
import static com.drones.models.database.Drone.Status.LOADED;
import static com.drones.models.database.Drone.Status.DELIVERING;
import static com.drones.models.database.Drone.Status.DELIVERED;
import static com.drones.models.database.Drone.Status.RETURNING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
class DroneServiceTest {

    @Mock
    private DroneRepository droneRepository;

    @Mock
    private DroneLoadRepository droneLoadRepository;

    @Mock
    private DroneMapper droneMapper;

    @InjectMocks
    private DroneService subject;

    @Test
    void findAll() {
        Mockito.when(droneRepository.findAll()).thenReturn(List.of(baseDrone(IDLE)));
        Mockito.when(droneMapper.toDroneResponse(baseDrone(IDLE))).thenReturn(baseDroneResponse(IDLE));
        assertEquals(subject.findAll(), List.of(baseDroneResponse(IDLE)));
    }

    @Test
    void findAvailableDrones() {
        Mockito.when(droneRepository.findByStatus(IDLE)).thenReturn(List.of(baseDrone(IDLE)));
        Mockito.when(droneMapper.toDroneResponse(baseDrone(IDLE))).thenReturn(baseDroneResponse(IDLE));
        assertEquals(subject.findAvailableDrones(), List.of(baseDroneResponse(IDLE)));
    }

    @Test
    void findDrone() throws DroneGeneralException {
        Mockito.when(droneRepository.findById(DRONE_ID)).thenReturn(Optional.ofNullable(baseDrone(IDLE)));
        assert baseDrone(IDLE) != null;
        Mockito.when(droneMapper.toDroneResponse(baseDrone(IDLE))).thenReturn(baseDroneResponse(IDLE));
        assertEquals(subject.findDrone(DRONE_ID), baseDroneResponse(IDLE));
    }

    @Test
    void registerDrone() {
        Mockito.when(droneRepository.save(baseDroneIdNull)).thenReturn(baseDrone(IDLE));
        assert baseDrone(IDLE) != null;
        Mockito.when(droneMapper.toDrone(baseDroneRequest())).thenReturn(baseDroneIdNull);
        Mockito.when(droneMapper.toDroneResponse(baseDrone(IDLE))).thenReturn(baseDroneResponse(IDLE));
        assertEquals(subject.registerDrone(baseDroneRequest()), baseDroneResponse(IDLE));
        Mockito.verify(droneRepository, Mockito.times(1)).save(baseDroneIdNull);
    }

    @Test
    void auditDrones() {
        Mockito.when(droneRepository.findAll()).thenReturn(List.of(baseDrone(IDLE)));
        subject.auditDrones();
        Mockito.verify(droneRepository, Mockito.times(1)).findAll();
    }

    @Test
    void loadingDrone() {
    }

    @Test
    void loadedMedications() throws DroneGeneralException {
        Mockito.when(droneRepository.findById(DRONE_ID)).thenReturn(Optional.ofNullable(baseDrone(IDLE)));
        Mockito.when(droneLoadRepository.findByDroneAndEndTimeNull(baseDrone(IDLE))).thenReturn(List.of(baseDroneLoad(IDLE)));
        Mockito.when(droneMapper.toDroneLoadResponse(baseDroneLoad(IDLE))).thenReturn(baseDroneLoadResponse(IDLE));
        assertEquals(subject.loadedMedications(DRONE_ID), baseDroneLoadResponse(IDLE));
        Mockito.verify(droneRepository, Mockito.times(1)).findById(DRONE_ID);
        Mockito.verify(droneLoadRepository, Mockito.times(1)).findByDroneAndEndTimeNull(baseDrone(IDLE));
    }

    @Test
    void loadedMedicationsException() {
        Mockito.when(droneRepository.findById(DRONE_ID)).thenReturn(Optional.ofNullable(baseDrone(IDLE)));
        Mockito.when(droneLoadRepository.findByDroneAndEndTimeNull(baseDrone(IDLE))).thenReturn(List.of());
        assertEquals(
                assertThrows(DroneGeneralException.class,
                        () -> subject.loadedMedications(DRONE_ID)).getMessage(),
                baseDroneGeneralExceptionZeroMoreActiveLoads.getMessage()
        );
        Mockito.verify(droneRepository, Mockito.times(1)).findById(DRONE_ID);
        Mockito.verify(droneLoadRepository, Mockito.times(1)).findByDroneAndEndTimeNull(baseDrone(IDLE));
    }

    @Test
    void loadedDrone() throws DroneGeneralException {
        Mockito.when(droneRepository.findById(DRONE_ID)).thenReturn(Optional.ofNullable(baseDrone(LOADING)));
        Mockito.when(droneLoadRepository.findByDroneAndEndTimeNull(baseDrone(LOADING))).thenReturn(List.of(baseDroneLoad(LOADING)));
        Mockito.when(droneRepository.save(baseDrone(LOADED))).thenReturn(baseDrone(LOADED));
        Mockito.when(droneMapper.toDroneLoadResponse(baseDroneLoad(LOADED))).thenReturn(baseDroneLoadResponse(LOADED));
        assertEquals(subject.loadedDrone(DRONE_ID), baseDroneLoadResponse(LOADED));
        Mockito.verify(droneRepository, Mockito.times(1)).findById(DRONE_ID);
        Mockito.verify(droneLoadRepository, Mockito.times(1)).findByDroneAndEndTimeNull(baseDrone(LOADING));
    }

    @Test
    void loadedDroneException() {
        Mockito.when(droneRepository.findById(DRONE_ID)).thenReturn(Optional.ofNullable(baseDrone(IDLE)));
        Mockito.when(droneLoadRepository.findByDroneAndEndTimeNull(baseDrone(IDLE))).thenReturn(List.of(baseDroneLoad(IDLE)));
        assertEquals(
                assertThrows(DroneGeneralException.class,
                        () -> subject.loadedDrone(DRONE_ID)).getMessage(),
                baseDroneGeneralExceptionNotLoading.getMessage()
        );
        Mockito.verify(droneRepository, Mockito.times(1)).findById(DRONE_ID);
        Mockito.verify(droneLoadRepository, Mockito.times(1)).findByDroneAndEndTimeNull(baseDrone(IDLE));
    }

    @Test
    void deliveringDrone() throws DroneGeneralException {
        Mockito.when(droneRepository.findById(DRONE_ID)).thenReturn(Optional.ofNullable(baseDrone(LOADED)));
        Mockito.when(droneLoadRepository.findByDroneAndEndTimeNull(baseDrone(LOADED))).thenReturn(List.of(baseDroneLoad(LOADED)));
        Mockito.when(droneRepository.save(baseDrone(DELIVERING))).thenReturn(baseDrone(DELIVERING));
        Mockito.when(droneMapper.toDroneLoadResponse(baseDroneLoad(DELIVERING))).thenReturn(baseDroneLoadResponse(DELIVERING));
        assertEquals(subject.deliveringDrone(DRONE_ID), baseDroneLoadResponse(DELIVERING));
        Mockito.verify(droneRepository, Mockito.times(1)).findById(DRONE_ID);
        Mockito.verify(droneLoadRepository, Mockito.times(1)).findByDroneAndEndTimeNull(baseDrone(LOADED));
    }

    @Test
    void deliveringDroneException() {
        Mockito.when(droneRepository.findById(DRONE_ID)).thenReturn(Optional.ofNullable(baseDrone(IDLE)));
        Mockito.when(droneLoadRepository.findByDroneAndEndTimeNull(baseDrone(IDLE))).thenReturn(List.of(baseDroneLoad(IDLE)));
        assertEquals(
                assertThrows(DroneGeneralException.class,
                        () -> subject.deliveringDrone(DRONE_ID)).getMessage(),
                baseDroneGeneralExceptionNotLoaded.getMessage()
        );
        Mockito.verify(droneRepository, Mockito.times(1)).findById(DRONE_ID);
        Mockito.verify(droneLoadRepository, Mockito.times(1)).findByDroneAndEndTimeNull(baseDrone(IDLE));
    }

    @Test
    void deliveringDroneBatteryException() {
        Mockito.when(droneRepository.findById(DRONE_ID)).thenReturn(Optional.ofNullable(baseDrone(LOADED, DRONE_BATTERY_CAPACITY_LOW)));
        Mockito.when(droneLoadRepository.findByDroneAndEndTimeNull(baseDrone(LOADED, DRONE_BATTERY_CAPACITY_LOW))).thenReturn(List.of(baseDroneLoad(LOADED, DRONE_BATTERY_CAPACITY_LOW)));
        assertEquals(
                assertThrows(DroneGeneralException.class,
                        () -> subject.deliveringDrone(DRONE_ID)).getMessage(),
                baseDroneGeneralExceptionMinimumBattery.getMessage()
        );
        Mockito.verify(droneRepository, Mockito.times(1)).findById(DRONE_ID);
        Mockito.verify(droneLoadRepository, Mockito.times(1)).findByDroneAndEndTimeNull(baseDrone(LOADED, DRONE_BATTERY_CAPACITY_LOW));
    }

    @Test
    void deliveredDrone() throws DroneGeneralException {
        Mockito.when(droneRepository.findById(DRONE_ID)).thenReturn(Optional.ofNullable(baseDrone(DELIVERING)));
        Mockito.when(droneLoadRepository.findByDroneAndEndTimeNull(baseDrone(DELIVERING))).thenReturn(List.of(baseDroneLoad(DELIVERING)));
        Mockito.when(droneRepository.save(baseDrone(DELIVERED))).thenReturn(baseDrone(DELIVERED));
        Mockito.when(droneMapper.toDroneLoadResponse(baseDroneLoad(DELIVERED))).thenReturn(baseDroneLoadResponse(DELIVERED));
        assertEquals(subject.deliveredDrone(DRONE_ID), baseDroneLoadResponse(DELIVERED));
        Mockito.verify(droneRepository, Mockito.times(1)).findById(DRONE_ID);
        Mockito.verify(droneLoadRepository, Mockito.times(1)).findByDroneAndEndTimeNull(baseDrone(DELIVERING));
    }

    @Test
    void deliveredDroneException() {
        Mockito.when(droneRepository.findById(DRONE_ID)).thenReturn(Optional.ofNullable(baseDrone(IDLE)));
        Mockito.when(droneLoadRepository.findByDroneAndEndTimeNull(baseDrone(IDLE))).thenReturn(List.of(baseDroneLoad(IDLE)));
        assertEquals(
                assertThrows(DroneGeneralException.class,
                        () -> subject.deliveredDrone(DRONE_ID)).getMessage(),
                baseDroneGeneralExceptionNotDelivering.getMessage()
        );
        Mockito.verify(droneRepository, Mockito.times(1)).findById(DRONE_ID);
        Mockito.verify(droneLoadRepository, Mockito.times(1)).findByDroneAndEndTimeNull(baseDrone(IDLE));
    }

    @Test
    void returningDrone() throws DroneGeneralException {
        Mockito.when(droneRepository.findById(DRONE_ID)).thenReturn(Optional.ofNullable(baseDrone(DELIVERED)));
        Mockito.when(droneLoadRepository.findByDroneAndEndTimeNull(baseDrone(DELIVERED))).thenReturn(List.of(baseDroneLoad(DELIVERED)));
        Mockito.when(droneRepository.save(baseDrone(RETURNING))).thenReturn(baseDrone(RETURNING));
        Mockito.when(droneMapper.toDroneLoadResponse(baseDroneLoad(RETURNING))).thenReturn(baseDroneLoadResponse(RETURNING));
        assertEquals(subject.returningDrone(DRONE_ID), baseDroneLoadResponse(RETURNING));
        Mockito.verify(droneRepository, Mockito.times(1)).findById(DRONE_ID);
        Mockito.verify(droneLoadRepository, Mockito.times(1)).findByDroneAndEndTimeNull(baseDrone(DELIVERED));
    }

    @Test
    void returningDroneException() {
        Mockito.when(droneRepository.findById(DRONE_ID)).thenReturn(Optional.ofNullable(baseDrone(IDLE)));
        Mockito.when(droneLoadRepository.findByDroneAndEndTimeNull(baseDrone(IDLE))).thenReturn(List.of(baseDroneLoad(IDLE)));
        assertEquals(
                assertThrows(DroneGeneralException.class,
                        () -> subject.returningDrone(DRONE_ID)).getMessage(),
                baseDroneGeneralExceptionNotDelivered.getMessage()
        );
        Mockito.verify(droneRepository, Mockito.times(1)).findById(DRONE_ID);
        Mockito.verify(droneLoadRepository, Mockito.times(1)).findByDroneAndEndTimeNull(baseDrone(IDLE));
    }

    @Test
    void returningDroneBatteryException() {
        Mockito.when(droneRepository.findById(DRONE_ID)).thenReturn(Optional.ofNullable(baseDrone(DELIVERED, DRONE_BATTERY_CAPACITY_LOW)));
        Mockito.when(droneLoadRepository.findByDroneAndEndTimeNull(baseDrone(DELIVERED, DRONE_BATTERY_CAPACITY_LOW))).thenReturn(List.of(baseDroneLoad(DELIVERED, DRONE_BATTERY_CAPACITY_LOW)));
        assertEquals(
                assertThrows(DroneGeneralException.class,
                        () -> subject.returningDrone(DRONE_ID)).getMessage(),
                baseDroneGeneralExceptionMinimumBattery.getMessage()
        );
        Mockito.verify(droneRepository, Mockito.times(1)).findById(DRONE_ID);
        Mockito.verify(droneLoadRepository, Mockito.times(1)).findByDroneAndEndTimeNull(baseDrone(DELIVERED, DRONE_BATTERY_CAPACITY_LOW));
    }

    @Test
    void idleDrone() throws DroneGeneralException {
        Mockito.when(droneRepository.findById(DRONE_ID)).thenReturn(Optional.ofNullable(baseDrone(RETURNING)));
        Mockito.when(droneLoadRepository.findByDroneAndEndTimeNull(baseDrone(RETURNING))).thenReturn(List.of(baseDroneLoad(RETURNING)));
        Mockito.when(droneLoadRepository.save(Mockito.any(DroneLoad.class))).thenReturn(baseDroneLoad(RETURNING));
        Mockito.when(droneRepository.save(baseDrone(IDLE))).thenReturn(baseDrone(IDLE));
        Mockito.when(droneMapper.toDroneLoadResponse(Mockito.any(DroneLoad.class))).thenReturn(baseDroneLoadResponse(IDLE));
        assertEquals(subject.idleDrone(DRONE_ID), baseDroneLoadResponse(IDLE));
        Mockito.verify(droneRepository, Mockito.times(1)).findById(DRONE_ID);
        Mockito.verify(droneLoadRepository, Mockito.times(1)).findByDroneAndEndTimeNull(baseDrone(RETURNING));
    }

    @Test
    void idleDroneException() {
        Mockito.when(droneRepository.findById(DRONE_ID)).thenReturn(Optional.ofNullable(baseDrone(IDLE)));
        Mockito.when(droneLoadRepository.findByDroneAndEndTimeNull(baseDrone(IDLE))).thenReturn(List.of(baseDroneLoad(IDLE)));
        assertEquals(
                assertThrows(DroneGeneralException.class,
                        () -> subject.idleDrone(DRONE_ID)).getMessage(),
                baseDroneGeneralExceptionNotReturning.getMessage()
        );
        Mockito.verify(droneRepository, Mockito.times(1)).findById(DRONE_ID);
        Mockito.verify(droneLoadRepository, Mockito.times(1)).findByDroneAndEndTimeNull(baseDrone(IDLE));
    }
}