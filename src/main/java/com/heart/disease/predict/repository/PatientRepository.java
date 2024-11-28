package com.heart.disease.predict.repository;

import com.heart.disease.predict.model.PatientsData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<PatientsData, Long> {
    // Metode untuk mencari pasien berdasarkan nama (case-insensitive)
    List<PatientsData> findByNameContainingIgnoreCase(String name);
}
