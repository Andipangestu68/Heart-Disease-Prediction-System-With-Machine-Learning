package com.heart.disease.predict.service;

import com.heart.disease.predict.model.PatientsData;

import java.util.List;

public interface SearchService {
    List<PatientsData> getAllPatients();
    List<PatientsData> searchPatientsByName(String name);
}
