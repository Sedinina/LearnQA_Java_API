package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PasswordTest {

  public static WebDriver driver;
  public static WebDriverWait wait;


  public List<String> getElementNames(List<WebElement> elements) {
    List<String> names = new ArrayList<>();
    for (WebElement e : elements) {
      names.add(e.getText());
    }
    return names;
  }


  @Test
  public void passwordTest() {

    driver = new FirefoxDriver();
    driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS); //неявное ожидание
    wait = new WebDriverWait(driver, Duration.ofSeconds(20));

    driver.get("https://en.wikipedia.org/wiki/List_of_the_most_common_passwords");
    List<WebElement> passwordAll = driver.findElements(By.cssSelector("table.wikitable tbody td[align=left]"));
    List<String> pass = getElementNames(passwordAll);

    driver.quit();
    driver = null;

    for (String password : pass) {

      Map<String, String> params = new HashMap<>();
      params.put("login", "super_admin");
      params.put("password", password);

      Response responseLogin = RestAssured
              .given()
              .queryParams(params)
              .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
              .andReturn();

      Map<String, String> responseLoginCookies = responseLogin.getCookies();


      Response responseCookie = RestAssured
              .given()
              .cookies(responseLoginCookies)
              .post("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
              .andReturn();

      String status = responseCookie.getBody().asString();

      if (status.equals("You are authorized")) {
        System.out.println("Кука " + status + " Пароль " + password);
      }
    }
  }
}
