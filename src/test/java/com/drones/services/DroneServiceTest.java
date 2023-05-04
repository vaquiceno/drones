package com.drones.services;

import com.drones.Mocks;
import com.drones.mappers.DroneMapper;
import com.drones.models.database.Drone;
import com.drones.models.database.DroneLoad;
import com.drones.models.database.Medication;
import com.drones.models.exceptions.DroneGeneralException;
import com.drones.models.requests.DroneLoadMedicationsRequest;
import com.drones.models.requests.MedicationRequest;
import com.drones.models.responses.DroneLoadMedicationResponse;
import com.drones.repositories.DroneLoadRepository;
import com.drones.repositories.DroneRepository;
import com.drones.repositories.MedicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


import static com.drones.models.database.Drone.Status.IDLE;
import static com.drones.models.database.Drone.Status.LOADING;
import static com.drones.models.database.Drone.Status.LOADED;
import static com.drones.models.database.Drone.Status.DELIVERING;
import static com.drones.models.database.Drone.Status.DELIVERED;
import static com.drones.models.database.Drone.Status.RETURNING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(SpringExtension.class)
class DroneServiceTest {

    @Mock
    private DroneRepository droneRepository;

    @Mock
    private DroneLoadRepository droneLoadRepository;

    @Mock
    private MedicationRepository medicationRepository;

    @Mock
    private DroneMapper droneMapper;

    private Mocks mocks;

    @InjectMocks
    private DroneService subject;

    @BeforeEach
    void beforeEach(){
        mocks = new Mocks();
    }

    @Test
    void findAll() {
        Mockito.when(droneRepository.findAll()).thenReturn(List.of(mocks.baseDrone(IDLE)));
        Mockito.when(droneMapper.toDroneResponse(mocks.baseDrone(IDLE))).thenReturn(mocks.baseDroneResponse(IDLE));
        assertEquals(subject.findAll(), List.of(mocks.baseDroneResponse(IDLE)));
    }

    @Test
    void findAvailableDrones() {
        Mockito.when(droneRepository.findByStatus(IDLE)).thenReturn(List.of(mocks.baseDrone(IDLE)));
        Mockito.when(droneMapper.toDroneResponse(mocks.baseDrone(IDLE))).thenReturn(mocks.baseDroneResponse(IDLE));
        assertEquals(subject.findAvailableDrones(), List.of(mocks.baseDroneResponse(IDLE)));
    }

    @Test
    void findDrone() throws DroneGeneralException {
        Mockito.when(droneRepository.findById(mocks.DRONE_ID)).thenReturn(Optional.ofNullable(mocks.baseDrone(IDLE)));
        assert mocks.baseDrone(IDLE) != null;
        Mockito.when(droneMapper.toDroneResponse(mocks.baseDrone(IDLE))).thenReturn(mocks.baseDroneResponse(IDLE));
        assertEquals(subject.findDrone(mocks.DRONE_ID), mocks.baseDroneResponse(IDLE));
    }

    @Test
    void registerDrone() {
        Mockito.when(droneRepository.save(mocks.baseDroneIdNull)).thenReturn(mocks.baseDrone(IDLE));
        assert mocks.baseDrone(IDLE) != null;
        Mockito.when(droneMapper.toDrone(mocks.baseDroneRequest())).thenReturn(mocks.baseDroneIdNull);
        Mockito.when(droneMapper.toDroneResponse(mocks.baseDrone(IDLE))).thenReturn(mocks.baseDroneResponse(IDLE));
        assertEquals(subject.registerDrone(mocks.baseDroneRequest()), mocks.baseDroneResponse(IDLE));
        Mockito.verify(droneRepository, Mockito.times(1)).save(mocks.baseDroneIdNull);
    }

    @Test
    void auditDrones() {
        Mockito.when(droneRepository.findAll()).thenReturn(List.of(mocks.baseDrone(IDLE)));
        subject.auditDrones();
        Mockito.verify(droneRepository, Mockito.times(1)).findAll();
    }

