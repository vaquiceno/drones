package com.drones.repositories;


import com.drones.models.database.Medication;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationRepository extends CrudRepository<Medication, String> {
}
