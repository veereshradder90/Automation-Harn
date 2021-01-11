package io.harness.rest.helper;

import com.jayway.jsonpath.ReadContext;
import io.restassured.response.ValidatableResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import com.jayway.jsonpath.JsonPath;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class CommonHelper {
  JsonPath responseJsonSections = null;

  public String createRandomName(String prefix, int count) {
    String result;
    result = prefix + RandomStringUtils.randomAlphanumeric(count);
    log.info("Random generated string is " + result);
    return result;
  }

  public String createRandomName(String prefix) {
    return createRandomName(prefix, 4);
  }

  public List<String> getValueListFromMap(Map<String, String> set) {
    return set.values().stream().collect(Collectors.toList());
  }

  public List<Object> getKeyListFromMap(Map<String, String> set) {
    return set.keySet().stream().collect(Collectors.toList());
  }

  public String parseResponse(ValidatableResponse response, String path){
    ReadContext readContext= responseJsonSections.parse(response.extract().body().asString());
    if(readContext.read(path).toString().equals("[]"))
      return "";
    else
      return readContext.read(path).toString();
  }
}
