package com.hackerrank.api.service.impl;

import com.hackerrank.api.exception.BadRequestException;
import com.hackerrank.api.model.Event;
import com.hackerrank.api.model.Report;
import com.hackerrank.api.repository.EventRepository;
import com.hackerrank.api.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    return eventRepository.findById(id)
        .orElseThrow(() -> new BadRequestException("Event not found with id: " + id));
  }

  @Override
  public List<Event> top3By(String by) {
    List<Event> allEvents = eventRepository.findAll();
    
    Comparator<Event> comparator = switch (by.toLowerCase()) {
      case "cost" -> Comparator.comparing(Event::getCost).reversed();
      case "duration" -> Comparator.comparing(Event::getDuration).reversed();
      default -> throw new BadRequestException("Invalid sort criteria: " + by);
    };

    return allEvents.stream()
        .sorted(comparator)
        .limit(3)
        .collect(Collectors.toList());
  }

  @Override
  public Integer totalBy(String by) {
    List<Event> events = eventRepository.findAll();
    
    return switch (by.toLowerCase()) {
      case "cost" -> events.stream().mapToInt(Event::getCost).sum();
      case "duration" -> events.stream().mapToInt(Event::getDuration).sum();
      default -> throw new BadRequestException("Invalid sum criteria: " + by);
    };
  }

  @Override
  public List<Report> getReport() {
    List<Event> events = eventRepository.findAll();
    
    // Filter out events with null location or name
    events = events.stream()
        .filter(event -> event.getLocation() != null && !event.getLocation().trim().isEmpty())
        .filter(event -> event.getName() != null && !event.getName().trim().isEmpty())
        .collect(Collectors.toList());

    // Group by location
    Map<String, List<Event>> eventsByLocation = events.stream()
        .collect(Collectors.groupingBy(Event::getLocation));

    // Calculate metrics for each location
    return eventsByLocation.entrySet().stream()
        .map(entry -> {
          String location = entry.getKey();
          List<Event> locationEvents = entry.getValue();
          
          int totalEvents = locationEvents.size();
          int totalCost = locationEvents.stream()
              .mapToInt(Event::getCost)
              .sum();
          int totalDuration = locationEvents.stream()
              .mapToInt(Event::getDuration)
              .sum();
          
          // Calculate cost/duration ratio with 3 decimal places
          double ratio = totalDuration == 0 ? 0.0 :
              BigDecimal.valueOf(totalCost)
                  .divide(BigDecimal.valueOf(totalDuration), 3, RoundingMode.HALF_UP)
                  .doubleValue();
          
          return Report.builder()
              .location(location)
              .totalEvents(totalEvents)
              .costDurationRatio(ratio)
              .build();
        })
        .collect(Collectors.toList());
  }
}
