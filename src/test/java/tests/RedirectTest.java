package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class RedirectTest {

  @Test
  public void testRestAssured() {
    Response response = RestAssured
            .given()
            .redirects()
            .follow(false)
            .when()
            .get("https://playground.learnqa.ru/api/long_redirect")
            .andReturn();

    String locationHeader = response.getHeader("Location");
    System.out.println(locationHeader);

  }


  @Test
  public void testLongRedirect() {
    String url = "https://playground.learnqa.ru/api/long_redirect";
    int statusCode = 0;

    while (statusCode != 200) {
      Response response = RestAssured
              .given()
              .redirects()
              .follow(false)
              .when()
              .get(url)
              .andReturn();

      statusCode = response.statusCode();
      url = response.getHeader("Location");
      System.out.println(statusCode);
      if (statusCode != 200) {
        System.out.println(url);
      }
    }
  }
}
