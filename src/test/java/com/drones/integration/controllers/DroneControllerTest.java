package com.drones.integration.controllers;


import com.drones.DronesApplication;
import com.drones.mappers.DroneMapper;
import com.drones.models.database.Drone;
import com.drones.models.database.DroneLoad;
import com.drones.models.database.DroneLoadMedication;
import com.drones.models.database.Medication;
import com.drones.models.requests.DroneLoadMedicationsRequest;
import com.drones.models.requests.MedicationRequest;
import com.drones.models.responses.DroneLoadMedicationResponse;
import com.drones.models.responses.DroneLoadResponse;
import com.drones.models.responses.DroneResponse;
import com.drones.repositories.DroneLoadRepository;
import com.drones.repositories.DroneRepository;
import com.drones.repositories.MedicationRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static com.drones.Mocks.DRONE_ID;
import static com.drones.Mocks.DRONE_LOAD_END_TIME;
import static com.drones.Mocks.DRONE_LOAD_START_TIME;
import static com.drones.Mocks.baseDroneIdNull;
import static com.drones.Mocks.baseDroneLoadMedicationsNull;
import static com.drones.Mocks.baseDroneLoadMedicationsRequest;
import static com.drones.Mocks.baseDroneLoadResponse;
import static com.drones.Mocks.baseDroneRequest;
import static com.drones.Mocks.baseDroneResponse;
import static com.drones.Mocks.baseMedication;
import static com.drones.Mocks.baseMedicationRequest;
import static com.drones.models.database.Drone.Model.Lightweight;
import static com.drones.models.database.Drone.Model.Middleweight;
import static com.drones.models.database.Drone.Status.DELIVERED;
import static com.drones.models.database.Drone.Status.DELIVERING;
import static com.drones.models.database.Drone.Status.IDLE;
import static com.drones.models.database.Drone.Status.LOADED;
import static com.drones.models.database.Drone.Status.LOADING;
import static com.drones.models.database.Drone.Status.RETURNING;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureMockMvc
@SpringBootTest(classes = DronesApplication.class)
class DroneControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private DroneRepository droneRepository;
    @Autowired
    private MedicationRepository medicationRepository;
    @Autowired
    private DroneLoadRepository droneLoadRepository;
    @Autowired
    private DroneMapper droneMapper;

    public static final String ENDPOINT_BASE = "/api/drones/";

    @BeforeEach
    void beforeEach(){


    }

    @Test
    void getAllDrones() throws Exception {
        String result = mockMvc.perform(
                MockMvcRequestBuilders
                        .get(ENDPOINT_BASE+"/all"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<DroneResponse> droneResponses = objectMapper.readValue(result, new TypeReference<List<DroneResponse>>(){});
        assertEquals(droneResponses, List.of(
                baseDroneResponse(1, "sn1", Lightweight.toString(), 500, 100, IDLE.toString()),
                baseDroneResponse(2, "sn2", Middleweight.toString(), 500, 50, IDLE.toString())
        ));
    }

    @Test
    void getAvailableDrones() throws Exception {
        String result = mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(ENDPOINT_BASE+"/available"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<DroneResponse> droneResponses = objectMapper.readValue(result, new TypeReference<List<DroneResponse>>(){});
        assertEquals(droneResponses, List.of(
                baseDroneResponse(1, "sn1", Lightweight.toString(), 500, 100, IDLE.toString()),
                baseDroneResponse(2, "sn2", Middleweight.toString(), 500, 50, IDLE.toString())
        ));
    }

    @Test
    void getDrone() throws Exception {
        String result = mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(ENDPOINT_BASE+"/request/"+DRONE_ID))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        DroneResponse droneResponse = objectMapper.readValue(result, DroneResponse.class);
        assertEquals(
                droneResponse,
                baseDroneResponse(1, "sn1", Lightweight.toString(), 500, 100, IDLE.toString())
        );
    }

    @Test
    void getLoadedMedications() throws Exception {
        droneLoadRepository.save(baseDroneLoadMedicationsNull());
        String result = mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(ENDPOINT_BASE+"/loaded_medications/"+DRONE_ID))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        DroneLoadResponse droneLoadResponse = objectMapper.readValue(result, DroneLoadResponse.class);
        assertEquals(
                droneLoadResponse,
                baseDroneLoadResponse(IDLE, List.of())
        );
    }

    @Test
    void registerDrone() throws Exception {
        Integer newDroneId = 3;
        String result = mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(ENDPOINT_BASE+"/register/")
                                .content(objectMapper.writeValueAsString(baseDroneRequest()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        DroneResponse droneResponse = objectMapper.readValue(result, DroneResponse.class);
        assertEquals(
                droneResponse.getDroneId(),
                newDroneId
        );
    }

    @Test
    public void loadingDrone() throws Exception {
        droneRepository.save(baseDroneIdNull);
        MedicationRequest medicationRequest1 = baseMedicationRequest(
                "6985",
                "Bread",
                10,
                "https://regex101.com//img.das",
                2);
        MedicationRequest medicationRequest2 = baseMedicationRequest(
                "69852",
                "Vine",
                10,
                null,
                3);
        DroneLoadMedicationsRequest droneLoadMedicationsRequest = baseDroneLoadMedicationsRequest(
                List.of(medicationRequest1, medicationRequest2)
        );
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
                        .weight(10)
                        .amount(3)
                        .build()
        );
        String result = mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(ENDPOINT_BASE+"/loading/")
                                .content(objectMapper.writeValueAsString(droneLoadMedicationsRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        DroneLoadResponse droneLoadResponse = objectMapper.readValue(result, DroneLoadResponse.class);
        assertNotNull(droneLoadResponse.getStartTime());
        droneLoadResponse.setStartTime(DRONE_LOAD_START_TIME);
        assertEquals(
                droneLoadResponse,
                baseDroneLoadResponse(LOADING, droneLoadMedicationResponses)
        );
    }

    @Test
    void loadedDrone() throws Exception {
        Medication medication = baseMedication(
                "6985",
                "Bread",
                10,
                "https://regex101.com//img.das");
        medicationRepository.save(medication);
        Drone drone = droneRepository.save(baseDroneIdNull);
        DroneLoad droneLoad = droneMapper.toDroneLoad(drone, LocalDateTime.now());
        droneLoad = droneLoadRepository.save(droneLoad);
        // save Drone Load Medication
        DroneLoad finalDroneLoad = droneLoad;
        finalDroneLoad.addDroneLoadMedication(
                DroneLoadMedication
                        .builder()
                        .droneLoad(finalDroneLoad)
                        .medication(medication)
                        .amount(2)
                        .build()
        );
        // set drone Status to LOADING
        drone.setStatus(LOADING);
        droneLoadRepository.save(finalDroneLoad);
        droneRepository.save(drone);
        Integer droneId = drone.getId();
        List<DroneLoadMedicationResponse> droneLoadMedicationResponses = List.of(
                DroneLoadMedicationResponse
                        .builder()
                        .code("6985")
                        .name("Bread")
                        .weight(10)
                        .imageUrl("https://regex101.com//img.das")
                        .amount(2)
                        .build()
        );

        String result = mockMvc.perform(
                        MockMvcRequestBuilders
                                .put(ENDPOINT_BASE+"/loaded/"+droneId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        DroneLoadResponse droneLoadResponse = objectMapper.readValue(result, DroneLoadResponse.class);
        assertNotNull(droneLoadResponse.getStartTime());
        droneLoadResponse.setStartTime(DRONE_LOAD_START_TIME);
        assertEquals(
                droneLoadResponse,
                baseDroneLoadResponse(droneId, LOADED, droneLoadMedicationResponses)
        );
    }

    @Test
    void deliveringDrone() throws Exception{
        Medication medication = baseMedication(
                "6985",
                "Bread",
                10,
                "https://regex101.com//img.das");
        medicationRepository.save(medication);
        Drone drone = droneRepository.save(baseDroneIdNull);
        DroneLoad droneLoad = droneMapper.toDroneLoad(drone, LocalDateTime.now());
        droneLoad = droneLoadRepository.save(droneLoad);
        // save Drone Load Medication
        DroneLoad finalDroneLoad = droneLoad;
        finalDroneLoad.addDroneLoadMedication(
                DroneLoadMedication
                        .builder()
                        .droneLoad(finalDroneLoad)
                        .medication(medication)
                        .amount(2)
                        .build()
        );
        // set drone Status to LOADED
        drone.setStatus(LOADED);
        droneLoadRepository.save(finalDroneLoad);
        droneRepository.save(drone);
        Integer droneId = drone.getId();
        List<DroneLoadMedicationResponse> droneLoadMedicationResponses = List.of(
                DroneLoadMedicationResponse
                        .builder()
                        .code("6985")
                        .name("Bread")
                        .weight(10)
                        .imageUrl("https://regex101.com//img.das")
                        .amount(2)
                        .build()
        );

        String result = mockMvc.perform(
                        MockMvcRequestBuilders
                                .put(ENDPOINT_BASE+"/delivering/"+droneId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        DroneLoadResponse droneLoadResponse = objectMapper.readValue(result, DroneLoadResponse.class);
        assertNotNull(droneLoadResponse.getStartTime());
        droneLoadResponse.setStartTime(DRONE_LOAD_START_TIME);
        assertEquals(
                droneLoadResponse,
                baseDroneLoadResponse(droneId, DELIVERING, droneLoadMedicationResponses)
        );
    }

    @Test
    void deliveredDrone() throws Exception{
        Medication medication = baseMedication(
                "6985",
                "Bread",
                10,
                "https://regex101.com//img.das");
        medicationRepository.save(medication);
        Drone drone = droneRepository.save(baseDroneIdNull);
        DroneLoad droneLoad = droneMapper.toDroneLoad(drone, LocalDateTime.now());
        droneLoad = droneLoadRepository.save(droneLoad);
        // save Drone Load Medication
        DroneLoad finalDroneLoad = droneLoad;
        finalDroneLoad.addDroneLoadMedication(
                DroneLoadMedication
                        .builder()
                        .droneLoad(finalDroneLoad)
                        .medication(medication)
                        .amount(2)
                        .build()
        );
        // set drone Status to DELIVERING
        drone.setStatus(DELIVERING);
        droneLoadRepository.save(finalDroneLoad);
        droneRepository.save(drone);
        Integer droneId = drone.getId();
        List<DroneLoadMedicationResponse> droneLoadMedicationResponses = List.of(
                DroneLoadMedicationResponse
                        .builder()
                        .code("6985")
                        .name("Bread")
                        .weight(10)
                        .imageUrl("https://regex101.com//img.das")
                        .amount(2)
                        .build()
        );

        String result = mockMvc.perform(
                        MockMvcRequestBuilders
                                .put(ENDPOINT_BASE+"/delivered/"+droneId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        DroneLoadResponse droneLoadResponse = objectMapper.readValue(result, DroneLoadResponse.class);
        assertNotNull(droneLoadResponse.getStartTime());
        droneLoadResponse.setStartTime(DRONE_LOAD_START_TIME);
        assertEquals(
                droneLoadResponse,
                baseDroneLoadResponse(droneId, DELIVERED, droneLoadMedicationResponses)
        );
    }

    @Test
    void returningDrone() throws Exception{
        Medication medication = baseMedication(
                "6985",
                "Bread",
                10,
                "https://regex101.com//img.das");
        medicationRepository.save(medication);
        Drone drone = droneRepository.save(baseDroneIdNull);
        DroneLoad droneLoad = droneMapper.toDroneLoad(drone, LocalDateTime.now());
        droneLoad = droneLoadRepository.save(droneLoad);
        // save Drone Load Medication
        DroneLoad finalDroneLoad = droneLoad;
        finalDroneLoad.addDroneLoadMedication(
                DroneLoadMedication
                        .builder()
                        .droneLoad(finalDroneLoad)
                        .medication(medication)
                        .amount(2)
                        .build()
        );
        // set drone Status to DELIVERED
        drone.setStatus(DELIVERED);
        droneLoadRepository.save(finalDroneLoad);
        droneRepository.save(drone);
        Integer droneId = drone.getId();
        List<DroneLoadMedicationResponse> droneLoadMedicationResponses = List.of(
                DroneLoadMedicationResponse
                        .builder()
                        .code("6985")
                        .name("Bread")
                        .weight(10)
                        .imageUrl("https://regex101.com//img.das")
                        .amount(2)
                        .build()
        );

        String result = mockMvc.perform(
                        MockMvcRequestBuilders
                                .put(ENDPOINT_BASE+"/returning/"+droneId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        DroneLoadResponse droneLoadResponse = objectMapper.readValue(result, DroneLoadResponse.class);
        assertNotNull(droneLoadResponse.getStartTime());
        droneLoadResponse.setStartTime(DRONE_LOAD_START_TIME);
        assertEquals(
                droneLoadResponse,
                baseDroneLoadResponse(droneId, RETURNING, droneLoadMedicationResponses)
        );
    }

    @Test
    void idleDrone() throws Exception{
        Medication medication = baseMedication(
                "6985",
                "Bread",
                10,
                "https://regex101.com//img.das");
        medicationRepository.save(medication);
        Drone drone = droneRepository.save(baseDroneIdNull);
        DroneLoad droneLoad = droneMapper.toDroneLoad(drone, LocalDateTime.now());
        droneLoad = droneLoadRepository.save(droneLoad);
        // save Drone Load Medication
        DroneLoad finalDroneLoad = droneLoad;
        finalDroneLoad.addDroneLoadMedication(
                DroneLoadMedication
                        .builder()
                        .droneLoad(finalDroneLoad)
                        .medication(medication)
                        .amount(2)
                        .build()
        );
        // set drone Status to RETURNING
        drone.setStatus(RETURNING);
        droneLoadRepository.save(finalDroneLoad);
        droneRepository.save(drone);
        Integer droneId = drone.getId();
        List<DroneLoadMedicationResponse> droneLoadMedicationResponses = List.of(
                DroneLoadMedicationResponse
                        .builder()
                        .code("6985")
                        .name("Bread")
                        .weight(10)
                        .imageUrl("https://regex101.com//img.das")
                        .amount(2)
                        .build()
        );

        String result = mockMvc.perform(
                        MockMvcRequestBuilders
                                .put(ENDPOINT_BASE+"/idle/"+droneId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        DroneLoadResponse droneLoadResponse = objectMapper.readValue(result, DroneLoadResponse.class);
        assertNotNull(droneLoadResponse.getStartTime());
        assertNotNull(droneLoadResponse.getEndTime());
        droneLoadResponse.setStartTime(DRONE_LOAD_START_TIME);
        droneLoadResponse.setEndTime(DRONE_LOAD_END_TIME);
        assertEquals(
                droneLoadResponse,
                baseDroneLoadResponse(droneId, IDLE, droneLoadMedicationResponses, DRONE_LOAD_END_TIME)
        );
    }
}