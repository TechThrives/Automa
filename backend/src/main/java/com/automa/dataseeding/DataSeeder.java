package com.automa.dataseeding;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.automa.entity.action.ActionInfo;
import com.automa.repository.ActionInfoRepository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class DataSeeder implements CommandLineRunner {

    private final ActionInfoRepository actionInfoRepository;

    public DataSeeder(ActionInfoRepository actionInfoRepository) {
        this.actionInfoRepository = actionInfoRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!seedActionInfo()) {
            System.out.println("ActionInfo data already seeded or error occurred.");
        }
    }

    @Transactional
    public boolean seedActionInfo() {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<ActionInfo>> typeReference = new TypeReference<List<ActionInfo>>() {
        };

        InputStream inputStream = TypeReference.class.getResourceAsStream("/data/actionInfo.json");

        if (inputStream == null) {
            System.out.println("Error: Could not find actionInfo.json file.");
            return false;
        }

        try {
            List<ActionInfo> actionInfos = mapper.readValue(inputStream, typeReference);
            for (ActionInfo actionInfo : actionInfos) {

                actionInfo.setActionGroup(actionInfo.getActionType().getActionGroup());

                try {
                    actionInfoRepository.save(actionInfo);
                } catch (Exception e) {
                    handleDuplicateKeyError(e);
                }
            }
            return true;
        } catch (IOException e) {
            System.out.println("ActionInfo JSON file error: " + e.getMessage());
        }
        return false;
    }

    private void handleDuplicateKeyError(Exception e) {
        String regex = "Key \\((.*?)\\)=(.*?) already exists";
        String message = e.getMessage();

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(message);

        if (matcher.find()) {
            String key = matcher.group(1);
            String values = matcher.group(2);

            System.out.println("Duplicate Entry for Key (" + key + ") = (" + values + ").");
        } else {
            System.out.println("Duplicate entry error: " + e.getMessage());
        }
    }
}
