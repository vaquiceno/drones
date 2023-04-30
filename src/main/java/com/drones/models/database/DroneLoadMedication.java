package com.drones.models.database;


import com.drones.models.ids.DroneLoadMedicationId;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.IdClass;
import java.io.Serializable;

@Entity
@IdClass(DroneLoadMedicationId.class)
@Table( name = "Drone_Load_Medication")
public class DroneLoadMedication implements Serializable {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="drone_load_id", nullable=false)
    private DroneLoad droneLoad;
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="medication_code", nullable=false)
    private Medication medication;
    @Column(name = "amount")
    private Integer amount;

    public DroneLoadMedication() {
    }

    public DroneLoadMedication(DroneLoad droneLoad, Medication medication, Integer amount) {
        this.droneLoad = droneLoad;
        this.medication = medication;
        this.amount = amount;
    }

    public DroneLoad getDroneLoad() {
        return droneLoad;
    }

    public void setDroneLoad(DroneLoad droneLoad) {
        this.droneLoad = droneLoad;
    }

    public Medication getMedication() {
        return medication;
    }

    public void setMedication(Medication medication) {
        this.medication = medication;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
