package com.heart.disease.predict.web.controller;

import com.heart.disease.predict.model.Role;
import com.heart.disease.predict.service.impl.UserServiceImpl;
import com.heart.disease.predict.web.dto.UserRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("api/v1/registration")
public class UserRegistrationController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping
    public String getRegistrationPage(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "registration";
    }

    @PostMapping
    public String registerUser(UserRegistrationDto registrationDto) {
        // Menambahkan role 'ROLE_USER' pada saat registrasi
        registrationDto.setRoles(List.of(new Role("ROLE_USER"))); // Menetapkan role USER
        userService.save(registrationDto); // Menyimpan pengguna dengan role yang ditetapkan
        return "redirect:/api/v1/registration?success";
    }

}
