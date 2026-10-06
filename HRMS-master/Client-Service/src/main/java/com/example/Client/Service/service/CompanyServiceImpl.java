package com.example.Client.Service.service;

import com.example.Client.Service.common.PagedResponse;
import com.example.Client.Service.dto.ClientDto;
import com.example.Client.Service.dto.CompanyDto;
import com.example.Client.Service.entity.Client;
import com.example.Client.Service.entity.Company;
import com.example.Client.Service.repository.ClientRepository;
import com.example.Client.Service.repository.CompanyRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ModelMapper modelMapper;

    //  1. Create Company

    @Override
    public CompanyDto createCompany(CompanyDto companyDto) {
        // ❌ Don't allow creation if company already exists with same name (or GST or phone etc.)
        Optional<Company> existing = companyRepository.findByCompany(companyDto.getCompany());
        if (existing.isPresent()) {
            throw new RuntimeException("Company already exists with name: " + companyDto.getCompany());
        }

        Company company = modelMapper.map(companyDto, Company.class);

        List<Client> clients = new ArrayList<>();
        if (companyDto.getClientIds() != null && !companyDto.getClientIds().isEmpty()) {
            clients = clientRepository.findAllById(companyDto.getClientIds());

            if (clients.size() != companyDto.getClientIds().size()) {
                throw new RuntimeException("Some client IDs are invalid");
            }

            for (Client client : clients) {
                if (client.getCompany() != null) {
                    throw new RuntimeException("Client with ID " + client.getId() + " is already assigned to a company");
                }
                client.setCompany(company);
            }
        }

        company.setClient(clients);
        Company savedCompany = companyRepository.save(company);

        CompanyDto savedDto = modelMapper.map(savedCompany, CompanyDto.class);
        savedDto.setClientIds(
                savedCompany.getClient().stream()
                        .map(Client::getId)
                        .toList()
        );

        return savedDto;
    }

    public CompanyDto assignClientsToCompany(Long companyId, List<Long> clientIds) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found with ID: " + companyId));

        List<Client> clients = clientRepository.findAllById(clientIds);

        if (clients.size() != clientIds.size()) {
            throw new RuntimeException("Some client IDs are invalid");
        }

        for (Client client : clients) {
            if (client.getCompany() != null) {
                throw new RuntimeException("Client with ID " + client.getId() + " is already assigned to a company");
            }
            client.setCompany(company);
        }

        clientRepository.saveAll(clients); // save updated clients

        CompanyDto companyDto = modelMapper.map(company, CompanyDto.class);
        companyDto.setClientIds(
                company.getClient().stream()
                        .map(Client::getId)
                        .toList()
        );
        return companyDto;
    }



    //  2. Get All Companies
    @Override
    public PagedResponse<CompanyDto> getAllCompanies(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Company> companyPage = companyRepository.findAll(pageable);

        List<CompanyDto> content = companyPage.getContent()
                .stream()
                .map(company -> {
                    CompanyDto dto = modelMapper.map(company, CompanyDto.class);

                    dto.setClientIds(
                            company.getClient() != null ?
                                    company.getClient().stream()
                                            .map(Client::getId)
                                            .collect(Collectors.toList()) : null
                    );

                    return dto;
                })
                .collect(Collectors.toList());

        return new PagedResponse<>(
                content,
                companyPage.getNumber(),
                companyPage.getSize(),
                companyPage.getTotalElements(),
                companyPage.getTotalPages(),
                companyPage.isLast()
        );
    }




    //  3. Get Company by ID

    @Override
    public CompanyDto getCompanyById(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        CompanyDto dto = modelMapper.map(company, CompanyDto.class);

        // set clientIds
        dto.setClientIds(company.getClient().stream()
                .map(Client::getId)
                .toList());



        return dto;
    }

    //  4. Update Company
    @Override
    public CompanyDto updateCompany(Long id, CompanyDto companyDto) {
        Company existing = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found with ID: " + id));

        modelMapper.map(companyDto, existing);

        // Re-link clients with company
        if (existing.getClient() != null) {
            existing.getClient().forEach(client -> client.setCompany(existing));
        }

        Company updated = companyRepository.save(existing);
        return modelMapper.map(updated, CompanyDto.class);
    }

    //  5. Delete Company
    @Override
    public void deleteCompany(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found with ID: " + id));
        companyRepository.delete(company);
    }
}
