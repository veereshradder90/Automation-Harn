package io.harness.ui.pageobjects;

import io.harness.ui.utils.Driver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * author: shaswat.deep
 */
public class DashboardPageObject extends Driver {
  public DashboardPageObject(WebDriver driver) {
    super(driver);
  }

  @FindBy(css = "[data-name=setup-as-code-link]") protected WebElement configAsCodeButton;

  @FindBy(css = "[data-tab-id='errors']") protected WebElement errorsTab;

  @FindBy(css = "[data-name='count']") protected WebElement appCount;

  @FindBy(css = "[data-name='no-data']") protected WebElement noApplicationText;

  @FindBy(css = "[data-name=nav-setup]") protected WebElement setupButton;

  @FindBy(xpath = "//*[text()='Harness Delegates']") protected WebElement harnessDelegates;

  @FindBy(xpath = "//*[text()='Template Library']") protected WebElement templateLibrary;

  @FindBy(css = "a[href$='connectors']") protected WebElement connectorsHrefLink;

  @FindBy(css = "[data-name=SourceRepoProviders-title]") protected WebElement sourceRepoProviderButton;

  @FindBy(css = "nav.AccountsMenu---main---8DPIG > div > a") protected WebElement mainAccountButton;

  @FindBy(css = "nav.AccountsMenu---main---8DPIG > div > div > a:nth-child(2)") protected WebElement switchAccountButton;

  @FindBy(css = "[data-name=AutomationTwo]") protected WebElement accountTwoButton;

  @FindBy(xpath = "//*[text()='Add Application']") protected WebElement addApplicationButton;

  @FindBy(css = "[placeholder='Search Application']") protected WebElement applicationSearchBox;

  @FindBy(css = "[data-name='three-dot-menu-icon']") protected WebElement threeDotApp;

  @FindBy(css = "[data-name='Delete']") protected WebElement deleteIcon;

  @FindBy(css = "[data-name='confirm']") protected WebElement confirmDelete;

  @FindBy(css = "[data-name='btn-add']") protected WebElement addTemplates;
}
