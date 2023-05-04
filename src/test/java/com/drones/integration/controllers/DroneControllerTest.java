package com.drones.integration.controllers;


import com.drones.DronesApplication;
import com.drones.Mocks;
import com.drones.mappers.DroneMapper;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.drones.models.database.Drone.Model.Lightweight;
import static com.drones.models.database.Drone.Model.Middleweight;
import static com.drones.models.database.Drone.Status.IDLE;
import static com.drones.models.database.Drone.Status.LOADING;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureMockMvc
@SpringBootTest(classes = DronesApplication.class)
@Transactional
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

    private Mocks mocks;

    public static final String ENDPOINT_BASE = "/api/drones/";

    @BeforeEach
    void beforeEach(){
        mocks = new Mocks();
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
                mocks.baseDroneResponse(1, "sn1", Lightweight.toString(), 500, 100, IDLE.toString()),
                mocks.baseDroneResponse(2, "sn2", Middleweight.toString(), 500, 50, IDLE.toString())
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
                mocks.baseDroneResponse(1, "sn1", Lightweight.toString(), 500, 100, IDLE.toString()),
                mocks.baseDroneResponse(2, "sn2", Middleweight.toString(), 500, 50, IDLE.toString())
        ));
    }

    @Test
    void getDrone() throws Exception {
        String result = mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(ENDPOINT_BASE+"/request/"+mocks.DRONE_ID))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        DroneResponse droneResponse = objectMapper.readValue(result, DroneResponse.class);
        assertEquals(
                droneResponse,
                mocks.baseDroneResponse(1, "sn1", Lightweight.toString(), 500, 100, IDLE.toString())
        );
    }

    @Test
    void registerDrone() throws Exception {
        Integer newDroneId = 3;
        String result = mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(ENDPOINT_BASE+"/register/")
                                .content(objectMapper.writeValueAsString(mocks.baseDroneRequest()))
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
        droneRepository.save(mocks.baseDroneIdNull);
        MedicationRequest medicationRequest1 = mocks.baseMedicationRequest(
                "6985",
                "Bread",
                10,
                "https://regex101.com//img.das",
                2);
        MedicationRequest medicationRequest2 = mocks.baseMedicationRequest(
                "69852",
                "Vine",
                10,
                null,
                3);
        DroneLoadMedicationsRequest droneLoadMedicationsRequest = mocks.baseDroneLoadMedicationsRequest(
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
        droneLoadResponse.setStartTime(mocks.DRONE_LOAD_START_TIME);
        assertEquals(
                droneLoadResponse,
                mocks.baseDroneLoadResponse(LOADING, droneLoadMedicationResponses)
        );
    }
}