    @Test
    void loadingDrone() throws DroneGeneralException {
        MedicationRequest medicationRequest1 = mocks.baseMedicationRequest(
                "6985",
                "Bread",
                10,
                "https://regex101.com//img.das",
                2);
        MedicationRequest medicationRequest2 = mocks.baseMedicationRequest(
                "69852",
                "Vine",
                5,
                null,
                3);
        Medication medication1 = mocks.baseMedication(
                "6985",
                "Bread",
                10,
                "https://regex101.com//img.das");
        Medication medication2 = mocks.baseMedication(
                "69852",
                "Vine",
                5,
                null);
        DroneLoadMedicationsRequest droneLoadMedicationsRequest = mocks.baseDroneLoadMedicationsRequest(
                List.of(medicationRequest1, medicationRequest2)
        );
        List<Medication> medications = List.of(medication1, medication2);
        List<DroneLoadMedicationResponse> droneLoadMedicationResponses = List.of(
                DroneLoadMedicationResponse
                        .builder()
                        .code("6985")
                        .name("Bread")
                        .weight(10)
                        .imageUrl("https://regex101.com//img.das")
                        .amount(2)
                        .build(),
                DroneLoadMedicationResponse.builder()
                        .code("69852")
                        .name("Vine")
                        .weight(5)
                        .amount(3)
                        .build()
        );
        Mockito.when(droneRepository.findById(mocks.DRONE_ID)).thenReturn(Optional.ofNullable(mocks.baseDrone(IDLE)));
        Mockito.when(droneLoadRepository.findByDroneAndEndTimeNull(mocks.baseDrone(IDLE))).thenReturn(List.of());
        Mockito.when(droneMapper.toMedication(medicationRequest1)).thenReturn(medication1);
        Mockito.when(droneMapper.toMedication(medicationRequest2)).thenReturn(medication2);
        Mockito.when(medicationRepository.saveAll(medications)).thenReturn(medications);
        Mockito.when(droneMapper.toDroneLoad(eq(mocks.baseDrone(IDLE)), Mockito.any(LocalDateTime.class))).thenReturn(mocks.baseDroneLoadMedicationsNull());
        Mockito.when(droneLoadRepository.save(mocks.baseDroneLoadMedicationsNull())).thenReturn(mocks.baseDroneLoadMedicationsNull(mocks.DRONE_LOAD_ID));
        Mockito.when(droneMapper.toDroneLoadResponse(Mockito.any(DroneLoad.class))).thenReturn(mocks.baseDroneLoadResponse(LOADING, droneLoadMedicationResponses));
        assertEquals(subject.loadingDrone(droneLoadMedicationsRequest), mocks.baseDroneLoadResponse(LOADING, droneLoadMedicationResponses));
        Mockito.verify(droneRepository, Mockito.times(1)).findById(mocks.DRONE_ID);
        Mockito.verify(droneLoadRepository, Mockito.times(1)).findByDroneAndEndTimeNull(mocks.baseDrone(LOADING));
        Mockito.verify(droneMapper, Mockito.times(2)).toMedication(medicationRequest1);
        Mockito.verify(droneMapper, Mockito.times(2)).toMedication(medicationRequest2);
        Mockito.verify(medicationRepository, Mockito.times(1)).saveAll(medications);
        Mockito.verify(droneLoadRepository, Mockito.times(1)).save(mocks.baseDroneLoadMedicationsNull());
        Mockito.verify(droneMapper, Mockito.times(1)).toDroneLoadResponse(Mockito.any(DroneLoad.class));
    }

