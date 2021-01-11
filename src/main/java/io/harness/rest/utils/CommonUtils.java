package io.harness.rest.utils;

import com.google.gson.JsonObject;
import java.util.HashMap;

public class CommonUtils {

    public String createRequestBody(JsonObject jsonObject, HashMap<String, String> additionalBody) {
        String bodyOfRequest = jsonObject.toString();
        if (additionalBody != null) {
            for (String key : additionalBody.keySet()) {
                if (additionalBody.get(key) != null && !additionalBody.get(key).trim().contentEquals("")) {
                    bodyOfRequest = bodyOfRequest.replaceFirst("[$]" + key, additionalBody.get(key));
                }
            }
        }
        return bodyOfRequest;
    }
}
