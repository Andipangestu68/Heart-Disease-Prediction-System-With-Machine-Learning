package com.heart.disease.predict.service;

import com.heart.disease.predict.model.Users;
import com.heart.disease.predict.web.dto.UserRegistrationDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    Users findById(Long id);

    List<Users> findAll();

    Users save(UserRegistrationDto registrationDto);

    void delete(Users users);

    void deleteById(Long id);

}
