package vn.winwindeal.android.app.push;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import vn.winwindeal.android.app.GlobalSharedPreference;

/**
 * Created by nhannguyen on 3/17/2018.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String token = FirebaseInstanceId.getInstance().getToken();//getToken(AppConfig.GOOGLE_PROJECT_ID, "FCM");
        GlobalSharedPreference.saveDeviceId(getBaseContext(), token);
        Log.d("NhanNATC", "==== FCM token : " + token);
    }
}
