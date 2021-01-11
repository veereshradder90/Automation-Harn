package io.harness.test.ui.base;

import io.harness.ui.core.Base;
import org.testng.annotations.BeforeSuite;

/**
 * author: shaswat.deep
 */
public class AbstractTest extends Base {
  @BeforeSuite
  public void setup() {
    String defaultAccount = configPropertis.getConfig("DEFAULT_ACCOUNT");
  }
}
