import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CookieTest {

  @Test
  public void cookieTest() {
    Response response = RestAssured
            .given()
            .post("https://playground.learnqa.ru/api/homework_cookie")
            .andReturn();

    Map<String, String> cookies = response.getCookies();
    assertTrue(cookies.containsKey("HomeWork"), "Response doesn't have 'HomeWork' cookie");
    assertTrue(cookies.containsValue("hw_value"), "Response doesn't have 'hw_value' cookie");

  }

}
