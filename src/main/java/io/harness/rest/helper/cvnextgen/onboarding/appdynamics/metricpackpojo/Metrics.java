package io.harness.rest.helper.cvnextgen.onboarding.appdynamics.metricpackpojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * author: shaswat.deep
 */
public class Metrics {
  @SerializedName("name") @Expose private String name;

  @SerializedName("included") @Expose private boolean included;

  @SerializedName("thresholds") @Expose private List<Thresholds> thresholds = new ArrayList<Thresholds>();

  public String getMetricName() {
    return name;
  }

  public void setMetricName(String name) {
    this.name = name;
  }

  public boolean isIncluded() {
    return included;
  }

  public void setIncluded(boolean included) {
    this.included = included;
  }

  public List<Thresholds> getThresholds() {
    return thresholds;
  }

  public void setThresholds(List<Thresholds> thresholds) {
    this.thresholds = thresholds;
  }
}
