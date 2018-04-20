package vn.winwindeal.android.app;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

import vn.winwindeal.android.app.model.UserInfo;

/**
 * Created by nhannguyen on 4/6/2018.
 */

public class GlobalSharedPreference {

    /**
     * Logout-clear access token
     *
     * @param context
     */
    public static void logout(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(context.getResources().getString(R.string.app_name),
                Context.MODE_PRIVATE).edit();
        editor.putString(Constant.JSON_TAG_EMAIL,"");
        editor.putString(Constant.JSON_TAG_ACCESS_TOKEN, "");
        editor.putString(Constant.JSON_TAG_REFRESH_TOKEN, "");
        editor.putInt(Constant.JSON_TAG_USER_ID, -1);
        editor.putInt(Constant.JSON_TAG_USER_TYPE, -1);
        editor.putString(Constant.JSON_TAG_ORDER, "");
        editor.putString(Constant.JSON_TAG_ADDRESS, "");
        editor.putString(Constant.JSON_TAG_PHONE, "");
        editor.putInt(Constant.JSON_TAG_DISTRICT_ID, -1);
        editor.putInt(Constant.JSON_TAG_USER_STATUS, -1);
        editor.commit();
    }

    public static void login(Context context, UserInfo ui) {
        SharedPreferences.Editor editor = context.getSharedPreferences(context.getResources().getString(R.string.app_name),
                Context.MODE_PRIVATE).edit();
        editor.putString(Constant.JSON_TAG_EMAIL, ui.email);
        editor.putString(Constant.JSON_TAG_ACCESS_TOKEN, ui.access_token);
        editor.putString(Constant.JSON_TAG_REFRESH_TOKEN, ui.refresh_token);
        editor.putInt(Constant.JSON_TAG_USER_ID, ui.user_id);
        editor.putInt(Constant.JSON_TAG_USER_TYPE, ui.user_type);
        editor.putString(Constant.JSON_TAG_ADDRESS, ui.address);
        editor.putString(Constant.JSON_TAG_PHONE, ui.phone);
        editor.putInt(Constant.JSON_TAG_DISTRICT_ID, ui.district_id);
        editor.putInt(Constant.JSON_TAG_USER_STATUS, ui.status);
        editor.commit();
    }

    /**
     * Get UserInfo from preferences
     * @param context
     */
    public static UserInfo getUserInfo(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(context.getResources().getString(R.string.app_name),
                Context.MODE_PRIVATE);
        UserInfo ui = new UserInfo();
        ui.email = prefs.getString(Constant.JSON_TAG_EMAIL, "");
        ui.user_id = prefs.getInt(Constant.JSON_TAG_USER_ID, -1);
        ui.user_type = prefs.getInt(Constant.JSON_TAG_USER_TYPE, -1);
        ui.email = prefs.getString(Constant.JSON_TAG_EMAIL, "");
        ui.access_token = prefs.getString(Constant.JSON_TAG_ACCESS_TOKEN, "");
        ui.refresh_token = prefs.getString(Constant.JSON_TAG_REFRESH_TOKEN, "");
        ui.address = prefs.getString(Constant.JSON_TAG_ADDRESS, "");
        ui.phone = prefs.getString(Constant.JSON_TAG_PHONE, "");
        ui.district_id = prefs.getInt(Constant.JSON_TAG_DISTRICT_ID, -1);
        ui.status = prefs.getInt(Constant.JSON_TAG_USER_STATUS, -1);

        return ui;
    }

    /**
     * Check user login or not
     *
     * @param context
     * @return
     */
    public static String getAccessToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(context.getResources().getString(R.string.app_name),
                Context.MODE_PRIVATE);
        String restoredText = prefs.getString(Constant.JSON_TAG_ACCESS_TOKEN,
                null);
        return restoredText;
    }

    /**
     * Get refresh token
     *
     * @param context
     * @return
     */
    public static String getRefreshToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(context.getResources().getString(R.string.app_name),
                Context.MODE_PRIVATE);
        String restoredText = prefs.getString(Constant.JSON_TAG_REFRESH_TOKEN,
                null);
        return restoredText;
    }

    public static void saveProductOrder(Context context, HashMap<String, String> map) {
        SharedPreferences.Editor editor = context.getSharedPreferences(context.getResources().getString(R.string.app_name),
                Context.MODE_PRIVATE).edit();
        editor.putString(Constant.JSON_TAG_ORDER, map.get(Constant.ORDER));
        editor.putString(Constant.JSON_TAG_QUANTITY, map.get(Constant.QUANTITY));
        editor.commit();
    }

    public static HashMap getProductOrder(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(context.getResources().getString(R.string.app_name),
                Context.MODE_PRIVATE);
        HashMap<String, String> map = new HashMap<>();
        map.put(Constant.QUANTITY, prefs.getString(Constant.JSON_TAG_QUANTITY, ""));
        map.put(Constant.ORDER, prefs.getString(Constant.JSON_TAG_ORDER, ""));
        return map;
    }

    public static void clearProductOrder(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(context.getResources().getString(R.string.app_name),
                Context.MODE_PRIVATE).edit();
        editor.putString(Constant.JSON_TAG_ORDER, "");
        editor.putString(Constant.JSON_TAG_QUANTITY, "");
        editor.commit();
    }

    public static void saveDistricts(Context context, String  data) {
        SharedPreferences.Editor editor = context.getSharedPreferences(context.getResources().getString(R.string.app_name),
                Context.MODE_PRIVATE).edit();
        editor.putString(Constant.DISTRICTS, data);
        editor.commit();
    }

    public static String getDistricts(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(context.getResources().getString(R.string.app_name),
                Context.MODE_PRIVATE);
        return prefs.getString(Constant.DISTRICTS, "");
    }

    public static void saveRoles(Context context, String data) {
        SharedPreferences.Editor editor = context.getSharedPreferences(context.getResources().getString(R.string.app_name),
                Context.MODE_PRIVATE).edit();
        editor.putString(Constant.ROLES, data);
        editor.commit();
    }

    public static String getRoles(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(context.getResources().getString(R.string.app_name),
                Context.MODE_PRIVATE);
        return prefs.getString(Constant.ROLES, "");
    }

    public static void saveUserStatus(Context context, String data) {
        SharedPreferences.Editor editor = context.getSharedPreferences(context.getResources().getString(R.string.app_name),
                Context.MODE_PRIVATE).edit();
        editor.putString(Constant.USER_STATUS, data);
        editor.commit();
    }

    public static String getUserStatus(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(context.getResources().getString(R.string.app_name),
                Context.MODE_PRIVATE);
        return prefs.getString(Constant.USER_STATUS, "");
    }

    public static void saveDeviceId(Context context, String deviceID) {
        SharedPreferences.Editor editor = context.getSharedPreferences(context.getResources().getString(R.string.app_name),
                Context.MODE_PRIVATE).edit();
        editor.putString(Constant.DEVICE_ID_TAG, deviceID);
        editor.commit();
    }

    public static String getDeviceId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(context.getResources().getString(R.string.app_name),
                Context.MODE_PRIVATE);
        return prefs.getString(Constant.DEVICE_ID_TAG, "");
    }
}
