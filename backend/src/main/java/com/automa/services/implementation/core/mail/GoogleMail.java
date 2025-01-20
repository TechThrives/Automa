package com.automa.services.implementation.core.mail;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Properties;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.automa.entity.action.Action;
import com.automa.services.implementation.core.ApiHelperService;
import com.automa.utils.WorkflowUtils;
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

    private final ApiHelperService apiHelperService;

    public GoogleMail(ApiHelperService apiHelperService) {
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

    public ArrayList<HashMap<String, Object>> sendMail(Action action,
            HashMap<String, ArrayList<HashMap<String, Object>>> workflowOutput) {
        ArrayList<HashMap<String, Object>> allData = WorkflowUtils.replaceVariableWithData(action.getData(),
                workflowOutput);
        ArrayList<HashMap<String, Object>> outputList = new ArrayList<>();

        System.out.println(allData);

        for (HashMap<String, Object> data : allData) {
            HashMap<String, Object> output = new HashMap<>();
            try {
                String apiUrl = "https://gmail.googleapis.com/gmail/v1/users/me/messages/send";

                MimeMessage email = createEmail(data.get("to").toString(), data.get("subject").toString(),
                        data.get("message").toString());
                String encodedEmail = Base64.getUrlEncoder().encodeToString(emailToBytes(email));
                String jsonPayload = "{ \"raw\": \"" + encodedEmail + "\" }";

                String responseBody = apiHelperService.executeWithAccessToken(apiUrl, HttpMethod.POST, jsonPayload);

                JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
                JsonArray labelIds = jsonResponse.getAsJsonArray("labelIds");

                Boolean isSend = false;

                if (labelIds != null) {
                    for (int i = 0; i < labelIds.size(); i++) {
                        if ("SENT".equals(labelIds.get(i).getAsString())) {
                            isSend = true;
                            break;
                        }
                    }
                }

                output.put("sent", isSend);

                outputList.add(output);

            } catch (Exception e) {
                System.out.println(e.getMessage());
                output.put("sent", false);
                outputList.add(output);
            }
        }
        return outputList;

    }

}