    @Test
    void loadingDroneDuplicatedCodesException() {
        MedicationRequest medicationRequest1 = mocks.baseMedicationRequest(
                "6985",
                "Bread",
                10,
                "https://regex101.com//img.das",
                2);
        MedicationRequest medicationRequest2 = mocks.baseMedicationRequest(
                "6985",
                "Vine",
                5,
                null,
                3);
        DroneLoadMedicationsRequest droneLoadMedicationsRequest = mocks.baseDroneLoadMedicationsRequest(
                List.of(medicationRequest1, medicationRequest2)
        );
        assertEquals(
                assertThrows(DroneGeneralException.class,
                        () -> subject.loadingDrone(droneLoadMedicationsRequest)).getMessage(),
                mocks.baseDroneGeneralExceptionDuplicatedCodesMedications.getMessage()
        );
        Mockito.verify(droneRepository, Mockito.never()).findById(Mockito.any(Integer.class));
        Mockito.verify(droneLoadRepository, Mockito.never()).findByDroneAndEndTimeNull(Mockito.any(Drone.class));
        Mockito.verify(droneMapper, Mockito.never()).toMedication(Mockito.any(MedicationRequest.class));
        Mockito.verify(medicationRepository, Mockito.never()).saveAll(Mockito.any());
        Mockito.verify(droneLoadRepository, Mockito.never()).save(Mockito.any(DroneLoad.class));
        Mockito.verify(droneMapper, Mockito.never()).toDroneLoadResponse(Mockito.any(DroneLoad.class));
    }

    @Test
    void loadingDroneNotFoundException() {
        MedicationRequest medicationRequest1 = mocks.baseMedicationRequest(
                "6985",
                "Bread",
                10,
                "https://regex101.com//img.das",
                2);
        MedicationRequest medicationRequest2 = mocks.baseMedicationRequest(
                "69856",
                "Vine",
                5,
                null,
                3);
        DroneLoadMedicationsRequest droneLoadMedicationsRequest = mocks.baseDroneLoadMedicationsRequest(
                List.of(medicationRequest1, medicationRequest2)
        );
        Mockito.when(droneRepository.findById(mocks.DRONE_ID)).thenReturn(Optional.empty());
        assertEquals(
                assertThrows(DroneGeneralException.class,
                        () -> subject.loadingDrone(droneLoadMedicationsRequest)).getMessage(),
                mocks.baseDroneGeneralExceptionDroneNotFound.getMessage()
        );
        Mockito.verify(droneRepository, Mockito.times(1)).findById(mocks.DRONE_ID);
        Mockito.verify(droneLoadRepository, Mockito.never()).findByDroneAndEndTimeNull(Mockito.any(Drone.class));
        Mockito.verify(droneMapper, Mockito.never()).toMedication(Mockito.any(MedicationRequest.class));
        Mockito.verify(medicationRepository, Mockito.never()).saveAll(Mockito.any());
        Mockito.verify(droneLoadRepository, Mockito.never()).save(Mockito.any(DroneLoad.class));
        Mockito.verify(droneMapper, Mockito.never()).toDroneLoadResponse(Mockito.any(DroneLoad.class));
    }

    @Test
    void loadingDroneNotIdleException() {
        MedicationRequest medicationRequest1 = mocks.baseMedicationRequest(
                "6985",
                "Bread",
                10,
                "https://regex101.com//img.das",
                2);
        MedicationRequest medicationRequest2 = mocks.baseMedicationRequest(
                "69856",
                "Vine",
                5,
                null,
                3);
        DroneLoadMedicationsRequest droneLoadMedicationsRequest = mocks.baseDroneLoadMedicationsRequest(
                List.of(medicationRequest1, medicationRequest2)
        );
        Mockito.when(droneRepository.findById(mocks.DRONE_ID)).thenReturn(Optional.ofNullable(mocks.baseDrone(RETURNING)));
        assertEquals(
                assertThrows(DroneGeneralException.class,
                        () -> subject.loadingDrone(droneLoadMedicationsRequest)).getMessage(),
                mocks.baseDroneGeneralExceptionNotIdle.getMessage()
        );
        Mockito.verify(droneRepository, Mockito.times(1)).findById(mocks.DRONE_ID);
        Mockito.verify(droneLoadRepository, Mockito.never()).findByDroneAndEndTimeNull(Mockito.any(Drone.class));
        Mockito.verify(droneMapper, Mockito.never()).toMedication(Mockito.any(MedicationRequest.class));
        Mockito.verify(medicationRepository, Mockito.never()).saveAll(Mockito.any());
        Mockito.verify(droneLoadRepository, Mockito.never()).save(Mockito.any(DroneLoad.class));
        Mockito.verify(droneMapper, Mockito.never()).toDroneLoadResponse(Mockito.any(DroneLoad.class));
    }

