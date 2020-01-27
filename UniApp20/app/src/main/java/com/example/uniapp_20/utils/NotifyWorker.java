package com.example.uniapp_20.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.uniapp_20.R;
import com.example.uniapp_20.SplashScreen;

import java.util.Calendar;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class NotifyWorker extends Worker {
    public NotifyWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {

        String title =  getInputData().getString(FrontEngine.EXTRA_TITLE);
        String text = getInputData().getString(FrontEngine.EXTRA_TEXT);

        int id = (int) getInputData().getLong(FrontEngine.EXTRA_ID, 0);
        // Method to trigger an instant notification
        triggerNotification(id, title, text);

        return Result.success();
        // (Returning RETRY tells WorkManager to try this task again
        // later; FAILURE says not to try again.)
    }

    public static void cancelReminder(String tag) {
        WorkManager instance = WorkManager.getInstance();
        instance.cancelAllWorkByTag(tag);
    }


    public static void scheduleReminder(Data data, String tag) {

        Calendar currentDate = Calendar.getInstance();
        Calendar dueDate = Calendar.getInstance();
        // Set Execution around 11:15:00 AM
        dueDate.set(Calendar.HOUR_OF_DAY, 11);
        dueDate.set(Calendar.MINUTE, 15);
        dueDate.set(Calendar.SECOND, 0);

        if(dueDate.before(currentDate)){
            dueDate.add(Calendar.HOUR_OF_DAY, 24);
        }

        long timeDiff = dueDate.getTimeInMillis() - currentDate.getTimeInMillis();


        OneTimeWorkRequest notificationWork = new OneTimeWorkRequest.Builder(NotifyWorker.class)
                .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS).addTag(tag)
                .setInputData(data).build();

        WorkManager instance = WorkManager.getInstance();
        instance.enqueue(notificationWork);
    }
    private void triggerNotification(int id, String title, String text) {
        SessionManager.getInstance(getApplicationContext()).setScreen("MensaFragment");
        Intent intent = new Intent(getApplicationContext(), SplashScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("id", id);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

        NotificationManager notificationManager = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default", "Default", NotificationManager.IMPORTANCE_DEFAULT);
            Objects.requireNonNull(notificationManager).createNotificationChannel(channel);
        }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), "default")
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true);

        Objects.requireNonNull(notificationManager).notify(id, notification.build());
    }

}