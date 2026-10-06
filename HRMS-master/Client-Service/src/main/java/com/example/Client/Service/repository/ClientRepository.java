package com.example.Client.Service.repository;

import com.example.Client.Service.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client,Long> {
    Optional<Client> findByEmail(String email);

    Optional<Client> findByMobileNo(String mobileNo);
}
