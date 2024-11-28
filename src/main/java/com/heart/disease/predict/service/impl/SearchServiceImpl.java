package com.heart.disease.predict.service.impl;

import com.heart.disease.predict.model.PatientsData;
import com.heart.disease.predict.repository.PatientRepository;
import com.heart.disease.predict.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    private final PatientRepository patientRepository;

    @Autowired
    public SearchServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public List<PatientsData> getAllPatients() {
        return patientRepository.findAll();
    }

    @Override
    public List<PatientsData> searchPatientsByName(String name) {
        return patientRepository.findByNameContainingIgnoreCase(name);
    }
}
