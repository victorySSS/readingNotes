package com.crowd.diary.internet;

/**
 * Created by leon on 17-12-13.
 */

import org.json.JSONObject;

public class JSONStruct {
    private String userID = null;
    private String userName= null;
    private String userPassword= null;
    private String userNickname= null;
    private String noteID= null;
    private String noteAddress= null;
    private String createdTime= null;
    private int totalLikes = 0;
    private int totalComments = 0;
    private String commentID= null;
    private String comFromID= null;
    private String comToID= null;
    private String commentText= null;
    JSONObject objJson;
    public JSONObject userJson(){
        JSONObject uJson = null;
        return uJson;
    }

    public JSONObject noteJson(){
        JSONObject nJson = null;
        return nJson;
    }

    public JSONObject commentJson(){
        JSONObject cJson = null;
        return cJson;
    }


}
