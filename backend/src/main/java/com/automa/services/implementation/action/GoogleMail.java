package com.automa.services.implementation.action;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.automa.services.ApiHelperService;
import com.google.gson.JsonObject;
import jakarta.mail.Session;
import jakarta.mail.Message.RecipientType;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
@Validated
public class GoogleMail {

    private final ApiHelperService apiHelperService;

    GoogleMail(ApiHelperService apiHelperService) {
        this.apiHelperService = apiHelperService;
    }

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

    public Map<String, String> sendEmail(String accessToken) {
        MimeMessage email = createEmail("robertbennet1998@gmail.com", "Hi", "Nothing");
        String apiUrl = "https://gmail.googleapis.com/gmail/v1/users/me/messages/send";
        String encodedEmail = Base64.getUrlEncoder().encodeToString(emailToBytes(email));
        String jsonPayload = "{ \"raw\": \"" + encodedEmail + "\" }";

        JsonObject jsonResponse = apiHelperService.makeApiRequest(apiUrl, accessToken, "POST", jsonPayload);

        Map<String, String> responseMap = new HashMap<>();

        responseMap.put("sent", "true");

        return responseMap;
    }

}
