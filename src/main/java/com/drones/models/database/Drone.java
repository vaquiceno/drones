package com.drones.models.database;


import javax.persistence.Entity;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.EnumType;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;
import java.util.ArrayList;
import java.util.List;

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
    private Status status = Status.IDLE;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "drone")
    private List<DroneLoad> droneLoads;

    public Drone() {
    }

    public Drone(String serialNumber, Model model, Integer weightLimit, Integer currentBatteryCapacity) {
        this.serialNumber = serialNumber;
        this.model = model;
        this.weightLimit = weightLimit;
        this.currentBatteryCapacity = currentBatteryCapacity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Integer getWeightLimit() {
        return weightLimit;
    }

    public void setWeightLimit(Integer weightLimit) {
        this.weightLimit = weightLimit;
    }

    public Integer getCurrentBatteryCapacity() {
        return currentBatteryCapacity;
    }

    public void setCurrentBatteryCapacity(Integer currentBatteryCapacity) {
        this.currentBatteryCapacity = currentBatteryCapacity;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void addDroneLoad(DroneLoad droneLoad){
        if (null == droneLoads)
            droneLoads = new ArrayList<>();
        droneLoads.add(droneLoad);
        droneLoad.setDrone(this);
    }

    public enum Status {
        IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING
    }

    public enum Model {
        Lightweight, Middleweight, Cruiserweight, Heavyweight
    }
}
