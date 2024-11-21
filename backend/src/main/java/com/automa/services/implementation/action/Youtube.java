package com.automa.services.implementation.action;

import java.util.HashMap;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestClient;

import com.automa.services.ApiHelperService;
import com.automa.utils.ServiceContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
@Validated
public class Youtube {

    private ApiHelperService apiHelperService = new ApiHelperService();

    public HashMap<String, String> getYouTubeVideoInfo(String videoId) {
        try {
            ServiceContext.setGoogleAccessToken("");
            RestClient restClient = apiHelperService.buildClient();
            String apiUrl = "https://www.googleapis.com/youtube/v3/videos?part=snippet,statistics&id=" + videoId;

            String responseBody = restClient
                    .method(HttpMethod.GET)
                    .uri(apiUrl)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + ServiceContext.getGoogleAccessToken())
                    .retrieve()
                    .body(String.class);

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
        } finally {
            ServiceContext.removeGoogleAccessToken();
        }

    }
}
