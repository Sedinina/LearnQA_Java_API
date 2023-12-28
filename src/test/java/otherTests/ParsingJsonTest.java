package otherTests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class ParsingJsonTest {

  @Test
  public void testRestAssured() {

    Response response = RestAssured
            .get("https://playground.learnqa.ru/api/get_json_homework")
            .andReturn();

    String message = response.jsonPath().getString("messages.message[1]");
    System.out.println(message);
  }

}
