package io.harness.test.rest.cdc.cdcCleanUp;

import io.harness.rest.helper.applications.ApplicationHelper;
import io.harness.test.rest.base.AbstractTest;
import org.testng.annotations.Test;

public class CdcCleanUp extends AbstractTest {

    ApplicationHelper applicationHelper=new ApplicationHelper();

    @Test(groups = {"CDC_ONLY"})
    public void deleteAppsCreatedInCdcTest() {
        applicationHelper.searchAndDeleteApp("CDC-Automation");
    }

}
