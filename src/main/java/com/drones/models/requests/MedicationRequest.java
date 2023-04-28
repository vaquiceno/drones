package com.drones.models.requests;

import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class MedicationRequest {
    @NotBlank(message = "code is required")
    @UniqueElements
    private String code;
    @NotBlank(message = "Serial number is required")
    private String name;
    @Max(value = 500, message = "Weight cannot be more than 500g")
    @Min(value = 10, message = "Weight cannot be less than 10g")
    private Integer weight;
    private String imageUrl;
    @Min(value = 1, message = "amount cannot be less than 1")
    private Integer amount;

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
