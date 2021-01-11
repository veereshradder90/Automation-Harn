package io.harness.rest.helper.UsageScope.pojo;

public class AppEnvRestrictions {
  private AppFilter appFilter;

  private EnvFilter envFilter;

  public void setAppFilter(AppFilter appFilter) {
    this.appFilter = appFilter;
  }
  public AppFilter getAppFilter() {
    return this.appFilter;
  }
  public void setEnvFilter(EnvFilter envFilter) {
    this.envFilter = envFilter;
  }
  public EnvFilter getEnvFilter() {
    return this.envFilter;
  }
}
