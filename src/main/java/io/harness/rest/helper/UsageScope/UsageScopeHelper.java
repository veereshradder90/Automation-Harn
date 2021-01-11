package io.harness.rest.helper.UsageScope;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import io.harness.rest.helper.UsageScope.pojo.AppEnvRestrictions;
import io.harness.rest.helper.UsageScope.pojo.AppFilter;
import io.harness.rest.helper.UsageScope.pojo.EnvFilter;
import io.harness.rest.helper.UsageScope.pojo.UsageRestrictions;

import java.util.ArrayList;
import java.util.List;

public class UsageScopeHelper {
  public JsonElement getDefaultUsageScope() {
    AppEnvRestrictions appEnvRestrictions = new AppEnvRestrictions();
    appEnvRestrictions.setAppFilter(getAllAppFilter());
    appEnvRestrictions.setEnvFilter(getAllProdEnvironmentFilter());

    AppEnvRestrictions appEnvRestrictions2 = new AppEnvRestrictions();
    appEnvRestrictions2.setAppFilter(getAllAppFilter());
    appEnvRestrictions2.setEnvFilter(getAllNonProdEnvironmentFilter());

    List<AppEnvRestrictions> appEnvList = new ArrayList<AppEnvRestrictions>();
    appEnvList.add(appEnvRestrictions);
    appEnvList.add(appEnvRestrictions2);
    UsageRestrictions usageRestrictions = new UsageRestrictions();
    usageRestrictions.setAppEnvRestrictions(appEnvList);

    JsonElement jsonElement = new Gson().toJsonTree(usageRestrictions);
    return jsonElement;
  }

  private AppFilter getAllAppFilter() {
    return getApplicationFilter(null);
  }

  private AppFilter getApplicationFilter(List<String> appIds) {
    AppFilter appFilter = new AppFilter();
    appFilter.setType("GenericEntityFilter");
    if (appIds == null) {
      appFilter.setFilterType("ALL");
    } else {
      appFilter.setFilterType("SELECTED");
    }
    appFilter.setIds(appIds);

    return appFilter;
  }

  private EnvFilter getAllProdEnvironmentFilter() {
    return getEnvironmentFilter(null, "PROD");
  }

  private EnvFilter getAllNonProdEnvironmentFilter() {
    return getEnvironmentFilter(null, "NON_PROD");
  }

  private EnvFilter getEnvironmentFilter(List<String> envIds, String envType) {
    EnvFilter envFilter = new EnvFilter();
    envFilter.setType("EnvFilter");
    envFilter.setIds(envIds);
    ArrayList<String> envTypeFilter = new ArrayList<String>();
    envTypeFilter.add(envType);
    envFilter.setFilterTypes(envTypeFilter);
    return envFilter;
  }

  public JsonElement getSpecificApplicationUsageScope(List<String> appIds, String envType) {
    AppEnvRestrictions prodEnvRestrictions = new AppEnvRestrictions();
    if (envType.equals("ALL") || envType.equals("PROD")) {
      prodEnvRestrictions.setAppFilter(getApplicationFilter(appIds));
      prodEnvRestrictions.setEnvFilter(getAllProdEnvironmentFilter());
    }
    AppEnvRestrictions nonProdEnvRestrictions = new AppEnvRestrictions();
    if (envType.equals("ALL") || envType.equals("NON_PROD")) {
      nonProdEnvRestrictions.setAppFilter(getApplicationFilter(appIds));
      nonProdEnvRestrictions.setEnvFilter(getAllNonProdEnvironmentFilter());
    }
    List<AppEnvRestrictions> appEnvList = new ArrayList<AppEnvRestrictions>();
    if (envType.equals("ALL")) {
      appEnvList.add(prodEnvRestrictions);
      appEnvList.add(nonProdEnvRestrictions);
    } else if (envType.equals("NON_PROD")) {
      appEnvList.add(nonProdEnvRestrictions);
    } else if (envType.equals("PROD")) {
      appEnvList.add(prodEnvRestrictions);
    }
    UsageRestrictions usageRestrictions = new UsageRestrictions();
    usageRestrictions.setAppEnvRestrictions(appEnvList);
    JsonElement jsonElement = new Gson().toJsonTree(usageRestrictions);
    return jsonElement;
  }
}
