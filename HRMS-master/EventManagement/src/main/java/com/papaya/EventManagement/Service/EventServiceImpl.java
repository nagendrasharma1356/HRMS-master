package com.papaya.EventManagement.Service;

import com.papaya.EventManagement.Config.PagedResponse;
import com.papaya.EventManagement.DTO.EventDto;
import com.papaya.EventManagement.Entity.Event;
import com.papaya.EventManagement.Mapper.EventMapper;
import com.papaya.EventManagement.Repository.EventRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static jakarta.persistence.GenerationType.UUID;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ModelMapper mapper;


    @Override
    public EventDto createEvent(EventDto eventDto, MultipartFile imageFile) {
        boolean exists = eventRepository.existsByEventNameAndStartOnDate(
                eventDto.getEventName(),
                eventDto.getStartOnDate()
        );

        if (exists) {
            throw new RuntimeException("Event with same name already exists on the same date!");
        }

        String fileName = null;
        String relativePath = null;

        try {
            Path uploadDir = Paths.get("uploads/");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            // Unique file name banayein
            fileName = java.util.UUID.randomUUID() + "_" + imageFile.getOriginalFilename();

            Path filePath = uploadDir.resolve(fileName);
            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Relative path jo DB me store karna hai
            relativePath = "uploads/" + fileName;

        } catch (IOException e) {
            throw new RuntimeException("Failed to store image: " + e.getMessage());
        }

        // Set relative path in DTO (aap chahe to filePath ya imageName me bhi daal sakte hain)
        eventDto.setFilePath(relativePath);  // yaha path store karenge
        eventDto.setImageName(fileName);     // yaha sirf file name store kar sakte ho, optional

        // Map DTO to Entity
        Event event = mapper.map(eventDto, Event.class);

        event.setCreatedAt(LocalDate.now());
        event.setUpdatedAt(LocalDate.now());

        // Save to DB
        Event savedEvent = eventRepository.save(event);

        return mapper.map(savedEvent, EventDto.class);
    }



    @Override
    @Deprecated
    public List<EventDto> getAllEvents() {
        return eventRepository.findAll()
                .stream()
                .map(EventMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventDto getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id " + id));
        return EventMapper.toDto(event);
    }

    @Override
    public EventDto updateEvent(Long id, EventDto eventDto) {
        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id " + id));

        existingEvent.setEventName(eventDto.getEventName());
        existingEvent.setLocation(eventDto.getLocation());
        existingEvent.setDescription(eventDto.getDescription());
        existingEvent.setStartOnDate(eventDto.getStartOnDate());
        existingEvent.setStartOnTime(eventDto.getStartOnTime());
        existingEvent.setEndOnDate(eventDto.getEndOnDate());
        existingEvent.setEndOnTime(eventDto.getEndOnTime());
        existingEvent.setDepartment(eventDto.getDepartment());
        existingEvent.setEventLink(eventDto.getEventLink());
        existingEvent.setFilePath(eventDto.getFilePath());
        existingEvent.setStatus(eventDto.getStatus());
        existingEvent.setUpdatedAt(LocalDate.now());

        Event updatedEvent = eventRepository.save(existingEvent);
        return EventMapper.toDto(updatedEvent);
    }

    @Override
    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new RuntimeException("Event not found with id " + id);
        }
        eventRepository.deleteById(id);
    }

    @Override
    public void setStatus(Long eventId, String status) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id " + eventId));
        event.setStatus(Boolean.parseBoolean(status));
        event.setUpdatedAt(LocalDate.now());
        eventRepository.save(event);
    }
    // pagination
    @Override
    public PagedResponse<EventDto> getAllEvents(
            int page,
            int size,
            String sortBy,
            String sortDir,
            String filterType,   // "days", "weeks", "months"
            Integer filterAmount // number of days/weeks/months
    ) {
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        LocalDate now = LocalDate.now();
        LocalDate filterDate = null;

        if (filterType != null && filterAmount != null) {
            switch (filterType.toLowerCase()) {
                case "days":
                    filterDate = now.minusDays(filterAmount);
                    break;
                case "weeks":
                    filterDate = now.minusWeeks(filterAmount);
                    break;
                case "months":
                    filterDate = now.minusMonths(filterAmount);
                    break;
                default:
                    filterDate = null;
            }
        }

        Page<Event> eventsPage;
        if (filterDate != null) {
            // Filter events where startOnDate is after filterDate
            eventsPage = eventRepository.findByStartOnDateAfter(filterDate, pageable);
        } else {
            eventsPage = eventRepository.findAll(pageable);
        }

        List<EventDto> content = eventsPage.getContent()
                .stream()
                .map(EventMapper::toDto)
                .toList();

        return new PagedResponse<>(
                content,
                eventsPage.getNumber(),
                eventsPage.getSize(),
                eventsPage.getTotalElements(),
                eventsPage.getTotalPages(),
                eventsPage.isLast()
        );
    }


}