    @Test
    void loadingDroneBatteryException() {
        MedicationRequest medicationRequest1 = mocks.baseMedicationRequest(
                "6985",
                "Bread",
                10,
                "https://regex101.com//img.das",
                2);
        MedicationRequest medicationRequest2 = mocks.baseMedicationRequest(
                "69856",
                "Vine",
                5,
                null,
                3);
        DroneLoadMedicationsRequest droneLoadMedicationsRequest = mocks.baseDroneLoadMedicationsRequest(
                List.of(medicationRequest1, medicationRequest2)
        );
        Mockito.when(droneRepository.findById(mocks.DRONE_ID)).thenReturn(Optional.ofNullable(mocks.baseDrone(IDLE, mocks.DRONE_BATTERY_CAPACITY_LOW)));
        assertEquals(
                assertThrows(DroneGeneralException.class,
                        () -> subject.loadingDrone(droneLoadMedicationsRequest)).getMessage(),
                mocks.baseDroneGeneralExceptionMinimumBattery.getMessage()
        );
        Mockito.verify(droneRepository, Mockito.times(1)).findById(mocks.DRONE_ID);
        Mockito.verify(droneLoadRepository, Mockito.never()).findByDroneAndEndTimeNull(Mockito.any(Drone.class));
        Mockito.verify(droneMapper, Mockito.never()).toMedication(Mockito.any(MedicationRequest.class));
        Mockito.verify(medicationRepository, Mockito.never()).saveAll(Mockito.any());
        Mockito.verify(droneLoadRepository, Mockito.never()).save(Mockito.any(DroneLoad.class));
        Mockito.verify(droneMapper, Mockito.never()).toDroneLoadResponse(Mockito.any(DroneLoad.class));
    }

    @Test
    void loadingDroneWeightLimitException() {
        MedicationRequest medicationRequest1 = mocks.baseMedicationRequest(
                "6985",
                "Bread",
                100,
                "https://regex101.com//img.das",
                20);
        MedicationRequest medicationRequest2 = mocks.baseMedicationRequest(
                "69856",
                "Vine",
                5,
                null,
                3);
        DroneLoadMedicationsRequest droneLoadMedicationsRequest = mocks.baseDroneLoadMedicationsRequest(
                List.of(medicationRequest1, medicationRequest2)
        );
        Mockito.when(droneRepository.findById(mocks.DRONE_ID)).thenReturn(Optional.ofNullable(mocks.baseDrone(IDLE)));
        assertEquals(
                assertThrows(DroneGeneralException.class,
                        () -> subject.loadingDrone(droneLoadMedicationsRequest)).getMessage(),
                mocks.baseDroneGeneralExceptionWeightLimit.getMessage()
        );
        Mockito.verify(droneRepository, Mockito.times(1)).findById(mocks.DRONE_ID);
        Mockito.verify(droneLoadRepository, Mockito.never()).findByDroneAndEndTimeNull(Mockito.any(Drone.class));
        Mockito.verify(droneMapper, Mockito.never()).toMedication(Mockito.any(MedicationRequest.class));
        Mockito.verify(medicationRepository, Mockito.never()).saveAll(Mockito.any());
        Mockito.verify(droneLoadRepository, Mockito.never()).save(Mockito.any(DroneLoad.class));
        Mockito.verify(droneMapper, Mockito.never()).toDroneLoadResponse(Mockito.any(DroneLoad.class));
    }

