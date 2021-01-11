package io.harness.rest.helper.cvnextgen.onboarding.appdynamics.metricpackpojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * author: shaswat.deep
 */
public class Thresholds {
  @SerializedName("accountId") @Expose private String accountId;

  @SerializedName("projectIdentifier") @Expose private String projectIdentifier;

  @SerializedName("metricName") @Expose private String metricName;

  @SerializedName("metricType") @Expose private String metricType;

  @SerializedName("metricGroupName") @Expose private String metricGroupName;

  @SerializedName("action") @Expose private String action;

  @SerializedName("criteria") @Expose private Criteria criteria;

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

  public String getMetricName() {
    return metricName;
  }

  public void setMetricName(String metricName) {
    this.metricName = metricName;
  }

  public String getMetricType() {
    return metricType;
  }

  public void setMetricType(String metricType) {
    this.metricType = metricType;
  }

  public String getMetricGroupName() {
    return metricGroupName;
  }

  public void setMetricGroupName(String metricGroupName) {
    this.metricGroupName = metricGroupName;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public Criteria getCriteria() {
    return criteria;
  }

  public void setCriteria(Criteria criteria) {
    this.criteria = criteria;
  }
}