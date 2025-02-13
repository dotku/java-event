package com.hackerrank.api.service.impl;

import com.hackerrank.api.exception.BadRequestException;
import com.hackerrank.api.model.Event;
import com.hackerrank.api.model.Report;
import com.hackerrank.api.repository.EventRepository;
import com.hackerrank.api.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DefaultEventService implements EventService {
  private final EventRepository eventRepository;

  @Autowired
  DefaultEventService(EventRepository eventRepository) {
    this.eventRepository = eventRepository;
  }

  @Override
  public List<Event> getAllEvent() {
    return eventRepository.findAll();
  }


  @Override
  public Event createNewEvent(Event event) {
    if (event.getId() != null) {
      throw new BadRequestException("The ID must not be provided when creating a new Event");
    }

    return eventRepository.save(event);
  }

  @Override
  public Event getEventById(Long id) {
    return null;
  }

  @Override
  public List<Event> top3By(String by) {
    return null;
  }

  @Override
  public Integer totalBy(String by) {
    return null;
  }

  @Override
  public List<Report> getReport() {
    return null;
  }
}
