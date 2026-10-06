package com.papaya.EventManagement.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.papaya.EventManagement.Config.ApiResponse;
import com.papaya.EventManagement.Config.PagedResponse;
import com.papaya.EventManagement.DTO.EventDto;
import com.papaya.EventManagement.Service.EventService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    //  Create Event
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<EventDto>> createEventWithImage(
            @RequestParam("eventName") String eventName,
            @RequestParam("location") String location,
            @RequestParam("description") String description,
            @RequestParam("startOnDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startOnDate,
            @RequestParam("startOnTime") @DateTimeFormat(pattern = "HH:mm:ss") LocalTime startOnTime,
            @RequestParam("endOnDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endOnDate,
            @RequestParam("endOnTime") @DateTimeFormat(pattern = "HH:mm:ss") LocalTime endOnTime,
            @RequestParam("department") String department,
            @RequestParam("status") Boolean status,
            @RequestParam(value = "eventLink", required = false) String eventLink,
            @RequestPart(value = "image", required = false) MultipartFile imageFile
    ) {
        EventDto eventDto = new EventDto();
        eventDto.setEventName(eventName);
        eventDto.setLocation(location);
        eventDto.setDescription(description);
        eventDto.setStartOnDate(startOnDate);
        eventDto.setStartOnTime(startOnTime);
        eventDto.setEndOnDate(endOnDate);
        eventDto.setEndOnTime(endOnTime);
        eventDto.setDepartment(department);
        eventDto.setStatus(status);
        eventDto.setEventLink(eventLink);

        EventDto createdEvent = eventService.createEvent(eventDto, imageFile);

        ApiResponse<EventDto> response = new ApiResponse<>();
        response.setMessage("Event created successfully!");
        response.setSuccess(true);
        response.setData(createdEvent);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }




    // Get Single Event by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EventDto>> getEventById(@PathVariable Long id) {
        EventDto event = eventService.getEventById(id);
        ApiResponse<EventDto> response = new ApiResponse<>();
        response.setMessage("Event created successfully!");
        response.setSuccess(true);
        response.setData(event);

        return ResponseEntity.ok(response);
    }

    //  Update Event
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<EventDto>> updateEvent(@PathVariable Long id, @RequestBody EventDto eventDto) {
        EventDto updated = eventService.updateEvent(id, eventDto);
        ApiResponse<EventDto> response = new ApiResponse<>();
        response.setMessage("Event created successfully!");
        response.setSuccess(true);
        response.setData(updated);

        return ResponseEntity.ok(response);
    }

    // Delete Event
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<String>> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        ApiResponse<String> response = new ApiResponse<>();
        response.setMessage("Event created successfully!");
        response.setSuccess(true);
        response.setData(null);

        return ResponseEntity.ok(response);
    }
//    @Deprecated


    @GetMapping("/all")
    public ResponseEntity<PagedResponse<EventDto>> getAllEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "startOnDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) String filterType, // days, weeks, months
            @RequestParam(required = false) Integer filterValue
    ) {
        PagedResponse<EventDto> response = eventService.getAllEvents(page, size, sortBy, sortDir, filterType, filterValue);
        return ResponseEntity.ok(response);
    }



}