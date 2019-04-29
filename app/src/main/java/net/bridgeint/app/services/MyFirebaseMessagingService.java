package net.bridgeint.app.services;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import net.bridgeint.app.R;
import net.bridgeint.app.activities.SplashActivity;
import net.bridgeint.app.application.Application;
import net.bridgeint.app.models.notifications.Notification;
import net.bridgeint.app.models.notifications.PostData;
import net.bridgeint.app.utils.Utility;

import java.util.Map;

/**
 * Created by DeviceBee on 8/16/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCM";
    private PendingIntent pendingIntent;
    private Intent intent;
    private String CHANNEL_ID = "1234";

    public static final String TYPE_MESSAGE = "Message";
    //public static final String TYPE_CHAT = "chat";
    public static final String TYPE_ADMIN = "admin";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "onMessageReceived full : " + remoteMessage.toString());
        Log.d(TAG, "onMessageReceived: " + remoteMessage.getData());
        try {
            Log.e(TAG, "From: " + remoteMessage.getFrom());
            if (remoteMessage.getNotification() != null) {
                Log.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            }
            if (remoteMessage.getData().size() > 0) {
                Log.e(TAG, "Message data payload: " + remoteMessage.getData());
            }

            String message = "";
            if(remoteMessage.getData().containsKey("aps")){
                message = remoteMessage.getData().get("aps");
                for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
                    System.out.println(entry.getKey() + " : " + entry.getValue());
                }
                Log.e(TAG, "Notification > Ready for notification");
                sendNotification(message);
            } else {
                message = remoteMessage.toString();
                for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
                    System.out.println(entry.getKey() + " : " + entry.getValue());
                }
                Log.e(TAG, "Notification > Ready for notification admin");
                Notification notificationBody = new Notification();
                notificationBody.setType(remoteMessage.getData().get("type"));
                notificationBody.setMessage(remoteMessage.getData().get("message"));
                sendNotificationScreen(notificationBody);
            }

            if(!message.isEmpty()) {

            }
        } catch (Exception e) {
            Log.e(TAG, "Notification > error");
            e.printStackTrace();
        }
    }

    private void sendNotificationScreen(Notification messageBody) {

        if (messageBody.getType().equals(TYPE_MESSAGE)) {
            if (Application.getIsAppOpen()) {
                Log.e(TAG, "Notification > App is Opne");
                Log.e(TAG, "Notification isShowChat > " + Application.isShowChat());
                if (!Application.isShowChat()) {
                    Log.e(TAG, "Notification show dialog");
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Activity activity = Application.getAppInstance().getCurrentActivity();
                                Utility.showChatDialog(activity, messageBody.getMessage());
                            } catch (Exception e) {
                                e.printStackTrace();
                                showNotification(messageBody);
                            }
                        }
                    }, 1000);
                } else {
                    Log.e(TAG, "Notification nothing");
                }
            } else {
                Log.e(TAG, "Notification > Chat Show notification");
                showNotification(messageBody);
            }
        } else if(messageBody.getType().equals(TYPE_ADMIN)){
            if (Application.getIsAppOpen()) {

            } else {
                Log.e(TAG, "Notification > Admin Show notification");
                showNotification(messageBody);
            }
        } else {
            Log.e(TAG, "Notification > Show notification for not message");
            showNotification(messageBody);
        }
    }

    private void showNotification(Notification messageBody) {
        intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("noti_type", messageBody.getType());
        pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Bitmap rawBitmap = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.noti_icon)
                .setLargeIcon(rawBitmap)
                .setContentTitle("Bridge App")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentText(messageBody.getMessage())
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody.getMessage()))
                .setDefaults(NotificationCompat.DEFAULT_LIGHTS)
                /*.setSound(defaultSoundUri)*/
                .setContentIntent(pendingIntent);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            String description = getString(R.string.app_name) + "D"; //getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
           // channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
            Log.e(TAG, "Notification > Channel Created");
        }

        notificationManager.notify(0, notificationBuilder.build());

    }


    private void sendNotification(String msg) {
        Gson gson = new Gson();
        Notification element = gson.fromJson(msg, Notification.class);
        Log.e("JSON", gson.toJson(element));
        sendNotificationScreen(element);

    }


   /* val mNotifyMgr = mContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    val defaults = NotificationCompat.DEFAULT_LIGHTS
    val mBuilder = NotificationCompat.Builder(mContext, FirebaseMessaging.CHANNEL_GENERAL)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(Notification.PRIORITY_HIGH)
            .setStyle(NotificationCompat.BigTextStyle().bigText(content))
            .setDefaults(defaults)
            .setTicker(title)

        if(pendingIntent != null) {
        mBuilder.setContentIntent(pendingIntent)
    }
        mBuilder.setAutoCancel(true)

    val customNotification = mBuilder.build()

        if (MyApplication.isOpen()) {
        customNotification.defaults = 0
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") val channel = NotificationChannel(
                    MyApplication.getAppInstance()!!.getString(R.string.app_name),
                    MyApplication.getAppInstance()!!.getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_LOW
                )
            mNotifyMgr.createNotificationChannel(channel)
        }
    }
    val notificationId = 1219724
        mNotifyMgr.notify(notificationId, customNotification)*/


}