    @Test
    void loadingDroneOneMoreLoadsException() {
        MedicationRequest medicationRequest1 = mocks.baseMedicationRequest(
                "6985",
                "Bread",
                10,
                "https://regex101.com//img.das",
                2);
        MedicationRequest medicationRequest2 = mocks.baseMedicationRequest(
                "69856",
                "Vine",
                5,
                null,
                3);
        DroneLoadMedicationsRequest droneLoadMedicationsRequest = mocks.baseDroneLoadMedicationsRequest(
                List.of(medicationRequest1, medicationRequest2)
        );
        Mockito.when(droneRepository.findById(mocks.DRONE_ID)).thenReturn(Optional.ofNullable(mocks.baseDrone(IDLE)));
        Mockito.when(droneLoadRepository.findByDroneAndEndTimeNull(mocks.baseDrone(IDLE))).thenReturn(List.of(mocks.baseDroneLoad(IDLE)));
        assertEquals(
                assertThrows(DroneGeneralException.class,
                        () -> subject.loadingDrone(droneLoadMedicationsRequest)).getMessage(),
                mocks.baseDroneGeneralExceptionOneMoreActiveLoads.getMessage()
        );
        Mockito.verify(droneRepository, Mockito.times(1)).findById(mocks.DRONE_ID);
        Mockito.verify(droneLoadRepository, Mockito.times(1)).findByDroneAndEndTimeNull(mocks.baseDrone(IDLE));
        Mockito.verify(droneMapper, Mockito.never()).toMedication(Mockito.any(MedicationRequest.class));
        Mockito.verify(medicationRepository, Mockito.never()).saveAll(Mockito.any());
        Mockito.verify(droneLoadRepository, Mockito.never()).save(Mockito.any(DroneLoad.class));
        Mockito.verify(droneMapper, Mockito.never()).toDroneLoadResponse(Mockito.any(DroneLoad.class));
    }

    @Test
    void loadedMedications() throws DroneGeneralException {
        Mockito.when(droneRepository.findById(mocks.DRONE_ID)).thenReturn(Optional.ofNullable(mocks.baseDrone(IDLE)));
        Mockito.when(droneLoadRepository.findByDroneAndEndTimeNull(mocks.baseDrone(IDLE))).thenReturn(List.of(mocks.baseDroneLoad(IDLE)));
        Mockito.when(droneMapper.toDroneLoadResponse(mocks.baseDroneLoad(IDLE))).thenReturn(mocks.baseDroneLoadResponse(IDLE));
        assertEquals(subject.loadedMedications(mocks.DRONE_ID), mocks.baseDroneLoadResponse(IDLE));
        Mockito.verify(droneRepository, Mockito.times(1)).findById(mocks.DRONE_ID);
        Mockito.verify(droneLoadRepository, Mockito.times(1)).findByDroneAndEndTimeNull(mocks.baseDrone(IDLE));
    }

    @Test
    void loadedMedicationsException() {
        Mockito.when(droneRepository.findById(mocks.DRONE_ID)).thenReturn(Optional.ofNullable(mocks.baseDrone(IDLE)));
        Mockito.when(droneLoadRepository.findByDroneAndEndTimeNull(mocks.baseDrone(IDLE))).thenReturn(List.of());
        assertEquals(
                assertThrows(DroneGeneralException.class,
                        () -> subject.loadedMedications(mocks.DRONE_ID)).getMessage(),
                mocks.baseDroneGeneralExceptionZeroMoreActiveLoads.getMessage()
        );
        Mockito.verify(droneRepository, Mockito.times(1)).findById(mocks.DRONE_ID);
        Mockito.verify(droneLoadRepository, Mockito.times(1)).findByDroneAndEndTimeNull(mocks.baseDrone(IDLE));
    }

