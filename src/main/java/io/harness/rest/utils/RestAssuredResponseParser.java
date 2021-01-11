package io.harness.rest.utils;

import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestAssuredResponseParser {
  public ArrayList<HashMap> getResponseMap(Response response, String path) {
    ArrayList<HashMap> resource = response.jsonPath().getJsonObject(path);
    return resource;
  }

  public Map<String, String> getResponseMapForGivenKeys(
      Response response, String path, String keyForResponseMapKey, String keyForResponseMapValue) {
    ArrayList<HashMap> resource = response.jsonPath().getJsonObject(path);
    Map<String, String> resultMap = new HashMap<String, String>();
    for (HashMap map : resource) {
      resultMap.put(map.get(keyForResponseMapKey).toString(), map.get(keyForResponseMapValue).toString());
    }
    return resultMap;
  }

  public Map<String, String> getPropertiesForGivenKey(
      Response response, String path, String keyToMatch, String valueToMatch) {
    ArrayList<HashMap> resource = response.jsonPath().getJsonObject(path);
    Map<String, String> resultMap = new HashMap<String, String>();
    for (HashMap map : resource) {
      if (map.get(keyToMatch).toString().equals(valueToMatch)) {
        resultMap = map;
      }
    }
    return resultMap;
  }

  public boolean checkValueInListOfMap(List<Map<String, Object>> listOfMap, String key, Object value) {
    for (Map<String, Object> map : listOfMap) {
      if (map.get(key).equals(value)) {
        return true;
      }
    }
    return false;
  }

  public Map<String, Object> getMapOfValueFromList(List<Map<String, Object>> listOfMap, String key, Object value) {
    for (Map<String, Object> map : listOfMap) {
      if (map.get(key).equals(value)) {
        return map;
      }
    }
    return null;
  }
}
