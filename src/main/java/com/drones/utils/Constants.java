package com.drones.utils;

public final class Constants {
    public static final Integer DEFAULT_DRONE_BATTERY_CAPACITY = 100;
    public static final Integer DEFAULT_DRONE_WEIGHT_LIMIT = 500;
    public static final Integer DEFAULT_DRONE_MINIMUM_BATTERY = 25;
    public static final String ERROR_MESSAGE_WEIGHT_LIMIT = "Total amount of medication's weight is higher than Drone weight limit";
    public static final String ERROR_MESSAGE_DRONE_NOT_FOUND = "Drone with specified id does not exists";
    public static final String ERROR_MESSAGE_TOTAL_WEIGHT_MEDICATIONS = "Error getting total weight medications";
    public static final String ERROR_MESSAGE_DUPLICATED_CODES_MEDICATIONS = "There are codes duplicated in medications";
    public static final String ERROR_MESSAGE_MINIMUM_BATTERY = "Drone battery is below " + DEFAULT_DRONE_MINIMUM_BATTERY + "%";
    public static final String ERROR_MESSAGE_NOT_IDLE = "Drone Status is not IDLE, can not carry medications";
    public static final String ERROR_MESSAGE_UNFINISHED_LOAD = "Drone has a Load that is not finished yet";
}
