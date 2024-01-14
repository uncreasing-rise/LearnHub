package com.example.learnhub.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Role")
public class Role {
    @Id
    @Column(name = "RoleID")
    private Integer roleId;

    @Column(name = "RoleName")
    private String roleName;

}
