package com.automa.services.implementation.action;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.automa.services.ApiHelperService;
import com.google.gson.JsonObject;

@Service
@Validated
public class Youtube {

    private ApiHelperService apiHelperService;

    public Youtube(ApiHelperService apiHelperService) {
        this.apiHelperService = apiHelperService;
    }

    public Map<String, String> getYouTubeVideoInfo(String videoId, String accessToken) {
        String apiUrl = "https://www.googleapis.com/youtube/v3/videos?part=snippet,statistics&id=" + videoId;
        JsonObject jsonResponse = apiHelperService.makeApiRequest(apiUrl, accessToken, "GET", null);

        Map<String, String> responseMap = new HashMap<>();

        JsonObject videoDetails = jsonResponse.getAsJsonArray("items").get(0).getAsJsonObject();
        JsonObject snippet = videoDetails.getAsJsonObject("snippet");
        JsonObject statistics = videoDetails.getAsJsonObject("statistics");

        responseMap.put("title", snippet.get("title").getAsString());
        responseMap.put("description", snippet.get("description").getAsString());
        responseMap.put("viewCount", statistics.get("viewCount").getAsString());
        responseMap.put("likeCount", statistics.get("likeCount").getAsString());
        responseMap.put("commentCount", statistics.get("commentCount").getAsString());

        return responseMap;
    }
}
