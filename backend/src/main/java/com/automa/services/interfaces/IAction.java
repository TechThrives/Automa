package com.automa.services.interfaces;

import java.util.ArrayList;
import java.util.HashMap;

import com.automa.dto.action.ActionRequestResponse;

public interface IAction {
    ArrayList<HashMap<String, Object>> runAction(ActionRequestResponse actionRequestResponse);

    HashMap<String, Object> getAllSpreadSheets();

    HashMap<String, Object> getAllSheets(String id);
}
