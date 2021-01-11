package io.harness.test.rest.ce;

import io.harness.rest.helper.cerecommendations.GraphQLRecommendationHelper;
import io.harness.test.rest.base.AbstractTest;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

@Test(groups = {"CE", "Recommendations", "QA"})
public class Recommendations extends AbstractTest {
    GraphQLRecommendationHelper graphQLRecommendationHelper = new GraphQLRecommendationHelper();

    @Test
    public void testOverViewPageRecommendationsListApi() throws IOException {
        JsonPath appResponse = graphQLRecommendationHelper.listAllRecommendations();
        Assert.assertTrue(!appResponse.getString("data.k8sWorkloadRecommendations.nodes").isEmpty());
    }

    @Test
    public void testWorkloadsRecommendationsApi() throws IOException {
        JsonPath appResponse = graphQLRecommendationHelper.listWorkloadRecommendations();
        Assert.assertTrue(!appResponse.getString("data.k8sWorkloadRecommendations.nodes").isEmpty());
        Assert.assertTrue(!appResponse.getString("data.k8sWorkloadRecommendations.nodes.containerRecommendations.current").isEmpty());
        Assert.assertTrue(!appResponse.getString("data.k8sWorkloadRecommendations.nodes.containerRecommendations.burstable").isEmpty());
        Assert.assertTrue(!appResponse.getString("data.k8sWorkloadRecommendations.nodes.containerRecommendations.guaranteed").isEmpty());
        Assert.assertTrue(appResponse.getString("data.k8sWorkloadRecommendations.nodes.workloadType").equalsIgnoreCase("[StatefulSet]"));
        Assert.assertTrue(!appResponse.getString("data.k8sWorkloadRecommendations.nodes.numDays").equals("0"));
    }

    @Test
    public void testBillingTrendStats() throws IOException {
        JsonPath appResponse = graphQLRecommendationHelper.billingTrendStatsValues();
        System.out.println(appResponse.getString("data.billingTrendStats.forecastCost.statsLabel"));
        Assert.assertTrue(!appResponse.getString("data.billingTrendStats.forecastCost.statsLabel").isEmpty());
    }
}

