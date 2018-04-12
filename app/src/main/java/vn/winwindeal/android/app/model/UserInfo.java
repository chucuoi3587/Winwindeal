package vn.winwindeal.android.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/**
 * Created by nhannguyen on 4/6/2018.
 */

public class UserInfo implements Parcelable{
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(user_id);
        parcel.writeString(access_token);
        parcel.writeString(refresh_token);
        parcel.writeInt(user_type);
        parcel.writeString(email);
        parcel.writeString(address);
        parcel.writeString(phone);
        parcel.writeInt(district_id);
        parcel.writeInt(status);
        parcel.writeString(avatar);
    }

    public static final Creator CREATOR = new Creator() {
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

    public UserInfo(Parcel in) {
        user_id = in.readInt();
        access_token = in.readString();
        refresh_token = in.readString();
        user_type = in.readInt();
        email = in.readString();
        address = in.readString();
        phone = in.readString();
        district_id = in.readInt();
        status = in.readInt();
        avatar = in.readString();
    }
}
