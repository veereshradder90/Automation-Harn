package io.harness.test.rest.devx;

import io.harness.rest.helper.dxaudittrails.AuditTrailsHelper;
import io.harness.test.rest.base.AbstractTest;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class AuditTrailTests extends AbstractTest {

    AuditTrailsHelper auditTrailsHelper = new AuditTrailsHelper();

    @Test
    public void testFetchAuditWithoutFilters() throws IOException {
        JsonPath appResponse = auditTrailsHelper.FetchAuditWithoutFilter();
        List<String> jsonResponse = appResponse.getList("data.audits.nodes");
        System.out.println("Length is :"+jsonResponse.size());
        for (int i = 0 ; i < jsonResponse.size();i++){
            Assert.assertTrue(appResponse.getString("data.audits.nodes["+i+"].id")!=null); ;
        }
    }

    @Test
    public void testFetchAuditWithSpecificAuditTrailFilter() throws IOException {
        JsonPath appResponse = auditTrailsHelper.FetchAuditSpecificTimeRange();
        List<String> jsonResponse = appResponse.getList("data.audits.nodes");
        System.out.println("Length is :"+jsonResponse.size());
        for (int i = 0 ; i < jsonResponse.size();i++){
            Assert.assertTrue(appResponse.getString("data.audits.nodes["+i+"].id")!=null); ;
        }
    }

    @Test
    public void testFetchAuditWithRelativeTime() throws IOException {
        JsonPath appResponse = auditTrailsHelper.FetchAuditRelativeTime();
        List<String> jsonResponse = appResponse.getList("data.audits.nodes");
        System.out.println("Length is :"+jsonResponse.size());
        for (int i = 0 ; i < jsonResponse.size();i++){
            Assert.assertTrue(appResponse.getString("data.audits.nodes["+i+"].id")!=null); ;
        }
    }


    @Test
    public void testFetchAuditWithYAMLChanges() throws IOException {
        JsonPath appResponse = auditTrailsHelper.FetchAuditYamlChangeSet();
        List<String> jsonResponse = appResponse.getList("data.auditChangeContent.nodes");
        System.out.println("Length is :"+jsonResponse.size());
        Assert.assertTrue(!appResponse.getString("data.auditChangeContent.nodes.resourceId").isEmpty());
        Assert.assertTrue(!appResponse.getString("data.auditChangeContent.nodes.changeSetId").isEmpty());
        Assert.assertTrue(!appResponse.getString("data.auditChangeContent.nodes.oldYaml").isEmpty());
    }

    @Test
    public void testFetchAuditWithChangeSetIdAndResourceId() throws IOException {
        JsonPath appResponse = auditTrailsHelper.FetchAuditYamlChangeSetAndResourceId();
        List<String> jsonResponse = appResponse.getList("data.auditChangeContent.nodes");
        System.out.println("Length is :"+jsonResponse.size());
        Assert.assertTrue(!appResponse.getString("data.auditChangeContent.nodes.resourceId").isEmpty());
        Assert.assertTrue(!appResponse.getString("data.auditChangeContent.nodes.changeSetId").isEmpty());
        Assert.assertTrue(!appResponse.getString("data.auditChangeContent.nodes.oldYaml").isEmpty());
    }

    @Test
    public void testFetchAuditCombinedQuery() throws IOException {
        JsonPath appResponse = auditTrailsHelper.FetchAuditCombinedQuery();
        List<String> jsonResponse = appResponse.getList("data.auditChangeContent.nodes");
        System.out.println("Length is :"+jsonResponse.size());
        Assert.assertTrue(!appResponse.getString("data.auditChangeContent.nodes.resourceId").isEmpty());
        Assert.assertTrue(!appResponse.getString("data.auditChangeContent.nodes.changeSetId").isEmpty());
        Assert.assertTrue(!appResponse.getString("data.auditChangeContent.nodes.oldYaml").isEmpty());
    }
}
