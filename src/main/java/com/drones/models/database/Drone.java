package com.drones.models.database;


import javax.persistence.Entity;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;

@Entity
@Table( name = "Drone")
public class Drone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "serial_number")
    private String serialNumber;
    @Column(name = "model")
    private String model;
    @Column(name = "current_battery_capacity")
    private Integer currentBatteryCapacity;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

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

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
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

    public enum Status {
        IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING
    }
}
