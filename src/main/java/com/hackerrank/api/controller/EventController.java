package com.hackerrank.api.controller;

import com.hackerrank.api.model.Event;
import com.hackerrank.api.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event")
public class EventController {
  private final EventService eventService;

  @Autowired
  public EventController(EventService eventService) {
    this.eventService = eventService;
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<Event> getAllEvents() {
    return eventService.getAllEvent();
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Event getEventByIdNew(@PathVariable Long id) {
    return getEventById(id);
  }

  @GetMapping("/byId/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Event getEventById(@PathVariable Long id) {
    return eventService.getEventById(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Event createEvent(@RequestBody Event event) {
    return eventService.createNewEvent(event);
  }

  @GetMapping("/top3")
  @ResponseStatus(HttpStatus.OK)
  public List<Event> getTop3By(@RequestParam(name = "by", required = true) String by) {
    if (by == null || by.trim().isEmpty()) {
      throw new BadRequestException("Parameter 'by' is required and must be either 'cost' or 'duration'");
    }
    String criteria = by.trim().toLowerCase();
    if (!criteria.equals("cost") && !criteria.equals("duration")) {
      throw new BadRequestException("Parameter 'by' must be either 'cost' or 'duration'");
    }
    return eventService.top3By(criteria);
  }

  @GetMapping("/total")
  @ResponseStatus(HttpStatus.OK)
  public Integer getTotalBy(@RequestParam(name = "by", required = true) String by) {
    if (by == null || by.trim().isEmpty()) {
      throw new BadRequestException("Parameter 'by' is required and must be either 'cost' or 'duration'");
    }
    String criteria = by.trim().toLowerCase();
    if (!criteria.equals("cost") && !criteria.equals("duration")) {
      throw new BadRequestException("Parameter 'by' must be either 'cost' or 'duration'");
    }
    return eventService.totalBy(criteria);
  }
}
