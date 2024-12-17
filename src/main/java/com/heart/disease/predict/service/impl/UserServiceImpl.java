package com.heart.disease.predict.service.impl;

import com.heart.disease.predict.model.Users;
import com.heart.disease.predict.model.Role;
import com.heart.disease.predict.repository.UserRepository;
import com.heart.disease.predict.service.UserService;
import com.heart.disease.predict.web.dto.UserRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository; // Dependency untuk mengelola database pengguna
    private final BCryptPasswordEncoder passwordEncoder; // Untuk mengenkripsi password pengguna

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Users findById(Long id) {
        // Mencari user berdasarkan ID; return null jika tidak ditemukan
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public List<Users> findAll() {
        // Mengambil semua data pengguna dari database
        return userRepository.findAll();
    }

    @Override
    public Users save(UserRegistrationDto registrationDto) {
        // Memeriksa apakah ini pengguna pertama di sistem
        boolean isFirstUser = userRepository.count() == 0;

        // Menetapkan role berdasarkan jumlah pengguna yang ada
        Role role = isFirstUser ? new Role("ROLE_ADMIN") : new Role("ROLE_USER");
        List<Role> roles = new ArrayList<>(Collections.singletonList(role));

        // Membuat objek pengguna baru berdasarkan data registrasi
        Users users = new Users(
                registrationDto.getFirstName(),
                registrationDto.getLastName(),
                registrationDto.getEmail(),
                passwordEncoder.encode(registrationDto.getPassword()), // Password dienkripsi
                roles
        );

        // Menyimpan pengguna baru ke database
        return userRepository.save(users);
    }

    @GetMapping("/forms")
    public String getFormsPage() {
        // Mendapatkan informasi pengguna yang sedang login
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Logged in as: " + authentication.getName()); // Nama pengguna
        System.out.println("Roles: " + authentication.getAuthorities()); // Role pengguna
        return "forms"; // Mengarahkan ke halaman forms
    }

    @Override
    public void delete(Users users) {
        // Menghapus pengguna berdasarkan objek pengguna
        userRepository.delete(users);
    }

    @Override
    public void deleteById(Long id) {
        // Menghapus pengguna berdasarkan ID
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Mencari pengguna berdasarkan email (sebagai username)
        Users users = userRepository.findByEmail(username);

        // Jika pengguna tidak ditemukan, lemparkan UsernameNotFoundException
        if (users == null) {
            throw new UsernameNotFoundException("Pengguna tidak ditemukan dengan email: " + username);
        }

        // Mengonversi pengguna ke objek UserDetails yang digunakan Spring Security
        return new User(users.getEmail(), users.getPassword(), mapRolesToAuthorities(users.getRoles()));
    }

    // Memetakan roles pengguna ke authorities yang dipahami oleh Spring Security
    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> {
                    String roleName = role.getName();
                    // Menambahkan prefix "ROLE_" jika belum ada
                    return roleName.startsWith("ROLE_")
                            ? new SimpleGrantedAuthority(roleName)
                            : new SimpleGrantedAuthority("ROLE_" + roleName);
                })
                .collect(Collectors.toList());
    }
}
