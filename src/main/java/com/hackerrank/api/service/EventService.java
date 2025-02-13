package com.hackerrank.api.service;

import com.hackerrank.api.model.Event;
import com.hackerrank.api.model.Report;
import java.util.List;

public interface EventService {

  List<Event> getAllEvent();

  Event createNewEvent(Event event);

  Event getEventById(Long id);

  List<Event> top3By(String by);

  Integer totalBy(String by);

  List<Report> getReport();
}
