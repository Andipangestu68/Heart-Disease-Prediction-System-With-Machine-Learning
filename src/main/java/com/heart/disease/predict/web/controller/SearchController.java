package com.heart.disease.predict.web.controller;


import com.heart.disease.predict.model.PatientsData;
import com.heart.disease.predict.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/heart/search") // Menggunakan endpoint untuk pencarian
public class SearchController {

    private final SearchService searchService;

    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping
    public List<PatientsData> searchResults(@RequestParam(value = "name", required = false) String name) {
        if (name != null && !name.isEmpty()) {
            return searchService.searchPatientsByName(name); // Mencari data berdasarkan nama
        } else {
            return searchService.getAllPatients(); // Mengembalikan semua hasil jika tidak ada pencarian nama
        }
    }
}
