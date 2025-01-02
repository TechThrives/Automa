package com.automa.services.implementation.core.youtube;

import java.util.HashMap;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.automa.services.implementation.core.ApiHelperService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
@Validated
public class Youtube {

    private ApiHelperService apiHelperService;

    public Youtube(ApiHelperService apiHelperService) {
        this.apiHelperService = apiHelperService;
    }

    public HashMap<String, String> getYouTubeVideoInfo(String videoId) {
        try {
            String apiUrl = "https://www.googleapis.com/youtube/v3/videos?part=snippet,statistics&id=" + videoId;

            String responseBody = apiHelperService.executeWithAccessToken(apiUrl, HttpMethod.GET, null);

            JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();

            if (jsonResponse.getAsJsonArray("items").size() < 1)
                throw new RuntimeException("Video Id not found");

            JsonObject videoDetails = jsonResponse.getAsJsonArray("items").get(0).getAsJsonObject();
            JsonObject snippet = videoDetails.getAsJsonObject("snippet");
            JsonObject statistics = videoDetails.getAsJsonObject("statistics");

            HashMap<String, String> responseMap = new HashMap<>();
            responseMap.put("title", snippet.get("title").getAsString());
            responseMap.put("description", snippet.get("description").getAsString());
            responseMap.put("viewCount", statistics.get("viewCount").getAsString());
            responseMap.put("likeCount", statistics.get("likeCount").getAsString());
            responseMap.put("commentCount", statistics.get("commentCount").getAsString());

            return responseMap;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

    }
}
