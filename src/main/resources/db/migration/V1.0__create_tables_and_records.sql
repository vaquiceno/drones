DROP TABLE IF EXISTS Drone;
DROP TABLE IF EXISTS Medication;

CREATE TABLE Drone (
    id int AUTO_INCREMENT,
    serial_number varchar(100) not null,
    model varchar(50) not null,
    weight_limit int default 500,
    current_battery_capacity int default 100,
    status varchar(50) default 'IDLE',
    PRIMARY KEY (id)
);

CREATE TABLE Medication (
    code varchar(255) not null,
    name varchar(255) not null,
    weight int not null,
    image_url varchar(255),
    PRIMARY KEY (code)
);

CREATE TABLE Drone_Load (
    id int AUTO_INCREMENT,
    drone_id int not null,
    start_time timestamp not null default current_timestamp,
    end_time timestamp,
    PRIMARY KEY (id),
    CONSTRAINT fk_Drone_Load_Drone
          FOREIGN KEY(drone_id)
    	  REFERENCES Drone(id)
);

CREATE TABLE Drone_Load_Medication (
    drone_load_id int,
    medication_code int,
    amount int,
    PRIMARY KEY (drone_load_id, medication_code),
    CONSTRAINT fk_Drone_Load_Medication_Drone_Load
              FOREIGN KEY(drone_load_id)
        	  REFERENCES Drone_Load(id),
    CONSTRAINT fk_Drone_Load_Medication_Medication
              FOREIGN KEY(medication_code)
        	  REFERENCES Medication(code)
);

INSERT INTO Drone (serial_number, model) values ('KSDFMFOSDFOSDFP', 'Lightweight');
INSERT INTO Drone (serial_number, model, current_battery_capacity) values ('dfvhhjmnghn', 'Middleweight', 50);