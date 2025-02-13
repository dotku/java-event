package com.hackerrank.eval;

import com.hackerrank.eval.extensions.RESTExtension;
import com.hackerrank.eval.model.Event;
import com.hackerrank.eval.model.Report;
import static io.restassured.RestAssured.get;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExtendWith({RESTExtension.class})
class FunctionalTests {

  private static Map<String, Report> getExpectedReport() {
    Map<String, Report> map = new HashMap<>();
    map.put("X city", Report.builder()
            .location("X city")
            .totalEvents(2)
            .costDurationRatio(2.333)
            .build());

    map.put("A city", Report.builder()
            .location("A city")
            .totalEvents(1)
            .costDurationRatio(3.0)
            .build());

    map.put("B city", Report.builder()
            .location("B city")
            .totalEvents(1)
            .costDurationRatio(2.333)
            .build());

    map.put("Z city", Report.builder()
            .location("Z city")
            .totalEvents(1)
            .costDurationRatio(0.833)
            .build());

    map.put("R city", Report.builder()
            .location("R city")
            .totalEvents(1)
            .costDurationRatio(0.025)
            .build());

    map.put("Y city", Report.builder()
            .location("Y city")
            .totalEvents(1)
            .costDurationRatio(0.045)
            .build());

    return map;
  }

  @Test
  @DisplayName("test top 3")
  void testTop3() {
    List<Event> actual = Arrays.asList(get("/event/top3?by=duration")
            .then()
            .statusCode(SC_OK)
            .extract()
            .response()
            .as(Event[].class));

    assertThat(
            actual.stream().map(Event::getName).toArray(),
            arrayContaining(new String[]{"event7", "event6", "event5"}));
  }

  @Test
  @DisplayName("total")
  void testTotal() {
    Integer actual = get("/event/total?by=cost")
            .then()
            .statusCode(SC_OK)
            .extract()
            .response()
            .as(Integer.class);

    Assertions.assertEquals(28, actual);
  }

  @Test
  void correctDashboardReportReturned() {
    Report[] actual =
            get("/event/report/eventDashboard")
                    .then()
                    .statusCode(200)
                    .extract()
                    .response()
                    .as(Report[].class);
    Map<String, Report> actualMap = new HashMap<>();
    for (int i = 0; i < actual.length; i++) {
      actualMap.put(actual[i].getLocation(), actual[i]);
    }

    Map<String, Report> expectedMap = getExpectedReport();

    Assertions.assertEquals(expectedMap.size(), actualMap.size());

    for (Map.Entry<String, Report> e : expectedMap.entrySet()) {
      Assertions.assertTrue(actualMap.containsKey(e.getKey()));
      Assertions.assertTrue(assertEquals(e.getValue().getTotalEvents(), actualMap.get(e.getKey()).getTotalEvents()));
      Assertions.assertTrue(assertEquals(e.getValue().getCostDurationRatio(), actualMap.get(e.getKey()).getCostDurationRatio()));
    }
  }

  public boolean assertEquals(double x, double y) {
    x = (long) (x * Math.pow(10, 2));
    y = (long) (y * Math.pow(10, 2));
    return x == y;
  }
}
