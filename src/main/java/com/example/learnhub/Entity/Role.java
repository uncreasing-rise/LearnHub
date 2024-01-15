package com.example.learnhub.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "Role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RoleID")
    private Integer roleId;

    @Column(name = "RoleName")
    private String roleName;

    @OneToMany(mappedBy = "role")
    private Set<User> users;

    // Constructors, getters, setters, and other methods as needed
}
