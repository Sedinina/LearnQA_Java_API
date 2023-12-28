package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("Delete cases")
@Feature("Deletion")
public class UserDeleteTest extends BaseTestCase {

  String cookie;
  String header;
  private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();


  @Test
  @Description("This test checks the positive case of delete user data")
  @DisplayName("Test deletion of user data")
  public void deleteTest() {
    //GENERATE USER
    Map<String, String> userData = DataGenerator.getRegistrationData();
    JsonPath responseCreateAuth = apiCoreRequests.makePostRequestUser(
            "https://playground.learnqa.ru/api/user/",
            userData);
    String userId = responseCreateAuth.getString("id");

    //LOGIN
    Map<String, String> authData = new HashMap<>();
    authData.put("email", userData.get("email"));
    authData.put("password", userData.get("password"));

    Response responseGetAuth = apiCoreRequests
            .makePostRequest("https://playground.learnqa.ru/api/user/login"
                    , authData);

    this.cookie = this.getCookie(responseGetAuth, "auth_sid");
    this.header = this.getHeader(responseGetAuth, "x-csrf-token");

    //DELETE
    Response responseDeleteUser = apiCoreRequests.makePostRequestDeleteUser(
            "https://playground.learnqa.ru/api/user/",
            this.header,
            this.cookie,
            userId);

    //GET
    Response responseUserData = apiCoreRequests.makeGetRequestWithId(
            "https://playground.learnqa.ru/api/user/",
            this.header,
            this.cookie,
            userId);

    Assertions.assertResponseTextEquals(responseUserData, "User not found");
  }

  @Test
  @Description("This test checks the negative case of delete user data")
  @DisplayName("Test no deletion of user data")
  public void deleteNegativeTest() {

    //GENERATE USER 1
    Map<String, String> userData = DataGenerator.getRegistrationData();
    JsonPath responseCreateAuth = apiCoreRequests.makePostRequestUser(
            "https://playground.learnqa.ru/api/user/",
            userData);
    String userId = responseCreateAuth.getString("id");


    //LOGIN USER 1
    Map<String, String> authData = new HashMap<>();
    authData.put("email", userData.get("email"));
    authData.put("password", userData.get("password"));

    Response responseGetAuth = apiCoreRequests
            .makePostRequest("https://playground.learnqa.ru/api/user/login"
                    , authData);

    this.cookie = this.getCookie(responseGetAuth, "auth_sid");
    this.header = this.getHeader(responseGetAuth, "x-csrf-token");

    //GENERATE USER 2
    Map<String, String> userDataTwo = DataGenerator.getRegistrationData();
    JsonPath responseCreateAuthUserTwo = apiCoreRequests.makePostRequestUser(
            "https://playground.learnqa.ru/api/user/",
            userDataTwo);
    String userTwoId = responseCreateAuthUserTwo.getString("id");

    //DELETE
    Response responseDeleteUser = apiCoreRequests.makePostRequestDeleteUser(
            "https://playground.learnqa.ru/api/user/",
            this.header,
            this.cookie,
            userTwoId);

    //GET
    Response responseUserData2 = apiCoreRequests.makeGetRequestWithId(
            "https://playground.learnqa.ru/api/user/",
            userTwoId);

    Assertions.assertJsonHasField(responseUserData2, "username");
  }

  @Test
  @Description("This test checks the case of delete user with id =2")
  @DisplayName("Test no deletion of user with id=2")
  public void deleteUserIdTest() {
    Map<String, String> authData = new HashMap<>();
    authData.put("email", "vinkotov@example.com");
    authData.put("password", "1234");

    Response responseGetAuth = apiCoreRequests
            .makePostRequest("https://playground.learnqa.ru/api/user/login",
                    authData);

    this.cookie = this.getCookie(responseGetAuth, "auth_sid");
    this.header = this.getHeader(responseGetAuth, "x-csrf-token");


    Response responseDeleteUser = apiCoreRequests.makePostRequestDeleteUser(
            "https://playground.learnqa.ru/api/user/",
            this.header,
            this.cookie,
            "2");


    Response responseUserData = apiCoreRequests.makeGetRequestWithId(
            "https://playground.learnqa.ru/api/user/",
            this.header,
            this.cookie,
            "2");

    Assertions.assertJsonHasField(responseUserData, "username");
    Assertions.assertJsonHasField(responseUserData, "firstName");
    Assertions.assertJsonHasField(responseUserData, "lastName");
    Assertions.assertJsonHasField(responseUserData, "email");
  }


}

