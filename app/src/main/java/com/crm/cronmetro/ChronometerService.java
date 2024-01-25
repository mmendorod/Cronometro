package com.crm.cronmetro;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

public class ChronometerService extends Service {

    private final IBinder binder = new LocalBinder();
    private static final int NOTIFICATION_ID = 1;
    private boolean isRunning = false;
    private int seconds = 0;
    private Handler handler;

    public class LocalBinder extends Binder {
        ChronometerService getService() {
            return ChronometerService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isRunning) {
            startForeground(NOTIFICATION_ID, createNotification());
            isRunning = true;
            startChronometer();
        }

        return START_STICKY;
    }

    private void startChronometer() {
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                seconds++;
                updateNotification();
                handler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    private void updateNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, createNotification());
    }

    private Notification createNotification() {
        String channelId = "chronometer_channel";
        String channelName = "Chronometer Channel";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);
        builder.setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Cronómetro")
                .setContentText("Tiempo: " + seconds + " segundos")
                .setContentIntent(createPendingIntent())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOngoing(true);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        return builder.build();
    }

    private PendingIntent createPendingIntent() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        stopForeground(true);
        isRunning = false;
    }

    public int getSeconds() {
        return seconds;
    }

    public void resetChronometer() {
        seconds = 0;

    }
}