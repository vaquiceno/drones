package com.drones.utils;

import com.drones.mappers.DroneMapper;
import com.drones.models.Exceptions.DroneGeneralException;
import com.drones.models.responses.ErrorResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;


@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class CustomExceptionHandler {
    private final DroneMapper droneMapper;

    public CustomExceptionHandler(DroneMapper droneMapper) {
        this.droneMapper = droneMapper;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DroneGeneralException.class)
    public ResponseEntity<ErrorResponse> handleDroneGeneralException(DroneGeneralException ex){
        return new ResponseEntity<>(droneMapper.toErrorResponse(ex), HttpStatus.BAD_REQUEST);
    }
}
