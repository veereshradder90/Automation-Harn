package io.harness.rest.helper.cvnextgen.onboarding.appdynamics;

/**
 * author: shaswat.deep
 */
public class CVNGAppDynamicsConstants {
  // Metric Packs Json paths
  public static final String REQUEST_JSON_CV_PERFORMANCE_METRIC_PACKS =
      "/src/main/resources/project/cvnextgen/metricpacks/cvPerformanceMetricPack.json";
  public static final String REQUEST_JSON_CV_QUALITY_METRIC_PACKS =
      "/src/main/resources/project/cvnextgen/metricpacks/cvQualityMetricPack.json";
  public static final String REQUEST_JSON_CV_RESOURCES_METRIC_PACKS =
      "/src/main/resources/project/cvnextgen/metricpacks/cvResourcesMetricPack.json";

  public static final String URI_CV_METRIC_PACK_METRICSPACKS = "/cv-nextgen/metric-pack";
  public static final String URI_CV_METRIC_PACK_THRESHOLDS = "/cv-nextgen/metric-pack/thresholds";
  public static final String URI_CV_APPS_LIST = "/api/appdynamics/applications";
  public static final String URI_CV_TIERS_LIST = "/api/appdynamics/tiers";
  public static final String URI_CV_METRIC_DATA = "/cv-nextgen/appdynamics/metric-data";
  public static final String URI_CV_3P_CALL_LOGS = "/api/activities/{metricName}/api-call-logs";
}
