package service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.jerrylee.mytime.MainActivity;
import com.example.jerrylee.mytime.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import layout.ChronometerFragment;

/**
 * Created by jerrylee on 11/20/16.
 */

public class TimerService extends Service {

    private static final String TAG = TimerService.class.getSimpleName();

    private long startTime, endTime;

    private boolean isTimerRunning;

    private static final int NOTIFICATION_ID = 1;

    private String taskName;
    private String startTimeInclock;
    private String startDate;

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

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm:ss");
            Date resultDate = new Date(startTime);

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
        if(Log.isLoggable(TAG,Log.VERBOSE)){
            Log.v(TAG,"foreground starting...");
        }
        Log.v(TAG,"foreground starting...");
    }

    public void background(){
        stopForeground(true);
        Log.v(TAG,"background starting...");
    }

    private Notification createNotification() {
        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this);

        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(taskName == null ? "" : taskName);
        builder.setContentText("tap to return to the app");

        Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(this,0,resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPendingIntent);

        Log.v(TAG,"notification building...");
        return builder.build();
    }


    public void setTaskName(String taskName){
        this.taskName = taskName;
    }

    public String getTaskName(){
        return taskName;
    }

    public void setStartTimeInclock(String startTimeInclock){
        this.startTimeInclock = startTimeInclock;
    }

    public String getStartTimeInclock(){
        return startTimeInclock;
    }

    public void setStartDate(String startDate){
        this.startDate = startDate;
    }

    public String getStartDate(){
        return startDate;
    }

}
