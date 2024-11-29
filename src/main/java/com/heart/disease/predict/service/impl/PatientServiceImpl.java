package com.heart.disease.predict.service.impl;

import com.heart.disease.predict.model.PatientsData;
import com.heart.disease.predict.repository.PatientRepository;
import com.heart.disease.predict.service.PatientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class PatientServiceImpl implements PatientService {

    // Inisialisasi logger untuk mencatat log
    private static final Logger logger = LoggerFactory.getLogger(PatientServiceImpl.class);

    // Autowire repository pasien untuk operasi database
    @Autowired
    private PatientRepository patientRepository;

    @Override
    public PatientsData savePatientData(PatientsData patientData) {
        // URL API Flask yang digunakan untuk prediksi
        String apiUrl = "http://localhost:5000/predict";
        RestTemplate restTemplate = new RestTemplate();

        try {
            // Kirim data pasien ke API Flask menggunakan metode POST dan dapatkan respons
            Map<String, Object> response = restTemplate.postForObject(apiUrl, patientData, Map.class);
            logger.info("Respons diterima dari Flask API: {}", response);

            // Cek apakah respons dari API valid dan memiliki struktur yang benar
            if (response != null && response.containsKey("probability") && response.containsKey("risk")) {
                // Ambil data probabilitas dan risiko dari respons API
                Map<String, Double> probability = (Map<String, Double>) response.get("probability");
                patientData.setRisk((String) response.get("risk"));
                patientData.setAtRiskProbability(probability.get("at_risk").floatValue());
                patientData.setNoRiskProbability(probability.get("no_risk").floatValue());
            } else {
                // Jika respons tidak valid, catat error dan lempar exception
                logger.error("Struktur respons dari API tidak valid.");
                throw new RuntimeException("Respons dari API prediksi tidak valid.");
            }

            // Simpan data pasien yang sudah diprediksi ke dalam database
            return patientRepository.save(patientData);

        } catch (HttpClientErrorException e) {
            // Tangani kesalahan yang terjadi saat komunikasi dengan API Flask
            logger.error("Terjadi kesalahan saat berkomunikasi dengan API prediksi: {}", e.getMessage());
            throw new RuntimeException("Terjadi kesalahan saat berkomunikasi dengan API prediksi: " + e.getMessage());
        } catch (Exception e) {
            // Tangani semua jenis kesalahan lainnya yang mungkin terjadi
            logger.error("Terjadi kesalahan saat memproses data pasien: {}", e.getMessage());
            throw new RuntimeException("Terjadi kesalahan saat memproses data pasien: " + e.getMessage());
        }
    }
}
