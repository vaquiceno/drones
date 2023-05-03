package com.drones.models.database;


import com.drones.models.ids.DroneLoadMedicationId;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.IdClass;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@IdClass(DroneLoadMedicationId.class)
@Table( name = "Drone_Load_Medication")
public class DroneLoadMedication implements Serializable {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="drone_load_id", nullable=false)
    @ToString.Exclude
    private DroneLoad droneLoad;
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="medication_code", nullable=false)
    private Medication medication;
    @Column(name = "amount")
    private Integer amount;
}
