package com.drones.jobs;

import com.drones.services.DroneService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DroneJob {
    private final DroneService droneService;

    public DroneJob(DroneService droneService) {
        this.droneService = droneService;
    }

    @Scheduled(fixedDelay = 10000)
    public void scheduleFixedRateTask() {
        droneService.auditDrones();
    }
}
