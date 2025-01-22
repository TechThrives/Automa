package com.automa.services.implementation;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.automa.dto.action.ActionRequestResponse;
import com.automa.entity.ApplicationUser;
import com.automa.services.implementation.core.WorkflowRunner;
import com.automa.services.implementation.core.spreadsheets.GoogleSheets;
import com.automa.services.interfaces.IAction;
import com.automa.utils.ContextUtils;
import com.automa.utils.ServiceContext;

@Service
@Validated
public class ActionService implements IAction {

    private final ApplicationUserService applicationUserService;
    private final GoogleSheets googleSheets;
    private final WorkflowRunner workflowRunner;

    public ActionService(ApplicationUserService applicationUserService, GoogleSheets googleSheets,
            WorkflowRunner workflowRunner) {
        this.applicationUserService = applicationUserService;
        this.googleSheets = googleSheets;
        this.workflowRunner = workflowRunner;
    }

    @Override
    public ArrayList<HashMap<String, Object>> runAction(ActionRequestResponse actionRequestResponse) {
        return workflowRunner.runAction(actionRequestResponse);
    }

    @Override
    public HashMap<String, Object> getAllSpreadSheets() {
        ApplicationUser user = applicationUserService.findByEmail(ContextUtils.getUsername());
        ServiceContext.setGoogleCredential(user.getGoogleCredential());
        HashMap<String, Object> sheets = googleSheets.getAllSpreadSheets();
        ServiceContext.clearContext();
        return sheets;
    }

    @Override
    public HashMap<String, Object> getAllSheets(String id) {
        ApplicationUser user = applicationUserService.findByEmail(ContextUtils.getUsername());
        ServiceContext.setGoogleCredential(user.getGoogleCredential());
        HashMap<String, Object> sheets = googleSheets.getAllSheets(id);
        ServiceContext.clearContext();
        return sheets;
    }

}
