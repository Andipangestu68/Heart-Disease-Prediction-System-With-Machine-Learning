package com.heart.disease.predict.model;


import jakarta.persistence.*;
import java.util.Collection;

@Entity
@Table(
        name = "app_user",
        uniqueConstraints = @UniqueConstraint(columnNames = "email") // Kolom email harus unik
)
public class Users {

    // Primary key menggunakan SEQUENCE untuk PostgreSQL
    @Id
    @SequenceGenerator(
            name = "user_seq",
            sequenceName = "user_sequence",
            allocationSize = 1 // Increment nilai sequence per langkah
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    private Long id;

    // Kolom untuk menyimpan nama depan pengguna
    @Column(name = "first_name", nullable = false)
    private String firstName;

    // Kolom untuk menyimpan nama belakang pengguna
    @Column(name = "last_name", nullable = false)
    private String lastName;

    // Kolom untuk menyimpan email (harus unik)
    @Column(nullable = false, unique = true)
    private String email;

    // Kolom untuk menyimpan password (hashed)
    @Column(nullable = false)
    private String password;

    // Relasi Many-to-Many antara pengguna dan peran (roles)
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Collection<Role> roles;

    // Constructor tanpa argumen (dibutuhkan oleh JPA)
    public Users() {}

    // Constructor untuk inisialisasi semua field
    public Users(String firstName, String lastName, String email, String password, Collection<Role> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    // Getter dan Setter untuk ID
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getter dan Setter untuk First Name
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    // Getter dan Setter untuk Last Name
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // Getter dan Setter untuk Email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter dan Setter untuk Password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getter dan Setter untuk Roles (peran pengguna)
    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }
}
