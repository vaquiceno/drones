package com.drones.models.database;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table( name = "Drone_Load")
public class DroneLoad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "start_time")
    private LocalDateTime startTime;
    @Column(name = "end_time")
    private LocalDateTime endTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="drone_id", nullable=false)
    private Drone drone;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "droneLoad")
    private List<DroneLoadMedication> droneLoadMedications;

    public void addDroneLoadMedication(DroneLoadMedication droneLoadMedication){
        if (null == droneLoadMedications)
            droneLoadMedications = new ArrayList<>();
        droneLoadMedications.add(droneLoadMedication);
    }
}