    @Test
    void loadedDrone() throws DroneGeneralException {
        Mockito.when(droneRepository.findById(mocks.DRONE_ID)).thenReturn(Optional.ofNullable(mocks.baseDrone(LOADING)));
        Mockito.when(droneLoadRepository.findByDroneAndEndTimeNull(mocks.baseDrone(LOADING))).thenReturn(List.of(mocks.baseDroneLoad(LOADING)));
        Mockito.when(droneMapper.toDroneLoadResponse(mocks.baseDroneLoad(LOADED))).thenReturn(mocks.baseDroneLoadResponse(LOADED));
        assertEquals(subject.loadedDrone(mocks.DRONE_ID), mocks.baseDroneLoadResponse(LOADED));
        Mockito.verify(droneRepository, Mockito.times(1)).findById(mocks.DRONE_ID);
        Mockito.verify(droneLoadRepository, Mockito.times(1)).findByDroneAndEndTimeNull(mocks.baseDrone(LOADING));
    }

    @Test
    void loadedDroneException() {
        Mockito.when(droneRepository.findById(mocks.DRONE_ID)).thenReturn(Optional.ofNullable(mocks.baseDrone(IDLE)));
        Mockito.when(droneLoadRepository.findByDroneAndEndTimeNull(mocks.baseDrone(IDLE))).thenReturn(List.of(mocks.baseDroneLoad(IDLE)));
        assertEquals(
                assertThrows(DroneGeneralException.class,
                        () -> subject.loadedDrone(mocks.DRONE_ID)).getMessage(),
                mocks.baseDroneGeneralExceptionNotLoading.getMessage()
        );
        Mockito.verify(droneRepository, Mockito.times(1)).findById(mocks.DRONE_ID);
        Mockito.verify(droneLoadRepository, Mockito.times(1)).findByDroneAndEndTimeNull(mocks.baseDrone(IDLE));
    }

    @Test
    void deliveringDrone() throws DroneGeneralException {
        Mockito.when(droneRepository.findById(mocks.DRONE_ID)).thenReturn(Optional.ofNullable(mocks.baseDrone(LOADED)));
        Mockito.when(droneLoadRepository.findByDroneAndEndTimeNull(mocks.baseDrone(LOADED))).thenReturn(List.of(mocks.baseDroneLoad(LOADED)));
        Mockito.when(droneMapper.toDroneLoadResponse(mocks.baseDroneLoad(DELIVERING))).thenReturn(mocks.baseDroneLoadResponse(DELIVERING));
        assertEquals(subject.deliveringDrone(mocks.DRONE_ID), mocks.baseDroneLoadResponse(DELIVERING));
        Mockito.verify(droneRepository, Mockito.times(1)).findById(mocks.DRONE_ID);
        Mockito.verify(droneLoadRepository, Mockito.times(1)).findByDroneAndEndTimeNull(mocks.baseDrone(LOADED));
    }

    @Test
    void deliveringDroneException() {
        Mockito.when(droneRepository.findById(mocks.DRONE_ID)).thenReturn(Optional.ofNullable(mocks.baseDrone(IDLE)));
        Mockito.when(droneLoadRepository.findByDroneAndEndTimeNull(mocks.baseDrone(IDLE))).thenReturn(List.of(mocks.baseDroneLoad(IDLE)));
        assertEquals(
                assertThrows(DroneGeneralException.class,
                        () -> subject.deliveringDrone(mocks.DRONE_ID)).getMessage(),
                mocks.baseDroneGeneralExceptionNotLoaded.getMessage()
        );
        Mockito.verify(droneRepository, Mockito.times(1)).findById(mocks.DRONE_ID);
        Mockito.verify(droneLoadRepository, Mockito.times(1)).findByDroneAndEndTimeNull(mocks.baseDrone(IDLE));
    }

