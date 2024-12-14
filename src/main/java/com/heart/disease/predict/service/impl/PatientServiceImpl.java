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

    // Logger untuk mencatat aktivitas di dalam class ini
    private static final Logger logger = LoggerFactory.getLogger(PatientServiceImpl.class);

    // Autowire repository untuk operasi database terkait data pasien
    @Autowired
    private PatientRepository patientRepository;

    @Override
    public PatientsData savePatientData(PatientsData patientData) {
        // URL endpoint API Flask yang digunakan untuk memprediksi risiko penyakit
        String apiUrl = "http://localhost:5000/predict";
        RestTemplate restTemplate = new RestTemplate();

        try {
            // Mengirim data pasien ke API Flask menggunakan metode POST dan menerima respons
            Map<String, Object> response = restTemplate.postForObject(apiUrl, patientData, Map.class);
            logger.info("Respons diterima dari Flask API: {}", response);

            // Validasi apakah respons dari API tidak null dan memiliki kunci yang diharapkan
            if (response != null && response.containsKey("probability") && response.containsKey("risk")) {
                // Mengambil data probabilitas dan risiko dari respons API
                Map<String, Double> probability = (Map<String, Double>) response.get("probability");
                patientData.setRisk((String) response.get("risk"));
                patientData.setAtRiskProbability(probability.get("at_risk").floatValue());
                patientData.setNoRiskProbability(probability.get("no_risk").floatValue());
            } else {
                // Jika struktur respons tidak valid, log kesalahan dan lempar pengecualian
                logger.error("Struktur respons dari API tidak valid.");
                throw new RuntimeException("Respons dari API prediksi tidak valid.");
            }

            // Menyimpan data pasien yang sudah diperbarui ke dalam database
            return patientRepository.save(patientData);

        } catch (HttpClientErrorException e) {
            // Menangani kesalahan komunikasi dengan API Flask (misalnya, kesalahan 404, 500, dll.)
            logger.error("Terjadi kesalahan saat berkomunikasi dengan API prediksi: {}", e.getMessage());
            throw new RuntimeException("Terjadi kesalahan saat berkomunikasi dengan API prediksi: " + e.getMessage());
        } catch (Exception e) {
            // Menangani kesalahan umum lainnya (misalnya, kesalahan parsing atau kesalahan runtime)
            logger.error("Terjadi kesalahan saat memproses data pasien: {}", e.getMessage());
            throw new RuntimeException("Terjadi kesalahan saat memproses data pasien: " + e.getMessage());
        }
    }
}
