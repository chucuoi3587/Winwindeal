package vn.winwindeal.android.app;

import android.app.Application;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nhannguyen on 4/12/2018.
 */

public class WinwindealApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (GlobalSharedPreference.getDistricts(this).equals("")) {
            JSONObject json = new JSONObject();
            try {
                json.accumulate("0", "--Vui lòng chọn quận--");
                json.accumulate("1", "Quận 1");
                json.accumulate("2", "Quận 2");
                json.accumulate("3", "Quận 3");
                json.accumulate("4", "Quận 4");
                json.accumulate("5", "Quận 5");
                json.accumulate("6", "Quận 6");
                json.accumulate("7", "Quận 7");
                json.accumulate("8", "Quận 8");
                json.accumulate("9", "Quận 9");
                json.accumulate("10", "Quận 10");
                json.accumulate("11", "Quận 11");
                json.accumulate("12", "Quận 12");
                json.accumulate("13", "Quận Bình Thạnh");
                json.accumulate("14", "Quận Gò Vấp");
                json.accumulate("15", "Quận Phú Nhuận");
                json.accumulate("16", "Quận Tân Phú");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            GlobalSharedPreference.saveDistricts(this, json.toString());
        }
        if (GlobalSharedPreference.getRoles(this).equals("")) {
            JSONObject json = new JSONObject();
            try {
                json.accumulate("1", "--Chọn loại người dùng--");
                json.accumulate("2", "Nhân viên");
                json.accumulate("3", "Khách hàng");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            GlobalSharedPreference.saveRoles(this, json.toString());
        }
        if (GlobalSharedPreference.getUserStatus(this).equals("")) {
            JSONObject json = new JSONObject();
            try {
                json.accumulate("-1", "--User Status--");
                json.accumulate("0", "Inactive");
                json.accumulate("1", "Active");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            GlobalSharedPreference.saveUserStatus(this, json.toString());
        }
    }
}
