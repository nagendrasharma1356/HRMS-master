package com.example.Client.Service.controller;

import com.example.Client.Service.common.ApiResponse;
import com.example.Client.Service.common.PagedResponse;
import com.example.Client.Service.dto.ClientDto;
import com.example.Client.Service.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    //  Create Client with profile picture and companyId
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ClientDto>> createClient(
            @RequestParam("salutation") String salutation,
            @RequestParam("clientName") String clientName,
            @RequestParam("email") String email,
            @RequestParam("country") String country,
            @RequestParam("mobileNo") String mobileNo,
//            @RequestParam("companyId") Long companyId,
            @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture
    ) {
        ClientDto clientDto = new ClientDto();
        clientDto.setSalutation(salutation);
        clientDto.setClientName(clientName);
        clientDto.setEmail(email);
        clientDto.setCountry(country);
        clientDto.setMobileNo(mobileNo);

        ClientDto createdClient = clientService.createClient(clientDto, profilePicture);

        ApiResponse<ClientDto> response = ApiResponse.<ClientDto>builder()
                .success(true)
                .message("Client created successfully!")
                .data(createdClient)
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //  Get all clients with pagination
    @GetMapping("/all")
    public ResponseEntity<PagedResponse<ClientDto>> getAllClients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        PagedResponse<ClientDto> clients = clientService.getAllClients(page, size);
        return ResponseEntity.ok(clients);
    }

    //  Get client by ID
    @GetMapping("/getById/{id}")
    public ResponseEntity<ApiResponse<ClientDto>> getClientById(@PathVariable Long id) {
        ClientDto clientDto = clientService.getClientById(id);
        ApiResponse<ClientDto> response = ApiResponse.<ClientDto>builder()
                .success(true)
                .message("Client fetched successfully!")
                .data(clientDto)
                .build();
        return ResponseEntity.ok(response);
    }

    //  Update client by ID
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<ClientDto>> updateClient(@PathVariable Long id, @RequestBody ClientDto clientDto) {
        ClientDto updatedClient = clientService.updateClient(id, clientDto);
        ApiResponse<ClientDto> response = ApiResponse.<ClientDto>builder()
                .success(true)
                .message("Client updated successfully!")
                .data(updatedClient)
                .build();
        return ResponseEntity.ok(response);
    }

    //  Delete client by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<String>> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        ApiResponse<String> response = ApiResponse.<String>builder()
                .success(true)
                .message("Client deleted successfully!")
                .data("Client with ID " + id + " has been deleted.")
                .build();
        return ResponseEntity.ok(response);
    }
}
