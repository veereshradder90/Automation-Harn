package io.harness.rest.helper.UsageScope.pojo;

import java.util.List;

public class UsageRestrictions {
  private List<AppEnvRestrictions> appEnvRestrictions;

  public void setAppEnvRestrictions(List<AppEnvRestrictions> appEnvRestrictions) {
    this.appEnvRestrictions = appEnvRestrictions;
  }
  public List<AppEnvRestrictions> getAppEnvRestrictions() {
    return this.appEnvRestrictions;
  }
}
