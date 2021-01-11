package io.harness.rest.helper.cvnextgen.onboarding.appdynamics.metricpackpojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * author: shaswat.deep
 */
public class MetricPacks {
  @SerializedName("accountId") @Expose private String accountId;

  @SerializedName("projectIdentifier") @Expose private String projectIdentifier;

  @SerializedName("dataSourceType") @Expose private String dataSourceType;

  @SerializedName("identifier") @Expose private String identifier;

  @SerializedName("category") @Expose private String category;

  @SerializedName("metrics") @Expose private List<Metrics> metricsList = new ArrayList<>();

  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  public String getProjectIdentifier() {
    return projectIdentifier;
  }

  public void setProjectIdentifier(String projectIdentifier) {
    this.projectIdentifier = projectIdentifier;
  }

  public String getDataSourceType() {
    return dataSourceType;
  }

  public void setDataSourceType(String dataSourceType) {
    this.dataSourceType = dataSourceType;
  }

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public List<Metrics> getMetricsList() {
    return metricsList;
  }

  public void setMetricsList(List<Metrics> metricsList) {
    this.metricsList = metricsList;
  }
}
