# Drones API REST

## Technical considerations
* postgresql H2 database used
* SpringBoot framework used 
* I used a three layer design: controllers, services and repositories
* For Exception handling, I created a customExceptionHandler, which allows to retrieve Exceptions from validations as valid Json Responses 

## Running the project
* Use java 11
* execute run task from gradle (application/bootRun)

## Here are some assumptions for the design approach
* serial number for drones are not unique
* Drones should have a minimum weight limit of 100gr
* Only Drones with status IDLE can be loaded with medications
* images are stored as URL for medications, and can be null
* Medications have unique code (Primary key) and are stored in database when loading Drones method is executed
* if a medication already exists and new medication with same code but different properties(i.e. name, weight) is loaded, the medication is replaced in database
* Medication Weight cannot be more than 500g
* Medication Weight cannot be less than 10g
