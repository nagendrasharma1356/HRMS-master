package com.papayaCoders.reposiory;

import com.papayaCoders.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {

    boolean existsByRoleNameIgnoreCaseAndIdNot(String roleName, Long id);
    Optional<Role> findByRoleNameIgnoreCase(String roleName);

    boolean existsByRoleName(String roleName);

}
