package io.harness.rest.helper.UsageScope.pojo;

import java.util.ArrayList;
import java.util.List;

public class EnvFilter {
  private String type;

  private List<String> ids;

  private List<String> filterTypes;

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
  public void setFilterTypes(List<String> filterTypes) {
    this.filterTypes = filterTypes;
  }
  public List<String> getFilterTypes() {
    return this.filterTypes;
  }
}
