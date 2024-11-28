package com.heart.disease.predict.web.controller;


import com.heart.disease.predict.model.PatientsData;
import com.heart.disease.predict.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/heart")
public class HeartPredictionController {

    // URL dari Flask API untuk prediksi penyakit jantung
    private static final String FLASK_API_URL = "http://127.0.0.1:5000/predict";

    // Menggunakan autowired untuk menyuntikkan PatientsRepository
    @Autowired
    private PatientRepository patientRepository;

    // Endpoint untuk melakukan prediksi risiko penyakit jantung
    @PostMapping("/predict")
    public ResponseEntity<Map<String, Object>> predictHeartRisk(@RequestBody PatientsData inputData) {
        // Inisialisasi Map untuk menyimpan respons API
        Map<String, Object> responseMap = new HashMap<>();
        try {
            // Membuat RestTemplate untuk mengirim permintaan ke Flask API
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Menyiapkan data dari inputData ke dalam format Map untuk dikirim sebagai JSON
            Map<String, Object> requestData = new HashMap<>();
            requestData.put("name", inputData.getName());
            requestData.put("age", inputData.getAge());
            requestData.put("sex", inputData.getSex());
            requestData.put("cp", inputData.getCp());
            requestData.put("trestbps", inputData.getTrestbps());
            requestData.put("chol", inputData.getChol());
            requestData.put("fbs", inputData.getFbs());
            requestData.put("restecg", inputData.getRestecg());
            requestData.put("thalach", inputData.getThalach());
            requestData.put("exang", inputData.getExang());
            requestData.put("oldpeak", inputData.getOldpeak());
            requestData.put("slope", inputData.getSlope());
            requestData.put("ca", inputData.getCa());
            requestData.put("thal", inputData.getThal());

            // Mengirim data ke Flask API dan menerima respons
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestData, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(FLASK_API_URL, request, Map.class);

            // Memeriksa apakah respons dari Flask API berhasil
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                // Mengambil body dari respons
                Map<String, Object> responseBody = response.getBody();
                Map<String, Double> probability = (Map<String, Double>) responseBody.get("probability");

                // Memperbarui data pasien dengan hasil prediksi dari Flask API
                inputData.setRisk((String) responseBody.get("risk"));
                inputData.setAtRiskProbability(probability != null ? probability.get("at_risk").floatValue() : 0.0f);
                inputData.setNoRiskProbability(probability != null ? probability.get("no_risk").floatValue() : 0.0f);

                // Menyimpan data pasien yang telah diperbarui ke database
                patientRepository.save(inputData);

                // Mempersiapkan respons yang akan dikembalikan ke klien
                responseMap.put("message", "Prediksi dan data berhasil disimpan!");
                responseMap.put("data", responseBody);
                return ResponseEntity.ok(responseMap);
            } else {
                // Jika respons dari Flask API tidak valid
                responseMap.put("error", "Respons dari Flask API tidak valid atau terjadi kesalahan.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMap);
            }
        } catch (Exception e) {
            // Menangani exception dan mengembalikan pesan error ke klien
            responseMap.put("error", "Terjadi kesalahan: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMap);
        }
    }

    @GetMapping("/patients")
    public ResponseEntity<List<PatientsData>> getAllPatients() {
        try {
            List<PatientsData> patients = patientRepository.findAll();
            if (patients.isEmpty()) {
                System.out.println("Tidak ada data pasien.");
            } else {
                System.out.println("Jumlah pasien: " + patients.size());
            }
            return ResponseEntity.ok(patients);
        } catch (Exception e) {
            e.printStackTrace();  // Tambahkan logging error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Endpoint untuk mengambil data statistik chart
    @GetMapping("/stats")
    public ResponseEntity<Object> getPatientStats() {
        try {
            // Ambil semua data pasien
            List<PatientsData> patients = patientRepository.findAll();

            // Hitung jumlah pasien yang berisiko dan tidak berisiko
            long atRiskCount = patients.stream().filter(p -> "at risk".equals(p.getRisk())).count();
            long noRiskCount = patients.stream().filter(p -> "no risk".equals(p.getRisk())).count();

            // Persiapkan data untuk chart
            int totalPatients = patients.size();
            double atRiskPercentage = ((double) atRiskCount / totalPatients) * 100;
            double noRiskPercentage = ((double) noRiskCount / totalPatients) * 100;

            // Kirimkan data untuk chart
            return ResponseEntity.ok(new Object[]{
                    new Object[]{"At Risk", atRiskPercentage},
                    new Object[]{"No Risk", noRiskPercentage}
            });

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving data.");
        }
    }


}