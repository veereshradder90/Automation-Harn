package io.harness.rest.helper.cvnextgen.onboarding.appdynamics.metricpackpojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * author: shaswat.deep
 */
public class Criteria {
  @SerializedName("type") @Expose private String type;

  @SerializedName("action") @Expose private String action;

  @SerializedName("occurrenceCount") @Expose private int occurrenceCount;

  @SerializedName("criteria") @Expose private String criteria;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public int getOccurrenceCount() {
    return occurrenceCount;
  }

  public void setOccurrenceCount(int occurrenceCount) {
    this.occurrenceCount = occurrenceCount;
  }

  public String getCriteria() {
    return criteria;
  }

  public void setCriteria(String criteria) {
    this.criteria = criteria;
  }
}
