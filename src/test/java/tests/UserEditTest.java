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

@Epic("Edit cases")
@Feature("Editing")
public class UserEditTest extends BaseTestCase {

  String cookie;
  String header;
  private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

  @Test
  @Description("This test checks the positive case of editing user data")
  @DisplayName("Test editing user data")
  public void editJustCreatedTest() {
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

    //EDIT
    String newName = "New name";
    Map<String, String> editData = new HashMap<>();
    editData.put("firstName", newName);

    Response responseEditUser = apiCoreRequests.makePostRequestEditUser(
            "https://playground.learnqa.ru/api/user/",
            this.header,
            this.cookie,
            editData,
            userId);

    //GET
    Response responseUserData = apiCoreRequests.makeGetRequestWithId(
            "https://playground.learnqa.ru/api/user/",
            this.header,
            this.cookie,
            userId);

    System.out.println(responseUserData.asString());

    Assertions.assertJsonByName(responseUserData, "firstName", newName);
  }

  @Test
  @Description("This test checks the positive case of editing user data no aut")
  @DisplayName("Test editing user data no auth")
  public void editJustCreatedNoAuthTest() {
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

    //EDIT
    String newName = "New name two";
    Map<String, String> editData = new HashMap<>();
    editData.put("firstName", newName);

    Response responseEditUser = apiCoreRequests.makePostRequestEditUser(
            "https://playground.learnqa.ru/api/user/",
            editData,
            userId);

    //GET
    Response responseUserData = apiCoreRequests.makeGetRequestWithId(
            "https://playground.learnqa.ru/api/user/",
            this.header,
            this.cookie,
            userId);

    System.out.println(responseUserData.asString());

    Assertions.assertJsonNotName(responseUserData, "firstName", newName);
  }

  @Test
  @Description("This test change the user's email, being authorized by the same user, to a new email without a symbol @")
  @DisplayName("Test editing user data new email without a symbol @")
  public void editMailCreatedTest() {
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
            .makePostRequest("https://playground.learnqa.ru/api/user/login",
                    authData);

    this.cookie = this.getCookie(responseGetAuth, "auth_sid");
    this.header = this.getHeader(responseGetAuth, "x-csrf-token");

    //EDIT
    String newMail = "vinkotovmail.ru";
    Map<String, String> editData = new HashMap<>();
    editData.put("email", newMail);

    Response responseEditUser = apiCoreRequests.makePostRequestEditUser(
            "https://playground.learnqa.ru/api/user/",
            this.header,
            this.cookie,
            editData,
            userId);

    //GET
    Response responseUserData = apiCoreRequests.makeGetRequestWithId(
            "https://playground.learnqa.ru/api/user/",
            this.header,
            this.cookie,
            userId);

    System.out.println(responseUserData.asString());

    Assertions.assertJsonNotName(responseUserData, "email", newMail);
  }


  @Test
  @Description("This test change the user's firstName, being authorized by the same user, to a very short value of one character")
  @DisplayName("Test editing user data very short value of one character")
  public void editNameCreatedTest() {
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
            .makePostRequest("https://playground.learnqa.ru/api/user/login",
                    authData);

    this.cookie = this.getCookie(responseGetAuth, "auth_sid");
    this.header = this.getHeader(responseGetAuth, "x-csrf-token");

    //EDIT
    String newName = "v";
    Map<String, String> editData = new HashMap<>();
    editData.put("firstName", newName);

    Response responseEditUser = apiCoreRequests.makePostRequestEditUser(
            "https://playground.learnqa.ru/api/user/",
            this.header,
            this.cookie,
            editData,
            userId);

    //GET
    Response responseUserData = apiCoreRequests.makeGetRequestWithId(
            "https://playground.learnqa.ru/api/user/",
            this.header,
            this.cookie,
            userId);

    System.out.println(responseUserData.asString());

    Assertions.assertJsonNotName(responseUserData, "firstName", newName);
  }


}
