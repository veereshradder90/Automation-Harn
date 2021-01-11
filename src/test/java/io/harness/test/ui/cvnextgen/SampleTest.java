package io.harness.test.ui.cvnextgen;

import io.harness.test.ui.base.AbstractTest;
import io.harness.ui.pageactions.DashboardPageAction;
import io.harness.ui.pageactions.LoginPageAction;
import io.harness.ui.utils.Driver;
import org.testng.annotations.Test;

/**
 * author: shaswat.deep
 */
public class SampleTest extends AbstractTest {

  @Test(enabled = true, priority = 0, description = "Sample Test for Login")
  public void testLoginLogout(){

    Driver.getDriver().manage().window().fullscreen();

    LoginPageAction loginPageAction = new LoginPageAction(Driver.getDriver());
    loginPageAction.login(defaultUser, defaultPassword);

    DashboardPageAction dashboardPageAction = new DashboardPageAction(Driver.getDriver());
    dashboardPageAction.clickOnSetup();
  }
}
