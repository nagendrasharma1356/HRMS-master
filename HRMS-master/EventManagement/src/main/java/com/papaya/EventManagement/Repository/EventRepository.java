package com.papaya.EventManagement.Repository;

import com.papaya.EventManagement.Entity.Event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.time.LocalDate;

public interface EventRepository extends JpaRepository<Event, Long>
{
    boolean existsByEventNameAndStartOnDate(String eventName, LocalDate startOnDate);
    Page<Event> findByStartOnDateAfter(LocalDate date, Pageable pageable);

}
