DROP TABLE IF EXISTS Drone;
DROP TABLE IF EXISTS Medication;

CREATE TABLE Drone (
    id int AUTO_INCREMENT,
    serial_number varchar(100) not null,
    model varchar(50) not null,
    current_battery_capacity int not null default 100,
    status varchar(50) not null default 'IDLE',
    PRIMARY KEY (id)
);

CREATE TABLE Medication (
    id int AUTO_INCREMENT,
    name varchar(255) not null,
    weight int not null,
    code varchar(255) not null,
    image_url varchar(255),
    PRIMARY KEY (id)
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
    medication_id int,
    amount int,
    PRIMARY KEY (drone_load_id, medication_id),
    CONSTRAINT fk_Drone_Load_Medication_Drone_Load
              FOREIGN KEY(drone_load_id)
        	  REFERENCES Drone_Load(id),
    CONSTRAINT fk_Drone_Load_Medication_Medication
              FOREIGN KEY(medication_id)
        	  REFERENCES Medication(id)
);

INSERT INTO Drone (serial_number, model, current_battery_capacity, status) values ('KSDFMFOSDFOSDFP', 'M1', 100, 'IDLE');
INSERT INTO Drone (serial_number, model, current_battery_capacity, status) values ('dfvhhjmnghn', 'M2', 50, 'IDLE');

INSERT INTO Medication (name, weight, code, image_url) values ('milk', 100, '5k_hg', 'url/1.png');
INSERT INTO Medication (name, weight, code, image_url) values ('apple', 20, 'gdfg14_hg', null);
INSERT INTO Medication (name, weight, code, image_url) values ('coffee', 100, '5k_hggg', 'url/2.jpg');
INSERT INTO Medication (name, weight, code, image_url) values ('sugar', 20, 'gdfg14hj_hg', null);