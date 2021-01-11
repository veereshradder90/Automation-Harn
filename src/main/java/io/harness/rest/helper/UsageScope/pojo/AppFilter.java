package io.harness.rest.helper.UsageScope.pojo;

import java.util.List;

public class AppFilter {
  private String type;

  private List<String> ids;

  private String filterType;

  public void setType(String type) {
    this.type = type;
  }
  public String getType() {
    return this.type;
  }
  public void setIds(List<String> ids) {
    this.ids = ids;
  }
  public List<String> getIds() {
    return this.ids;
  }
  public void setFilterType(String filterType) {
    this.filterType = filterType;
  }
  public String getFilterType() {
    return this.filterType;
  }
}
