package io.harness.ui.pageactions;

import io.harness.ui.pageobjects.LoginPageObject;
import io.harness.ui.utils.Driver;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 * author: shaswat.deep
 */
public class LoginPageAction extends LoginPageObject {
  public LoginPageAction(WebDriver driver) {
    super(driver);
  }
  public void login(String username, String password) {
    PageFactory.initElements(Driver.getDriver(), this);
    try {
      usernameField.clear();
      usernameField.sendKeys(username);
      try {
        nextButton.click();
      } catch (NoSuchElementException e) {
        e.printStackTrace();
      }
      waitForElement(passwordField);
      passwordField.clear();
      passwordField.sendKeys(password);
      submitButton.click();
      waitForElement(setupButton);
    } catch (StaleElementReferenceException e) {
      login(username, password);
    }
  }

  public String getCurrentUrl() {
    return getDriver().getCurrentUrl();
  }
}