    @Test
    void deliveringDroneBatteryException() {
        Mockito.when(droneRepository.findById(mocks.DRONE_ID)).thenReturn(Optional.ofNullable(mocks.baseDrone(LOADED, mocks.DRONE_BATTERY_CAPACITY_LOW)));
        Mockito.when(droneLoadRepository.findByDroneAndEndTimeNull(mocks.baseDrone(LOADED, mocks.DRONE_BATTERY_CAPACITY_LOW))).thenReturn(List.of(mocks.baseDroneLoad(LOADED, mocks.DRONE_BATTERY_CAPACITY_LOW)));
        assertEquals(
                assertThrows(DroneGeneralException.class,
                        () -> subject.deliveringDrone(mocks.DRONE_ID)).getMessage(),
                mocks.baseDroneGeneralExceptionMinimumBattery.getMessage()
        );
        Mockito.verify(droneRepository, Mockito.times(1)).findById(mocks.DRONE_ID);
        Mockito.verify(droneLoadRepository, Mockito.times(1)).findByDroneAndEndTimeNull(mocks.baseDrone(LOADED, mocks.DRONE_BATTERY_CAPACITY_LOW));
    }

    @Test
    void deliveredDrone() throws DroneGeneralException {
        Mockito.when(droneRepository.findById(mocks.DRONE_ID)).thenReturn(Optional.ofNullable(mocks.baseDrone(DELIVERING)));
        Mockito.when(droneLoadRepository.findByDroneAndEndTimeNull(mocks.baseDrone(DELIVERING))).thenReturn(List.of(mocks.baseDroneLoad(DELIVERING)));
        Mockito.when(droneMapper.toDroneLoadResponse(mocks.baseDroneLoad(DELIVERED))).thenReturn(mocks.baseDroneLoadResponse(DELIVERED));
        assertEquals(subject.deliveredDrone(mocks.DRONE_ID), mocks.baseDroneLoadResponse(DELIVERED));
        Mockito.verify(droneRepository, Mockito.times(1)).findById(mocks.DRONE_ID);
        Mockito.verify(droneLoadRepository, Mockito.times(1)).findByDroneAndEndTimeNull(mocks.baseDrone(DELIVERING));
    }

    @Test
    void deliveredDroneException() {
        Mockito.when(droneRepository.findById(mocks.DRONE_ID)).thenReturn(Optional.ofNullable(mocks.baseDrone(IDLE)));
        Mockito.when(droneLoadRepository.findByDroneAndEndTimeNull(mocks.baseDrone(IDLE))).thenReturn(List.of(mocks.baseDroneLoad(IDLE)));
        assertEquals(
                assertThrows(DroneGeneralException.class,
                        () -> subject.deliveredDrone(mocks.DRONE_ID)).getMessage(),
                mocks.baseDroneGeneralExceptionNotDelivering.getMessage()
        );
        Mockito.verify(droneRepository, Mockito.times(1)).findById(mocks.DRONE_ID);
        Mockito.verify(droneLoadRepository, Mockito.times(1)).findByDroneAndEndTimeNull(mocks.baseDrone(IDLE));
    }

    @Test
    void returningDrone() throws DroneGeneralException {
        Mockito.when(droneRepository.findById(mocks.DRONE_ID)).thenReturn(Optional.ofNullable(mocks.baseDrone(DELIVERED)));
        Mockito.when(droneLoadRepository.findByDroneAndEndTimeNull(mocks.baseDrone(DELIVERED))).thenReturn(List.of(mocks.baseDroneLoad(DELIVERED)));
        Mockito.when(droneMapper.toDroneLoadResponse(mocks.baseDroneLoad(RETURNING))).thenReturn(mocks.baseDroneLoadResponse(RETURNING));
        assertEquals(subject.returningDrone(mocks.DRONE_ID), mocks.baseDroneLoadResponse(RETURNING));
        Mockito.verify(droneRepository, Mockito.times(1)).findById(mocks.DRONE_ID);
        Mockito.verify(droneLoadRepository, Mockito.times(1)).findByDroneAndEndTimeNull(mocks.baseDrone(DELIVERED));
    }

