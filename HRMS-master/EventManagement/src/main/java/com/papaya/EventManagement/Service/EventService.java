package com.papaya.EventManagement.Service;

import com.papaya.EventManagement.Config.PagedResponse;
import com.papaya.EventManagement.DTO.EventDto;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

public interface EventService {

    EventDto createEvent(EventDto eventDto, MultipartFile imageFile);

    @Deprecated
    List<EventDto> getAllEvents();
    EventDto getEventById(Long id);
    EventDto updateEvent(Long id, EventDto eventDto);
    void deleteEvent(Long id);

    void setStatus(Long eventId, String status);

    //pagination
    PagedResponse<EventDto> getAllEvents(int page, int size, String sortBy, String sortDir, String filterType, Integer filterValue);


}
