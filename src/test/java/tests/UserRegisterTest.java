package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

@Epic("Registration cases")
@Feature("Registration")
public class UserRegisterTest extends BaseTestCase {

  private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

  @Test
  @Description("This test checks the positive case of an existing mail")
  @DisplayName("Test user with existing email")
  public void testCreateUserWithExistingEmail() {
    String email = "vinkotov@mail.ru";

    Map<String, String> userData = new HashMap<>();
    userData.put("email", email);
    userData = DataGenerator.getRegistrationData(userData);

    Response responseCreateAuth = apiCoreRequests.makePostRequest(
            "https://playground.learnqa.ru/api/user/",
            userData
    );

    Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
    Assertions.assertResponseTextEquals(responseCreateAuth, "Users with email '" + email + "' already exists");
  }

  @Test
  @Description("This test checks positive case of registration")
  @DisplayName("Test positive registration user")
  public void testCreateUserSuccessfully() {
    Map<String, String> userData = DataGenerator.getRegistrationData();

    Response responseCreateAuth = apiCoreRequests.makePostRequest(
            "https://playground.learnqa.ru/api/user/",
            userData);
    Assertions.assertResponseCodeEquals(responseCreateAuth, 200);
    Assertions.assertJsonHasField(responseCreateAuth, "id");
  }

  @Test
  @Description("This test checks the creation of a user with an incorrect email")
  @DisplayName("Invalid email format")
  public void testCreateUserWithWrongEmail() {
    String email = "vinkotovmail.ru";
    Map<String, String> userData = new HashMap<>();
    userData.put("email", email);
    userData = DataGenerator.getRegistrationData(userData);

    Response responseCreateAuth = apiCoreRequests
            .makePostRequestCreateUser(
                    "https://playground.learnqa.ru/api/user/",
                    userData);

    Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
    Assertions.assertResponseTextEquals(responseCreateAuth, "Invalid email format");
  }

  @ParameterizedTest
  @ValueSource(strings = {"email", "password", "username", "firstName", "lastName"})
  @Description("This test checks the creation of a user without specifying one of the fields")
  @DisplayName("The following required params are missed: ")
  public void testCreateUserWithOutFields(String field) {
    Map<String, String> userData;
    userData = DataGenerator.getRegistrationData();
    userData.remove(field);

    Response responseCreateAuth = apiCoreRequests
            .makePostRequestCreateUser(
                    "https://playground.learnqa.ru/api/user/",
                    userData);

    Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
    Assertions.assertResponseTextEquals(responseCreateAuth, "The following required params are missed: " + field);
  }

  @Test
  @Description("This test verifies the creation of a user with a very short name of one character")
  @DisplayName("Creation of a user with a very short name of one character")
  public void testCreateUserWithShortName() {
    String firstName = "v";
    Map<String, String> userData = new HashMap<>();
    userData.put("firstName", firstName);
    userData = DataGenerator.getRegistrationData(userData);

    Response responseCreateAuth = apiCoreRequests
            .makePostRequestCreateUser(
                    "https://playground.learnqa.ru/api/user/",
                    userData);

    Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
    Assertions.assertResponseTextEquals(responseCreateAuth, "The value of 'firstName' field is too short");
  }

  @Test
  @Description("This test checks the creation of a user with a very long name - longer than 250 characters")
  @DisplayName("Creation of a user with a very long name")
  public void testCreateUserWithLongName() {
    String longName = DataGenerator.getLongString();
    Map<String, String> userData = new HashMap<>();
    userData.put("firstName", longName);
    userData = DataGenerator.getRegistrationData(userData);

    Response responseCreateAuth = apiCoreRequests
            .makePostRequestCreateUser(
                    "https://playground.learnqa.ru/api/user/",
                    userData);

    responseCreateAuth.asString();

    Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
    Assertions.assertResponseTextEquals(responseCreateAuth, "The value of 'firstName' field is too long");
  }
}
