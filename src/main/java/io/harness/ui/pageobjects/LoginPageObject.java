package io.harness.ui.pageobjects;

import io.harness.ui.utils.Driver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * author: shaswat.deep
 */
public class LoginPageObject extends Driver {
  public LoginPageObject(WebDriver driver) {
    super(driver);
  }
  @FindBy(id = "root_login") protected WebElement usernameField;

  @FindBy(id = "root_password") protected WebElement passwordField;

  @FindBy(css = "#react-root > login-container > main > page-content > div > form > div:nth-child(2) > button")
  protected WebElement nextButton;

  @FindBy(css = "page-content > div > form > div:nth-child(3) > button") protected WebElement submitButton;

  @FindBy(css = "[data-name=nav-setup]") protected WebElement setupButton;
}
