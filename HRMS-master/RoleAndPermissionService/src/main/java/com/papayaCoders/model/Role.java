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
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Role Name is required")
    private String roleName;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinTable(
            name = "role_permission",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private List<Permission> permissions;

    private LocalDate createdAt;

    private LocalDate updatedAt;



/*joinColumns = @JoinColumn(name = "role_id")
→ Specifies the foreign key in the join table that references the Role entity’s primary key.
In simpler words: This column holds the Role ID.

inverseJoinColumns = @JoinColumn(name = "permission_id")
→ Specifies the foreign key in the join table that references the Permission entity’s primary key.
In simpler words: This column holds the Permission ID.

*/
}
