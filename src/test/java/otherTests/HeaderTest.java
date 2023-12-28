package otherTests;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HeaderTest {


  @Test
  public void cookieTest() {
    Response response = RestAssured
            .given()
            .post("https://playground.learnqa.ru/api/homework_header")
            .andReturn();

    Headers headers = response.getHeaders();
    assertTrue(headers.hasHeaderWithName("x-secret-homework-header"), "Response doesn't have 'x-secret-homework-header' header");

    String valueSecretHeader = response.getHeader("x-secret-homework-header");
    assertEquals("Some secret value", valueSecretHeader, "Response doesn't have header value 'Some secret value'");
  }
}
