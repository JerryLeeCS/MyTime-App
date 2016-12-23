package service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import layout.ChronometerFragment;

/**
 * Created by jerrylee on 11/20/16.
 */

public class TimerService extends Service {

    private static final String TAG = TimerService.class.getSimpleName();

    private long startTime, endTime;

    private boolean isTimerRunning;

    private static final int NOTIFICATION_ID = 1;


    private final IBinder serviceBinder = new RunServiceBinder();

    @Override
    public void onCreate() {
        super.onCreate();

        if(Log.isLoggable(TAG,Log.VERBOSE)){
            Log.v(TAG,"Creating Service...");
        }

        startTime = 0;
        endTime = 0;
        isTimerRunning = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(Log.isLoggable(TAG, Log.VERBOSE)){
            Log.v(TAG,"Service starting...");
        }

        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return serviceBinder;
    }

    public class RunServiceBinder extends Binder {
        public TimerService getService(){
            return TimerService.this;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(Log.isLoggable(TAG, Log.VERBOSE)){
            Log.v(TAG,"Destroying Service...");
        }
    }

    public void startTimer(){
        if(!isTimerRunning){
            startTime = System.currentTimeMillis();
            isTimerRunning = true;
        }else{
            Log.e(TAG,"startTimer request for an already running timer");
        }
    }

    public void stopTimer(){
        if(isTimerRunning){
            endTime = System.currentTimeMillis();
            isTimerRunning = false;
        }else{
            Log.e(TAG, "stopTimer request for a timer that isn't running");
        }
    }

    public boolean isTimerRunning(){
        return isTimerRunning;
    }

    public long elapsedTime(){

        return endTime > startTime ?
                (endTime - startTime) / 1000 :
                (System.currentTimeMillis() - startTime) / 1000;
    }

    public void foreground(){
        startForeground(NOTIFICATION_ID,createNotification());
    }

    public void background(){
        stopForeground(true);
    }

    private Notification createNotification() {
        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setContentTitle("Timer Active")
                .setContentText("tap to return to the app")
                ;

        Intent resultIntent = new Intent(this, ChronometerFragment.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(this,0,resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPendingIntent);

        return builder.build();
    }


}
