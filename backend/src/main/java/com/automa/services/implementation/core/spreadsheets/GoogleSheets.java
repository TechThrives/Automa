package com.automa.services.implementation.core.spreadsheets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.automa.services.implementation.core.ApiHelperService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
@Validated
public class GoogleSheets {

    private final ApiHelperService apiHelperService;

    GoogleSheets(ApiHelperService apiHelperService) {
        this.apiHelperService = apiHelperService;
    }

    public HashMap<String, Object> getAllSpreadSheets() {
        try {
            String apiUrl = "https://www.googleapis.com/drive/v3/files?q=mimeType='application/vnd.google-apps.spreadsheet'";

            String responseBody = apiHelperService.executeWithAccessToken(apiUrl, HttpMethod.GET, "");

            JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();

            JsonArray files = jsonResponse.getAsJsonArray("files");

            List<HashMap<String, Object>> fileList = new ArrayList<>();

            for (int i = 0; i < files.size(); i++) {
                JsonObject file = files.get(i).getAsJsonObject();
                HashMap<String, Object> fileMap = new HashMap<>();
                fileMap.put("id", file.get("id").getAsString());
                fileMap.put("name", file.get("name").getAsString());
                fileMap.put("spreadsheetUrl",
                        "https://docs.google.com/spreadsheets/d/" + file.get("id").getAsString() + "/edit");
                fileList.add(fileMap);
            }

            HashMap<String, Object> output = new HashMap<>();
            output.put("files", fileList);

            return output;

        } catch (Exception e) {
            HashMap<String, Object> output = new HashMap<>();
            output.put("files", new ArrayList<>());

            return output;

        }
    }

    public HashMap<String, Object> getAllSheets(String id) {
        try {
            String apiUrl = "https://sheets.googleapis.com/v4/spreadsheets/" + id;

            String responseBody = apiHelperService.executeWithAccessToken(apiUrl, HttpMethod.GET, "");

            JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();

            JsonArray sheets = jsonResponse.getAsJsonArray("sheets");

            List<HashMap<String, Object>> sheetList = new ArrayList<>();

            for (int i = 0; i < sheets.size(); i++) {
                JsonObject sheet = sheets.get(i).getAsJsonObject();
                JsonObject properties = sheet.get("properties").getAsJsonObject();
                HashMap<String, Object> sheetMap = new HashMap<>();
                sheetMap.put("sheetId", properties.get("sheetId").getAsString());
                sheetMap.put("title", properties.get("title").getAsString());
                sheetMap.put("index", properties.get("index").getAsInt());
                sheetMap.put("sheetType", properties.get("sheetType").getAsString());
                JsonObject gridProperties = properties.get("gridProperties").getAsJsonObject();
                sheetMap.put("rowCount", gridProperties.get("rowCount").getAsInt());
                sheetMap.put("columnCount", gridProperties.get("columnCount").getAsInt());
                sheetList.add(sheetMap);
            }

            HashMap<String, Object> output = new HashMap<>();
            output.put("sheets", sheetList);

            return output;

        } catch (Exception e) {
            HashMap<String, Object> output = new HashMap<>();
            output.put("sheets", new ArrayList<>());

            return output;
        }
    }
}
