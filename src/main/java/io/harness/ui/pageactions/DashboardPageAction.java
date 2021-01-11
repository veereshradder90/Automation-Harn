package io.harness.ui.pageactions;

import io.harness.ui.pageobjects.DashboardPageObject;
import org.openqa.selenium.WebDriver;

/**
 * author: shaswat.deep
 */
public class DashboardPageAction extends DashboardPageObject {
  public DashboardPageAction(WebDriver driver) {
    super(driver);
  }

  public void clickOnSetup(){
    waitForElement(setupButton);
    setupButton.click();
    waitForElement(harnessDelegates);
  }
}
