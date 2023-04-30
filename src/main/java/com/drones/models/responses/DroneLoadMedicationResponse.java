package com.drones.models.responses;

import java.time.LocalDateTime;

public class DroneLoadMedicationResponse {
    private String code;
    private String name;
    private Integer weight;
    private String imageUrl;
    private Integer amount;

    public DroneLoadMedicationResponse(String code,
                                       String name,
                                       Integer weight,
                                       String imageUrl,
                                       Integer amount) {
        this.code = code;
        this.name = name;
        this.weight = weight;
        this.imageUrl = imageUrl;
        this.amount = amount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
