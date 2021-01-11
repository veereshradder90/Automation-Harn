package io.harness.rest.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileNotFoundException;
import java.io.FileReader;
public class JsonFileReader {

    public JsonObject readJSONFiles(String fileName) throws FileNotFoundException {

        String filePath = System.getProperty("user.dir") +fileName;
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(new FileReader(filePath));
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        return jsonObject;
    }
}
