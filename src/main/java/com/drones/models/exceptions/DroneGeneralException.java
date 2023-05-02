package com.drones.models.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class DroneGeneralException extends Exception{
    public DroneGeneralException(String message) {
        super(message);
    }
}
