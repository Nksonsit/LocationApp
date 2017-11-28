package com.myapp.locationapp.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.myapp.locationapp.R;
import com.myapp.locationapp.dbhelper.DBOpenHelper;
import com.myapp.locationapp.helper.Functions;
import com.myapp.locationapp.helper.PrefUtils;
import com.myapp.locationapp.model.Site;
import com.myapp.locationapp.ui.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.e("fcm msg", remoteMessage.getFrom());

        if (!PrefUtils.isUserLoggedIn(this)) {
            return;
        }

        if (remoteMessage.getData().size() > 0) {
            try {
                JSONObject json = new JSONObject(remoteMessage.getData());
                Log.e(TAG, "messageJson1: " + json.toString());

                JSONArray jsonArray = json.getJSONArray("message");
                JSONObject msgData = jsonArray.getJSONObject(0);
                Log.e(TAG, "messageJson: " + msgData.get("Site"));
                if (PrefUtils.isUserLoggedIn(this)) {
                    sendNotification(msgData);
                }
                /*JSONObject messageJson = json.getJSONObject("message");
                Log.e(TAG, "messageJson: " + messageJson.get("Site"));
                Log.e(TAG, "Data: " + Functions.jsonString(json.get("message")));
                if (PrefUtils.isUserLoggedIn(this)) {
                    sendNotification(new JSONObject(Functions.jsonString(json.get("message"))));
                }*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void sendNotification(JSONObject msgObject) {
        Log.e("dfs",msgObject.toString());
        Site site = new Site();
        try {
            site.setId("" + msgObject.get("Id"));
            site.setUserId("" + msgObject.get("UserId"));
            site.setUserId("" + msgObject.get("Site"));
            site.setUserId("" + msgObject.get("Description"));
            site.setUserId("" + msgObject.get("Distance"));
            site.setUserId("" + msgObject.get("Latitude"));
            site.setUserId("" + msgObject.get("Longitude"));
            site.setUserId("" + msgObject.get("Timestamp"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        DBOpenHelper.addSite(site);


        Intent intent = new Intent(this, MainActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification.Builder mBuilder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mBuilder = new Notification.Builder(this, "1");
        } else {
            mBuilder = new Notification.Builder(this);
        }

        Notification notification;
        notification = mBuilder
                .setTicker("News Site uploaded")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setContentTitle("News Site uploaded")
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;

        if (notificationManager != null) {
            notificationManager.notify(m, notification);
        }

    }

}
