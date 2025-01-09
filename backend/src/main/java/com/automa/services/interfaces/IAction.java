package com.automa.services.interfaces;

import java.util.HashMap;

public interface IAction {
    HashMap<String, Object> getAllSpreadSheets();

    HashMap<String, Object> getAllSheets(String id);
}
