package com.heart.disease.predict.web.dto;


import com.heart.disease.predict.model.Role;

import java.util.List;

// DTO untuk registrasi pengguna
public class UserRegistrationDto {

    private String firstName;  // Nama depan pengguna
    private String lastName;   // Nama belakang pengguna
    private String email;      // Email pengguna
    private String password;   // Password pengguna
    private List<Role> roles;  // List untuk menyimpan role (misalnya, ROLE_USER)

    // Konstruktor default (dibutuhkan oleh Spring dan JPA)
    public UserRegistrationDto() {}

    // Getter dan Setter untuk firstName
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    // Getter dan Setter untuk lastName
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // Getter dan Setter untuk email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter dan Setter untuk password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getter dan Setter untuk roles
    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
