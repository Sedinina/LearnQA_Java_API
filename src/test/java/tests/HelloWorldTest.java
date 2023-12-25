package tests;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;


import java.util.HashMap;
import java.util.Map;

public class HelloWorldTest {

  @Test
  public void testHelloName() {
    System.out.println("Hello from Ksenia");
  }

  @Test
  public void testRestAssured() {

    Map<String, String> params = new HashMap<>();
    params.put("name", "John");
    Response response = RestAssured
            .given()
            .queryParams(params)
            .get("https://playground.learnqa.ru/api/hello")
            .andReturn();
    int statusCode = response.statusCode();
    System.out.println(statusCode);
    response.prettyPrint();
  }


  @Test
  public void testRestAssuredJsonPath() {

    Map<String, String> params = new HashMap<>();
    params.put("name", "John");

    JsonPath response = RestAssured
            .given()
            .queryParams(params)
            .get("https://playground.learnqa.ru/api/hello")
            .jsonPath();

    String answer = response.get("answer");
    String name = response.get("answer2");

    if (name == null) {
      System.out.println("The key 'answer2' is absent");
    } else {
      System.out.println(name);
    }
  }


  @Test
  public void testRestAssuredCheckType() {
    Map<String, String> params = new HashMap<>();
    params.put("param1", "value1");
    params.put("param2", "value2");

    Map<String, String> headers = new HashMap<>();
    headers.put("myHeaders1", "myValue1");
    headers.put("myHeaders2", "myValue2");

    Response response = RestAssured
            .given()
            .headers(headers)
            .body(params)
            .post("https://playground.learnqa.ru/api/check_type")
            .andReturn();
    response.print();
  }

  @Test
  public void testRestAssuredHeaders() {
    Map<String, String> headers = new HashMap<>();
    headers.put("myHeaders1", "myValue1");
    headers.put("myHeaders2", "myValue2");

    Response response = RestAssured
            .given()
            .headers(headers)
            .when()
            .get("https://playground.learnqa.ru/api/show_all_headers")
            .andReturn();
    //response.prettyPrint();



    Response response303 = RestAssured
            .given()
            .redirects()
            .follow(false)
            .when()
            .get("https://playground.learnqa.ru/api/get_303")
            .andReturn();

    Headers responseHeaders = response.getHeaders();
    String locationHeader = response303.getHeader("Location");
    System.out.println(locationHeader);
    System.out.println(responseHeaders);
  }

  @Test
  public void testGetText() {
    Response response = RestAssured
            .get("https://playground.learnqa.ru/api/get_text")
            .andReturn();
    response.prettyPrint();
  }

}
