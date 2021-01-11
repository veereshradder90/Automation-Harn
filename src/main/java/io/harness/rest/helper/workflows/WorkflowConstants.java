package io.harness.rest.helper.workflows;

public class WorkflowConstants {
    public static final String REQUEST_JSON_WORKFLOW_CREATION =
            "/src/main/resources/common/workflows/workflowcreation.json";
    public static final String REQUEST_JSON_ADD_BUILD_WORKFLOW_STEP =
            "/src/main/resources/common/workflows/buildWorkflow/addOrEditWorkflowStep.json";
    public static final String REQUEST_JSON_WORKFLOW_DEPLOY =
            "/src/main/resources/common/workflows/workflowDeployment/workflowDeployment.json";
    public static final String REQUEST_JSON_WORKFLOW_TEMPLATISATION =
            "/src/main/resources/common/workflows/workflowtemplatisation.json";

    public static final String WORKFLOW_CREATION_URI = "/workflows";
    public static final String GET_WORKFLOW_DETAILS_URI = "/workflows/{workflowId}";
    public static final String GET_WORKFLOW_LIST = "/workflows";
    public static final String WORKFLOW_UPDATE_URI = "/workflows/{workflowId}/phases/{phaseId}";
    public static final String WORKFLOW_VARIABLES = "/executions/workflow-variables";
    public static final String WORKFLOW_DEPLOY = "/executions";
    public static final String WORKFLOW_INFRA_DEFINITIONS = "/infrastructure-definitions/list";
    public static final String WORKFLOW_TEMPLATISE_URI = "/workflows/{workflowId}/basic";

}
