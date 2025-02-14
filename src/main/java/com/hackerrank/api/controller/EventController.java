package com.hackerrank.api.controller;

import com.hackerrank.api.model.Event;
import com.hackerrank.api.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/event")
public class EventController {
  private final EventService eventService;

  @Autowired
  public EventController(EventService eventService) {
    this.eventService = eventService;
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Event getEventByIdNew(@PathVariable Long id) {
    System.out.println("Fetching event with ID: " + id);
    Event event = eventService.getEventById(id);
    System.out.println("Retrieved event: " + event);
    return eventService.getEventById(id);
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
}
