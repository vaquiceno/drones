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
import javax.persistence.Enumerated;
import javax.persistence.EnumType;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table( name = "Drone")
public class Drone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "serial_number")
    private String serialNumber;
    @Enumerated(EnumType.STRING)
    @Column(name = "model")
    private Model model;
    @Column(name = "weight_limit")
    private Integer weightLimit;
    @Column(name = "current_battery_capacity")
    private Integer currentBatteryCapacity;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @Builder.Default
    private Status status = Status.IDLE;

    public enum Status {
        IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING
    }

    public enum Model {
        Lightweight, Middleweight, Cruiserweight, Heavyweight
    }
}