    @Test
    void returningDroneException() {
        Mockito.when(droneRepository.findById(mocks.DRONE_ID)).thenReturn(Optional.ofNullable(mocks.baseDrone(IDLE)));
        Mockito.when(droneLoadRepository.findByDroneAndEndTimeNull(mocks.baseDrone(IDLE))).thenReturn(List.of(mocks.baseDroneLoad(IDLE)));
        assertEquals(
                assertThrows(DroneGeneralException.class,
                        () -> subject.returningDrone(mocks.DRONE_ID)).getMessage(),
                mocks.baseDroneGeneralExceptionNotDelivered.getMessage()
        );
        Mockito.verify(droneRepository, Mockito.times(1)).findById(mocks.DRONE_ID);
        Mockito.verify(droneLoadRepository, Mockito.times(1)).findByDroneAndEndTimeNull(mocks.baseDrone(IDLE));
    }

    @Test
    void returningDroneBatteryException() {
        Mockito.when(droneRepository.findById(mocks.DRONE_ID)).thenReturn(Optional.ofNullable(mocks.baseDrone(DELIVERED, mocks.DRONE_BATTERY_CAPACITY_LOW)));
        Mockito.when(droneLoadRepository.findByDroneAndEndTimeNull(mocks.baseDrone(DELIVERED, mocks.DRONE_BATTERY_CAPACITY_LOW))).thenReturn(List.of(mocks.baseDroneLoad(DELIVERED, mocks.DRONE_BATTERY_CAPACITY_LOW)));
        assertEquals(
                assertThrows(DroneGeneralException.class,
                        () -> subject.returningDrone(mocks.DRONE_ID)).getMessage(),
                mocks.baseDroneGeneralExceptionMinimumBattery.getMessage()
        );
        Mockito.verify(droneRepository, Mockito.times(1)).findById(mocks.DRONE_ID);
        Mockito.verify(droneLoadRepository, Mockito.times(1)).findByDroneAndEndTimeNull(mocks.baseDrone(DELIVERED, mocks.DRONE_BATTERY_CAPACITY_LOW));
    }

    @Test
    void idleDrone() throws DroneGeneralException {
        Mockito.when(droneRepository.findById(mocks.DRONE_ID)).thenReturn(Optional.ofNullable(mocks.baseDrone(RETURNING)));
        Mockito.when(droneLoadRepository.findByDroneAndEndTimeNull(mocks.baseDrone(RETURNING))).thenReturn(List.of(mocks.baseDroneLoad(RETURNING)));
        Mockito.when(droneMapper.toDroneLoadResponse(Mockito.any(DroneLoad.class))).thenReturn(mocks.baseDroneLoadResponse(IDLE));
        assertEquals(subject.idleDrone(mocks.DRONE_ID), mocks.baseDroneLoadResponse(IDLE));
        Mockito.verify(droneRepository, Mockito.times(1)).findById(mocks.DRONE_ID);
        Mockito.verify(droneLoadRepository, Mockito.times(1)).findByDroneAndEndTimeNull(mocks.baseDrone(RETURNING));
    }

    @Test
    void idleDroneException() {
        Mockito.when(droneRepository.findById(mocks.DRONE_ID)).thenReturn(Optional.ofNullable(mocks.baseDrone(IDLE)));
        Mockito.when(droneLoadRepository.findByDroneAndEndTimeNull(mocks.baseDrone(IDLE))).thenReturn(List.of(mocks.baseDroneLoad(IDLE)));
        assertEquals(
                assertThrows(DroneGeneralException.class,
                        () -> subject.idleDrone(mocks.DRONE_ID)).getMessage(),
                mocks.baseDroneGeneralExceptionNotReturning.getMessage()
        );
        Mockito.verify(droneRepository, Mockito.times(1)).findById(mocks.DRONE_ID);
        Mockito.verify(droneLoadRepository, Mockito.times(1)).findByDroneAndEndTimeNull(mocks.baseDrone(IDLE));
    }
}