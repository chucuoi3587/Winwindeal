package vn.winwindeal.android.app.model;

import org.json.JSONObject;

/**
 * Created by nhannguyen on 4/6/2018.
 */

public class UserInfo {
    public Integer user_id;
    public String email;
    public String access_token;
    public String refresh_token;
    public int user_type;

    public UserInfo() {

    }
    public UserInfo(JSONObject json) {
        if (json != null) {
            user_id = json.optInt("user_id");
            access_token = json.optString("session_token");
            refresh_token = json.optString("refresh_token");
            user_type = json.optInt("role_id");
            email = json.optString("email");
        }
    }
}
