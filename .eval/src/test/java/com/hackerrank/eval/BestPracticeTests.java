package com.hackerrank.eval;

import com.hackerrank.eval.extensions.RESTExtension;
import static io.restassured.RestAssured.get;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({RESTExtension.class})
class BestPracticeTests {

  @Test
  @DisplayName("statusCode404WhenNonExistentEventRequested")
  void statusCode404WhenNonExistentEventRequested() throws Exception {
    get("/event/byId/-1")
            .then().statusCode(SC_NOT_FOUND);
  }


  @Test
  @DisplayName("statusCode400WhenTop3ByInvalid")
  void statusCode400WhenTop3ByInvalid() throws Exception {
    get("/event/top3?by=invalid")
            .then().statusCode(SC_BAD_REQUEST);
  }

  @Test
  @DisplayName("statusCode400WhenTotalByInvalid")
  void statusCode400WhenTotalByInvalid() throws Exception {
    get("/event/total?by=invalid")
            .then().statusCode(SC_BAD_REQUEST);
  }
}
