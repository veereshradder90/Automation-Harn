package io.harness.rest.helper.pipelines;

/* Created by sonypriyadarshini
on 13/08/20 */

public class PipelineConstants {

    public static final String REQUEST_JSON_PIPELINE_EDIT =
            "/src/main/resources/common/pipelines/editPipeline.json";
    public static final String REQUEST_JSON_PIPELINE_HARNESS_APPROVAL_STAGE =
            "/src/main/resources/common/pipelines/approvalStagePipeline/harnessApprovalStage.json";

    public static final String PIPELINE_CREATION_URI = "/pipelines";
    public static final String PIPELINE_EDIT_URI = "/pipelines/{pipelineId}";
    public static final String PIPELINE_CLONE_URI = "/pipelines/{pipelineId}/clone";

}
