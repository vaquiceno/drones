# Drones API REST

## Technical considerations
* postgresql H2 database used
* Springboot framework used 

## Running the project
* Use java 11
* execute run task from gradle (application/bootRun)

## Here are some assumptions for the design approach
* serial number for drones are not unique
* Drones should have a minimum weight limit of 100gr
* images are stored as URL for medications, and can be null
