package com.heart.disease.predict.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "role")
public class Role {

    // Membuat sequence generator khusus untuk ID role
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_seq")
    @SequenceGenerator(name = "role_seq", sequenceName = "role_sequence", allocationSize = 1)
    private Long id;

    // Nama dari role, misalnya: ROLE_USER, ROLE_ADMIN
    @NotBlank(message = "Nama role tidak boleh kosong")
    private String name;

    // Constructor default (diperlukan oleh JPA)
    public Role() {
    }

    // Constructor dengan parameter untuk inisialisasi nama role
    public Role(String name) {
        // Validasi jika nama role null atau kosong
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Nama role tidak boleh null atau kosong");
        }
        // Memastikan bahwa nama role selalu diawali dengan "ROLE_"
        if (!name.startsWith("ROLE_")) {
            throw new IllegalArgumentException("Nama role harus diawali dengan 'ROLE_'");
        }
        this.name = name;
    }

    // Getter untuk ID role
    public Long getId() {
        return id;
    }

    // Setter untuk ID role
    public void setId(Long id) {
        this.id = id;
    }

    // Getter untuk nama role
    public String getName() {
        return name;
    }

    // Setter untuk nama role
    public void setName(String name) {
        // Validasi jika nama role null atau kosong
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Nama role tidak boleh null atau kosong");
        }
        // Memastikan bahwa nama role selalu diawali dengan "ROLE_"
        if (!name.startsWith("ROLE_")) {
            throw new IllegalArgumentException("Nama role harus diawali dengan 'ROLE_'");
        }
        this.name = name;
    }

    // Override method equals() untuk membandingkan objek Role berdasarkan ID
    @Override
    public boolean equals(Object obj) {
        // Mengecek apakah objek yang dibandingkan adalah objek yang sama
        if (this == obj) return true;
        // Mengecek apakah objek null atau kelasnya berbeda
        if (obj == null || getClass() != obj.getClass()) return false;
        // Cast objek ke tipe Role dan bandingkan berdasarkan ID
        Role role = (Role) obj;
        return id != null && id.equals(role.id);
    }

    // Override method hashCode() untuk menghasilkan nilai hash berdasarkan ID
    @Override
    public int hashCode() {
        return 31;
    }

    // Override method toString() untuk memberikan representasi string dari objek Role
    @Override
    public String toString() {
        return "Role{id=" + id + ", name='" + name + "'}";
    }
}
