package tests;

import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.qameta.allure.SeverityLevel.MINOR;

@Epic("Showing user data cases")
@Feature("Showing user data")
public class UserGetTest extends BaseTestCase {
  String cookie;
  String header;
  private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

  @Test
  @Owner("Sedinina Ksenia")
  @Description("This test unauthorized request for data - we only received a username there")
  @DisplayName("Test unauthorized request only username")
  @Severity(MINOR)
  public void testGetUserDataNotAuth() {

    Response responseUserData = RestAssured
            .get("https://playground.learnqa.ru/api/user/2")
            .andReturn();

    System.out.println(responseUserData.asString());
    Assertions.assertJsonHasField(responseUserData, "username");
    Assertions.assertJsonHasNotField(responseUserData, "firstName");
    Assertions.assertJsonHasNotField(responseUserData, "lastName");
    Assertions.assertJsonHasNotField(responseUserData, "email");
  }

  @Test
  @Description("This test request to get data from the same user")
  @DisplayName("Authorized request")
  public void testGetUserDetailsAuthAsSameUser(){
    Map<String, String> authData = new HashMap<>();
    authData.put("email", "vinkotov@mail.ru");
    authData.put("password", "123");

    Response responseGetAuth = apiCoreRequests
            .makePostRequest("https://playground.learnqa.ru/api/user/login",
                    authData);

    this.cookie = this.getCookie(responseGetAuth, "auth_sid");
    this.header = this.getHeader(responseGetAuth, "x-csrf-token");

    Response responseUserData = apiCoreRequests
            .makeGetRequest("https://playground.learnqa.ru/api/user/87799",
                    this.header,
                    this.cookie);

    String[] expectedFields = {"username","firstName","lastName","email" };
    Assertions.assertJsonHasFields(responseUserData, expectedFields);
  }


  @Test
  @Description("his test checks get information about other user by auth user")
  @DisplayName("Test get user username by other auth user")
  public void testGetOtherDataAuth() {
    Map<String, String> authData = new HashMap<>();
    authData.put("email", "vinkotov@mail.ru");
    authData.put("password", "123");

    Response responseGetAuth = apiCoreRequests
            .makePostRequest("https://playground.learnqa.ru/api/user/login",
                    authData);

    this.cookie = this.getCookie(responseGetAuth, "auth_sid");
    this.header = this.getHeader(responseGetAuth, "x-csrf-token");


    Response responseUserData = apiCoreRequests
            .makeGetRequest("https://playground.learnqa.ru/api/user/2",
                    this.header,
                    this.cookie);

    System.out.println(responseUserData.asString());
    Assertions.assertJsonHasField(responseUserData, "username");
    Assertions.assertJsonHasNotField(responseUserData, "firstName");
    Assertions.assertJsonHasNotField(responseUserData, "lastName");
    Assertions.assertJsonHasNotField(responseUserData, "email");
  }


}
