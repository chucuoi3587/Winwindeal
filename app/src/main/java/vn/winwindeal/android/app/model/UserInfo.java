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
    public String address;
    public int district_id;
    public String phone;
    public int status;
    public String avatar;

    public UserInfo() {

    }
    public UserInfo(JSONObject json) {
        if (json != null) {
            user_id = json.optInt("id");
            access_token = json.optString("session_token");
            refresh_token = json.optString("refresh_token");
            user_type = json.optInt("role_id");
            email = json.optString("email");
            address = json.optString("address", "");
            phone = json.optString("phone", "");
            district_id = json.optInt("district_id", -1);
            status = json.optInt("status", 0);
            avatar = json.optString("avatar", "");
        }
    }
}
