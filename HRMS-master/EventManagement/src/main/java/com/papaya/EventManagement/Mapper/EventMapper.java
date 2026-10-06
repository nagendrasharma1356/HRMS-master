package com.papaya.EventManagement.Mapper;

import com.papaya.EventManagement.DTO.EventDto;
import com.papaya.EventManagement.Entity.Event;

import java.time.LocalDateTime;
import java.util.Date;

public class EventMapper {

    // Entity to DTO
    public static EventDto toDto(Event event) {
        EventDto dto = new EventDto();
        dto.setId(event.getId());
        dto.setEventName(event.getEventName());
        dto.setLocation(event.getLocation());
        dto.setDescription(event.getDescription());
        dto.setStartOnDate(event.getStartOnDate());
        dto.setStartOnTime(event.getStartOnTime());
        dto.setEndOnDate(event.getEndOnDate());
        dto.setEndOnTime(event.getEndOnTime());
        dto.setDepartment(event.getDepartment());
        dto.setStatus(event.getStatus());
        dto.setFilePath(event.getFilePath());
        dto.setEventLink(event.getEventLink());
        dto.setCreatedAt(event.getCreatedAt());
        dto.setUpdatedAt(event.getUpdatedAt());


        return dto;
    }

    // DTO to Entity
    public static Event toEntity(EventDto dto) {
        Event event = new Event();
        event.setId(dto.getId());
        event.setEventName(dto.getEventName());
        event.setLocation(dto.getLocation());
        event.setDescription(dto.getDescription());
        event.setStartOnDate(dto.getStartOnDate());
        event.setStartOnTime(dto.getStartOnTime());
        event.setEndOnDate(dto.getEndOnDate());
        event.setEndOnTime(dto.getEndOnTime());
        event.setDepartment(dto.getDepartment());
        event.setStatus(dto.getStatus());
        event.setFilePath(dto.getFilePath());
        event.setEventLink(dto.getEventLink());

        // createdAt and updatedAt will be set manually in service layer
        return event;
    }
}

