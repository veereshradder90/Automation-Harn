package io.harness.ui.listeners;

import static io.harness.ui.core.Base.configPropertis;

import io.harness.ui.utils.Driver;
import io.harness.ui.utils.DriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * author: shaswat.deep
 */
@Slf4j
public class Listener implements IInvokedMethodListener {
  @Override
  public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
    if (method.isTestMethod()) {
      String browserName = configPropertis.getConfig("DEFAULT_BROWSER");
      ;
      try {
        String baseUri = configPropertis.getConfig("BASE_URI");
        WebDriver driver = DriverManager.createInstance(browserName, baseUri, method.getTestMethod().getMethodName());
        log.info("Initializing webdriver session --> Thread ID: " + Thread.currentThread().getId());
        log.info("Running test --> " + method.getTestMethod().getMethodName());
        Driver.setWebDriver(driver);
      } catch (MalformedURLException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
    if (method.isTestMethod()) {
      WebDriver driver = Driver.getDriver();
      if (driver != null) {
        log.info("Closing webdriver session: " + Thread.currentThread().getId());
        driver.quit();
      }
    }
  }
}
