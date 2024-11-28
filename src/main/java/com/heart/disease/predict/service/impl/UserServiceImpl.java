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

    // Mendeklarasikan UserRepository dan BCryptPasswordEncoder sebagai dependency
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // Menggunakan @Autowired untuk menyuntikkan dependency melalui constructor
    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Users findById(Long id) {
        // Mencari user berdasarkan ID, jika tidak ditemukan maka return null
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public List<Users> findAll() {
        // Mengembalikan daftar semua pengguna yang ada di database
        return userRepository.findAll();
    }

    @Override
    public Users save(UserRegistrationDto registrationDto) {
        // Mengecek apakah ini adalah user pertama di sistem (jika jumlah user = 0)
        boolean isFirstUser = userRepository.count() == 0;

        // Menetapkan role, jika ini user pertama maka ROLE_ADMIN, jika tidak maka ROLE_USER
        Role role = isFirstUser ? new Role("ROLE_ADMIN") : new Role("ROLE_USER");

        // Membuat list roles untuk user baru
        List<Role> roles = new ArrayList<>(Collections.singletonList(role));

        // Membuat objek MyUser baru dengan data dari DTO dan role yang ditetapkan
        Users users = new Users(
                registrationDto.getFirstName(),
                registrationDto.getLastName(),
                registrationDto.getEmail(),
                passwordEncoder.encode(registrationDto.getPassword()), // Mengenkripsi password
                roles // Menetapkan roles ke user
        );

        // Menyimpan user baru ke dalam database
        return userRepository.save(users);
    }

    @GetMapping("/forms")
    public String getFormsPage() {
        // Mendapatkan informasi pengguna yang sedang login dari SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Logged in as: " + authentication.getName()); // Menampilkan nama pengguna
        System.out.println("Roles: " + authentication.getAuthorities()); // Menampilkan role pengguna
        return "forms"; // Mengarahkan ke halaman forms
    }

    @Override
    public void delete(Users users) {
        // Menghapus user dari database berdasarkan objek MyUser
        userRepository.delete(users);
    }

    @Override
    public void deleteById(Long id) {
        // Menghapus user dari database berdasarkan ID
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Mencari user berdasarkan email (digunakan sebagai username)
        Users users = userRepository.findByEmail(username);

        // Jika user tidak ditemukan, lemparkan exception dengan pesan dalam bahasa Indonesia
        if (users == null) {
            throw new UsernameNotFoundException("Pengguna tidak ditemukan dengan email: " + username);
        }

        // Mengembalikan objek UserDetails yang digunakan oleh Spring Security
        return new User(users.getEmail(), users.getPassword(), mapRolesToAuthorities(users.getRoles()));
    }

    // Method untuk memetakan role ke authorities (yang dipahami oleh Spring Security)
    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        // Mengonversi setiap role menjadi SimpleGrantedAuthority
        return roles.stream()
                .map(role -> {
                    String roleName = role.getName();
                    // Jika role sudah diawali dengan "ROLE_", tidak perlu menambahkannya lagi
                    if (roleName.startsWith("ROLE_")) {
                        return new SimpleGrantedAuthority(roleName);
                    } else {
                        // Menambahkan prefix "ROLE_" jika belum ada
                        return new SimpleGrantedAuthority("ROLE_" + roleName);
                    }
                })
                .collect(Collectors.toList());
    }
}
