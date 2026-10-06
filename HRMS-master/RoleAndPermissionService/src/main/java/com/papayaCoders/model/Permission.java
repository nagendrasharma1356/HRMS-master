package com.papayaCoders.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Permission
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Message name is required")
    private String name;

    private String status="ACTIVE";

    @ManyToMany(mappedBy = "permissions") // Bi-directional mapping
    private List<Role> roles;

    private String httpMethod; // e.g., POST, PUT
    private String apiRoute;

    private LocalDate createdAt;

    private LocalDate updatedAt;
}
