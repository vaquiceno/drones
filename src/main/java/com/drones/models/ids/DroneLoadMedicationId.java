package com.drones.models.ids;

import com.drones.models.database.DroneLoad;
import com.drones.models.database.Medication;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

public class DroneLoadMedicationId implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="drone_load_id", nullable=false)
    private DroneLoad droneLoad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="medication_code", nullable=false)
    private Medication medication;
}
