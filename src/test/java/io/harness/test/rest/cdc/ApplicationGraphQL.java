package io.harness.test.rest.cdc;

import io.harness.rest.helper.dxapplicationsgraphql.GraphQLApplicationHelper;
import io.harness.test.rest.base.AbstractTest;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class ApplicationGraphQL extends AbstractTest {
    GraphQLApplicationHelper graphQLApplicationHelper = new GraphQLApplicationHelper();

    @Test (description="Verify Application List By Filter Tags with AppIds and tags ")
    public void testApplicationListByFilterTagsApi() throws  IOException{
        JsonPath appResponse = graphQLApplicationHelper.listApplicationsByFilters();
        Assert.assertEquals(appResponse.getList("data.applications.nodes").size(), 1);
        Assert.assertEquals(appResponse.getString("data.applications.nodes[0].name"), "juhi-exhaustive-app");
        Assert.assertTrue(!appResponse.getString("data.applications.nodes[0].id").isEmpty());
    }

    @Test (description="Verify Application List By Filter Tags should not list the Applications for which user doesn't have access to ")
    public void testApplicationListByFilterTagsRBACApi() throws  IOException{
        JsonPath appResponse = graphQLApplicationHelper.listApplicationsByFiltersRBAC();
        Assert.assertEquals(appResponse.getList("data.applications.nodes").size(), 0);
    }
}
