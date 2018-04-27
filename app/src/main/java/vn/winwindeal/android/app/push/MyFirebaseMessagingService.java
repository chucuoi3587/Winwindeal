package vn.winwindeal.android.app.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import vn.winwindeal.android.app.Constant;
import vn.winwindeal.android.app.OrderDetailActivity;
import vn.winwindeal.android.app.R;

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

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

        }
        String title = remoteMessage.getNotification().getTitle();
        String message = remoteMessage.getNotification().getBody();
        Intent resultIntent = new Intent(getBaseContext(), OrderDetailActivity.class);
        resultIntent.putExtra("order_id", Integer.parseInt(remoteMessage.getData().get("order_id")));
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        int icon = R.drawable.winwindeal_logo_72x72_shihoute;

        int smallIcon = Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT ? R.drawable.winwindeal_logo_72x72_shihoute : R.drawable.winwindeal_logo_72x72;

        int mNotificationId = AppConfig.NOTIFICATION_ID;

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        getBaseContext(),
                        0,
                        resultIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getBaseContext(), Constant.NOTIFICATION_CHANEL_ID);
        Notification notification = mBuilder.setSmallIcon(smallIcon).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setLargeIcon(BitmapFactory.decodeResource(getBaseContext().getResources(), icon))
                .setContentText(message)
                .build();

        NotificationManager notificationManager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(mNotificationId, notification);
    }
}
