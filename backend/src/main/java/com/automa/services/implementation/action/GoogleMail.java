package com.automa.services.implementation.action;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Properties;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestClient;
import com.automa.services.ApiHelperService;
import com.automa.utils.ServiceContext;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import jakarta.mail.Session;
import jakarta.mail.Message.RecipientType;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
@Validated
public class GoogleMail {

    private final ApiHelperService apiHelperService = new ApiHelperService();

    private static MimeMessage createEmail(String to, String subject, String bodyText) {
        try {
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);

            MimeMessage email = new MimeMessage(session);
            email.addRecipient(RecipientType.TO, new InternetAddress(to));
            email.setSubject(subject);
            email.setText(bodyText);

            return email;
        } catch (Exception e) {
            return null;
        }
    }

    private static byte[] emailToBytes(MimeMessage email) {
        try {
            try (var outputStream = new ByteArrayOutputStream()) {
                email.writeTo(outputStream);
                return outputStream.toByteArray();
            }
        } catch (Exception e) {
            return null;
        }
    }

    public HashMap<String, String> sendEmail() {
        try {
            ServiceContext.setGoogleAccessToken("");
            RestClient restClient = apiHelperService.buildClient();
            String apiUrl = "https://gmail.googleapis.com/gmail/v1/users/me/messages/send";

            MimeMessage email = createEmail("abc@gmail.com", "Hi", "Nothing");
            String encodedEmail = Base64.getUrlEncoder().encodeToString(emailToBytes(email));
            String jsonPayload = "{ \"raw\": \"" + encodedEmail + "\" }";

            String responseBody = restClient
                    .method(HttpMethod.POST)
                    .uri(apiUrl)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + ServiceContext.getGoogleAccessToken())
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(jsonPayload)
                    .retrieve()
                    .body(String.class);

            JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
            JsonArray labelIds = jsonResponse.getAsJsonArray("labelIds");

            String isSend = "false";

            if (labelIds != null) {
                for (int i = 0; i < labelIds.size(); i++) {
                    if ("SENT".equals(labelIds.get(i).getAsString())) {
                        isSend = "true";
                        break;
                    }
                }
            }

            HashMap<String, String> responseMap = new HashMap<>();

            responseMap.put("sent", isSend);

            return responseMap;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        } finally {
            ServiceContext.removeGoogleAccessToken();
        }
    }

}
