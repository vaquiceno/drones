package com.drones.utils;

public final class Constants {
    public static final Integer DEFAULT_DRONE_BATTERY_CAPACITY = 100;
    public static final Integer DEFAULT_DRONE_WEIGHT_LIMIT = 500;
    public static final Integer DEFAULT_DRONE_MINIMUM_BATTERY = 25;
    public static final String ERROR_MESSAGE_WEIGHT_LIMIT = "Total amount of medication's weight is higher than Drone weight limit";
    public static final String ERROR_MESSAGE_DRONE_NOT_FOUND = "Drone with specified id does not exists";
    public static final String ERROR_MESSAGE_DUPLICATED_CODES_MEDICATIONS = "There are codes duplicated in medications";
    public static final String ERROR_MESSAGE_MINIMUM_BATTERY = "Drone battery is below " + DEFAULT_DRONE_MINIMUM_BATTERY + "%";
    public static final String ERROR_MESSAGE_NOT_IDLE = "Drone Status is not IDLE, can not set Drone to LOADING";
    public static final String ERROR_MESSAGE_NOT_LOADING = "Drone Status is not LOADING, can not set Drone to LOADED";
    public static final String ERROR_MESSAGE_NOT_LOADED = "Drone Status is not LOADED, can not set Drone to DELIVERING";
    public static final String ERROR_MESSAGE_NOT_DELIVERING = "Drone Status is not DELIVERING, can not set Drone to DELIVERED";
    public static final String ERROR_MESSAGE_NOT_DELIVERED = "Drone Status is not DELIVERED, can not set Drone to RETURNING";
    public static final String ERROR_MESSAGE_NOT_RETURNING = "Drone Status is not RETURNING, can not set Drone to IDLE";
    public static final String ERROR_MESSAGE_ONE_MORE_ACTIVE_LOADS = "Drone has one or more active Loads. Can not set drone to LOADING";
    public static final String ERROR_MESSAGE_ZERO_MORE_ONE_ACTIVE_LOADS = "Drone has zero active Loads or more than one active Loads";
}
