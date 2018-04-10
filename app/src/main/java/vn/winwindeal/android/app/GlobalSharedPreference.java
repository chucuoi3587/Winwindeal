package vn.winwindeal.android.app;

import android.content.Context;
import android.content.SharedPreferences;

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

    public static void saveProductOrder(Context context, String order) {
        SharedPreferences.Editor editor = context.getSharedPreferences(context.getResources().getString(R.string.app_name),
                Context.MODE_PRIVATE).edit();
        editor.putString(Constant.JSON_TAG_ORDER, order);
        editor.commit();
    }

    public static String getProductOrder(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(context.getResources().getString(R.string.app_name),
                Context.MODE_PRIVATE);
        return prefs.getString(Constant.JSON_TAG_ORDER, "");
    }
}
