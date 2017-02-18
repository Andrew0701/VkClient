package com.example.andrew.vkclient;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.andrew.vkclient.activity.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class VkMessagingService extends FirebaseMessagingService {
    private static final String TAG = "VkMessagingService";

    public VkMessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        Log.d(TAG, "Text: " + remoteMessage.getData().get("text"));
        sendNotification(remoteMessage.getData().get("text"));

    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.ic_ab_app)
                        .setContentTitle("New message")
                        .setContentText(messageBody);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(001, notificationBuilder.build());
    }
}
