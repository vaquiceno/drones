# Drones API REST

## Technical considerations
* postgresql H2 database used
* SpringBoot framework used 
* I used a three layer design: controllers, services and repositories
* For Exception handling, I created a customExceptionHandler, which allows to retrieve Exceptions from validations as valid Json Responses 

## Running the project
* Use java 11
* execute run task from gradle (application/bootRun)

## Database Design
I created these tables:
* **Drone**
* **Medication**
* **Drone_Load**: When a Drone is loaded with medications, a new DroneLoad is created.
A DroneLoad has two columns, start_time(will be set at the moment the load created),
and end_time(null until the load is completed, when Drone returns to IDLE status, after RETURNING)
* **Drone_Load_Medication**: It has all medications for a particular DroneLoad and respective amount of them.

## Here are some assumptions for the design approach
* serial number for drones are not unique
* Drones should have a minimum weight limit of 100gr
* Only Drones with status IDLE can be loaded with medications
* Drone can not be LOADING, DELIVERING or RETURNING if the battery level is below 25%
* only way Drone return to IDLE status is after RETURNING is finished
* images are stored as URL for medications, and can be null
* Medications have unique code (Primary key) and are stored in database when loading Drones method is executed
* if a medication already exists and new medication with same code but different properties(i.e. name, weight) is loaded, the medication is replaced in database
* Medication Weight cannot be more than 500g
* Medication Weight cannot be less than 10g
* The required periodic task only checks the battery levels of all drones and print a Json array using log4j, every 10 seconds

## Endpoints
Here you have a description of all endpoints. please Refer to the [postman](postman) collection to testing this REST API.
```json
[
    {
        "name": "Get all Drones",
        "request": {
            "url": "http://localhost:8080/api/drones/all",
            "method": "GET",
            "description": "Get a list with all Drones"
        }
    },
    {
        "name": "Get Available Drones",
        "request": {
            "url": "http://localhost:8080/api/drones/available",
            "method": "GET",
            "description": "Get a list with Available Drones (IDLE status)"
        }
    },
    {
        "name": "get particular Drone",
        "request": {
            "url": "http://localhost:8080/api/drones/request/2",
            "method": "GET",
            "description": "get particular Drone using drone id"
        }
    },
    {
        "name": "Loaded medications for a drone",
        "request": {
            "url": "http://localhost:8080/api/drones/loaded_medications/2",
            "method": "GET",
            "header": [],
            "body": {},
            "description": "Get Loaded medications for a drone, using drone id"
        }
    },
    {
        "name": "Register Drone",
        "request": {
            "url": "http://localhost:8080/api/drones/register",
            "method": "POST",
            "description": "Register a new Drone"
        }
    },
    {
        "name": "Loading Drone",
        "request": {
            "url": "http://localhost:8080/api/drones/loading",
            "method": "POST",
            "description": "Move Drone from IDLE to LOADING status. load all medications and set start time to current time"
        }
    },
    {
        "name": "Loaded Drone",
        "request": {
            "url": "http://localhost:8080/api/drones/loaded/1",
            "method": "PUT",
            "description": "Move Drone from LOADING to LOADED status."
        }
    },
    {
        "name": "Delivering Drone",
        "request": {
            "url": "http://localhost:8080/api/drones/delivering/1",
            "method": "PUT",
            "description": "Move Drone from LOADED to DELIVERING status."
        }
    },
    {
        "name": "Delivered Drone",
        "request": {
            "url": "http://localhost:8080/api/drones/delivered/1",
            "method": "PUT",
            "description": "Move Drone from DELIVERING to DELIVERED status."
        }
    },
    {
        "name": "Returning Drone",
        "request": {
            "url": "http://localhost:8080/api/drones/returning/1",
            "method": "PUT",
            "description": "Move Drone from DELIVERED to RETURNING status."
        }
    },
    {
        "name": "Idle Drone",
        "request": {
            "url": "http://localhost:8080/api/drones/idle/1",
            "method": "PUT",
            "description": "Move Drone from RETURNING to IDLE status. Set end time for the current Drone Load."
        }
    }
]
```
