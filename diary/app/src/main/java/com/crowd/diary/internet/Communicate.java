package com.crowd.diary.internet;

/**
 * Created by leon on 17-12-12.
 */
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class Communicate {
    public void login(String username, String password) throws JSONException{
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("username", username);
        jsonObj.put("password", password);

    }


    public void signup(String username, String password) throws JSONException {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("username", username);
        jsonObj.put("password", password);
    }
}
