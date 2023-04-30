package com.drones.models.requests;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class MedicationRequest {
    @NotBlank(message = "code is required")
    @Pattern(regexp = "^(?=.*[A-Z0-9])[A-Z0-9_]*$", message = "allowed only upper case letters, underscore and numbers")
    private String code;
    @NotBlank(message = "name is required")
    @Pattern(regexp = "^(?=.*[A-Za-z0-9])[A-Za-z0-9_-]*$", message = "allowed only letters, numbers, hyphens(-) and underscores(_)")
    private String name;
    @Max(value = 500, message = "Weight cannot be more than 500g")
    @Min(value = 10, message = "Weight cannot be less than 10g")
    private Integer weight;
    @Pattern(regexp = "((\\/|\\\\|\\/\\/|https?:\\\\\\\\|https?:\\/\\/)[a-z0-9_@\\-^!#$%&+={}.\\/\\\\\\[\\]]+)+\\.[a-z]+$",
            message = "imageUrl must have a valid url syntax")
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
