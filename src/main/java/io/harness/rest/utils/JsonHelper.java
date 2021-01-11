package io.harness.rest.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Map;

public class JsonHelper {
  public JsonArray getJsonArrayForCustomSecretManager(Map<String, String> hashMap) {
    JsonArray array = new JsonArray();
    for (String key : hashMap.keySet()) {
      JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty("name", key);
      jsonObject.addProperty("value", hashMap.get(key));
      array.add(jsonObject);
    }
    return array;
  }
}
