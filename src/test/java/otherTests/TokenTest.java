package otherTests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class TokenTest {


  @Test
  public void tokenTest() throws InterruptedException {
    JsonPath responseToken = RestAssured
            .get("https://playground.learnqa.ru/ajax/api/longtime_job")
            .jsonPath();

    String token = responseToken.get("token");
    Map<String, String> params = new HashMap<>();
    params.put("token", token);

    Response responseJob = RestAssured
            .given()
            .queryParams(params)
            .get("https://playground.learnqa.ru/ajax/api/longtime_job")
            .andReturn();

    String status = responseJob.jsonPath().getString("status");
    String error = responseJob.jsonPath().getString("error");
    String result = responseJob.jsonPath().getString("result");


    if (status.equals("Job is NOT ready")) {
      Thread.sleep(1000);
    } else if (status.equals("Job is ready")) {
      System.out.println(result);
    } else {
      System.out.println(error);
    }
  }

}
