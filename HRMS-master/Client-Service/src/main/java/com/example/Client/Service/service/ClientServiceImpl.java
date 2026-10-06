package com.example.Client.Service.service;

import com.example.Client.Service.common.PagedResponse;
import com.example.Client.Service.dto.ClientDto;
import com.example.Client.Service.entity.Client;
import com.example.Client.Service.entity.Company;
import com.example.Client.Service.repository.ClientRepository;
import com.example.Client.Service.repository.CompanyRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ClientDto createClient(ClientDto clientDto, MultipartFile file) {

        // Step 1: Check for existing Mobile No or Email
        Optional<Client> existingByEmail = clientRepository.findByEmail(clientDto.getEmail());
        if (existingByEmail.isPresent()) {
            throw new RuntimeException("Client with this email already exists.");
        }

        Optional<Client> existingByMobile = clientRepository.findByMobileNo(clientDto.getMobileNo());
        if (existingByMobile.isPresent()) {
            throw new RuntimeException("Client with this mobile number already exists.");
        }

        // Step 2: Image Upload
        String fileName = null;
        String relativePath = null;

        try {
            Path uploadDir = Paths.get("uploads/clients/");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            fileName = java.util.UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = uploadDir.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            relativePath = "uploads/clients/" + fileName;

        } catch (IOException e) {
            throw new RuntimeException("Failed to store client image: " + e.getMessage());
        }

        clientDto.setProfilePicture(relativePath);

        // Step 3: Save
        Client client = modelMapper.map(clientDto, Client.class);
        client.setCreatedAt(LocalDate.now().toString());
        client.setUpdatedAt(LocalDate.now().toString());
        Client saved = clientRepository.save(client);

        return modelMapper.map(saved, ClientDto.class);
    }


    @Override
    public PagedResponse<ClientDto> getAllClients(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Client> clientPage = clientRepository.findAll(pageable);

        List<ClientDto> content = clientPage.getContent()
                .stream()
                .map(client -> modelMapper.map(client, ClientDto.class))
                .collect(Collectors.toList());

        return new PagedResponse<>(
                content,
                clientPage.getNumber(),
                clientPage.getSize(),
                clientPage.getTotalElements(),
                clientPage.getTotalPages(),
                clientPage.isLast()
        );
    }

    @Override
    public ClientDto getClientById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with ID: " + id));
        return modelMapper.map(client, ClientDto.class);
    }

    @Override
    public ClientDto updateClient(Long id, ClientDto clientDto) {
        Client existing = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with ID: " + id));

        modelMapper.map(clientDto, existing); // Update fields
        existing.setUpdatedAt(LocalDate.now().toString());
        Client updated = clientRepository.save(existing);
        return modelMapper.map(updated, ClientDto.class);
    }

    @Override
    public void deleteClient(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with ID: " + id));
        clientRepository.delete(client);
    }
}
