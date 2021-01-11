package io.harness.ui.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * author: shaswat.deep
 */
public class Driver {
  WebDriver driver;

  public Driver() {}

  public Driver(WebDriver driver) {
    PageFactory.initElements(driver, this);
    this.driver = driver;
  }

  private static ThreadLocal<WebDriver> webDriver = new ThreadLocal<WebDriver>();

  public static WebDriver getDriver() {
    return webDriver.get();
  }

  public static void setWebDriver(WebDriver driver) {
    webDriver.set(driver);
  }

  protected void waitForElement(WebElement element) {
    WebDriverWait wait = new WebDriverWait(driver, 120);
    wait.until(ExpectedConditions.visibilityOf(element));
  }

  protected void smallExplicitWait(WebElement element) {
    WebDriverWait wait = new WebDriverWait(driver, 20);
    wait.until(ExpectedConditions.visibilityOf(element));
  }
}