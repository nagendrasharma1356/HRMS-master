package com.example.Client.Service.service;

import com.example.Client.Service.common.PagedResponse;
import com.example.Client.Service.dto.ClientDto;
import com.example.Client.Service.dto.CompanyDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ClientService {
    ClientDto createClient(ClientDto clientDto, MultipartFile file);
    PagedResponse<ClientDto> getAllClients(int page,int size);
    ClientDto getClientById(Long id);
    ClientDto updateClient(Long id, ClientDto clientDto);
    void deleteClient(Long id);


}