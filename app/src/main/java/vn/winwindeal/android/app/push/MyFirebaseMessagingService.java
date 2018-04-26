package vn.winwindeal.android.app.push;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by nhannguyen on 3/17/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private String TAG = "FirebaseMessagingService";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Toast.makeText(getBaseContext(), "Message data payload: " + remoteMessage.getData(), Toast.LENGTH_SHORT).show();

//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                scheduleJob();
//            } else {
//                // Handle message within 10 seconds
//                handleNow();
//            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            Toast.makeText(getBaseContext(), "Message Notification Body: " + remoteMessage.getNotification().getBody(), Toast.LENGTH_SHORT).show();
        }
//        Log.e(TAG, "Push received: " + intent.getExtras().toString());
//
//        Bundle extras = intent.getExtras();
//        for (String key : extras.keySet()) {
//            Object value = extras.get(key);
//            Log.d("Tay", "Push received: " +  String.format("%s %s (%s)", key,
//                    value.toString(), value.getClass().getName()));
//        }
//        String key = extras.getString("key", "");
//        if (!key.equals("")) {
//            // download file process
//            String name = extras.getString("alert", "");
//            if (!name.equals("")) {
//                String[] strs = name.split(", ");
//                if (strs.length > 1) {
//                    name = strs[0];
//                }
//                doDownloadFile(context, key, name);
//            }
//        }
//        parsePushJson(context, extras);
    }
}
