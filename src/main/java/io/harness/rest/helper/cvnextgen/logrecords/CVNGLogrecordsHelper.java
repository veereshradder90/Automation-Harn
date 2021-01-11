package io.harness.rest.helper.cvnextgen.logrecords;

import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.harness.rest.helper.cvnextgen.CVNGConstants;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;

/**
 * author: shaswat.deep
 */
@Slf4j
public class CVNGLogrecordsHelper extends CoreUtils {
  GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();

  public Response postLogRecords(String body) {
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObjectNG();
    requestSpecification.body(body);
    log.info("Post call to make Log entry");
    Response response = genericRequestBuilder.postCall(requestSpecification, CVNGConstants.URI_CV_LOG_RECORDS);

    return response;
  }
}