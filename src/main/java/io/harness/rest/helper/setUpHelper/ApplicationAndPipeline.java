package io.harness.rest.helper.setUpHelper;

/* Created by sonypriyadarshini
on 13/08/20 */

import io.harness.rest.core.CoreUtils;
import io.harness.rest.helper.CommonHelper;
import io.harness.rest.helper.applications.ApplicationHelper;
import io.harness.rest.helper.pipelines.PipelineHelper;
import io.restassured.path.json.JsonPath;

public class ApplicationAndPipeline extends CoreUtils {

    PipelineHelper pipelineHelper=new PipelineHelper();
    ApplicationHelper applicationHelper=new ApplicationHelper();
    CommonHelper commonHelper=new CommonHelper();

    public JsonPath createApplicationAndPipeline(String appName, String pipelineName) {
        //create app, Build WF, remove install step from build WF
        JsonPath appResponse = applicationHelper.createApplication(appName);
        JsonPath pipelineResponse=pipelineHelper.createPipeline(pipelineName,appResponse.getString("resource.appId"));
        return  pipelineResponse;
    }

    public JsonPath createApplicationAndPipeline() {
        //create app, Build WF, remove install step from build WF
        JsonPath appResponse = applicationHelper.createApplication(commonHelper.createRandomName("CDC-Automation-"));
        JsonPath pipelineResponse=pipelineHelper.createPipeline(commonHelper.createRandomName("CDC-Auto-"),
                appResponse.getString("resource.appId"));
        return  pipelineResponse;
    }
}
