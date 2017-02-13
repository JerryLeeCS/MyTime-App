package com.example.jerrylee.mytime;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import item.TaskInfo;

public class TimeFormActivity extends AppCompatActivity {

    private static final String TAG = TimeFormActivity.class.getSimpleName();

    public static final String MODE = "TIME_FORM_MODE";
    public static final String START_MODE= "START";
    public static final String EDIT_MODE = "EDIT";

    public static final String ITEM = "DAT_ITEM";

    public static final String TASK_NAME = "TASK_NAME";

    private TaskInfo dataItem;

    private boolean editMode;

    private EditText taskNameEditText;
    private TextView fromTimeEditText;
    private TextView toTimeEditText;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_form);

        Log.v(TAG,"onCreate...");

        taskNameEditText = (EditText) findViewById(R.id.taskNameEditText);
        fromTimeEditText = (TextView) findViewById(R.id.fromTimeEditText);
        toTimeEditText = (TextView) findViewById(R.id.toTimeEditText);
        linearLayout = (LinearLayout) findViewById(R.id.scrollLinearLayout);
        
        Intent intent = getIntent();

        if(intent.getStringExtra(MODE).equals(START_MODE)){
            editMode = false;
        }else{
            editMode = true;
        }

        dataItem = (TaskInfo) intent.getSerializableExtra(ITEM);

        if(!editMode){
            editMode = false;
            toTimeEditText.setVisibility(View.INVISIBLE);
            fromTimeEditText.setText(dataItem.getStartTime());
        }else if(intent.getStringExtra(MODE).equals(EDIT_MODE)){
            editMode = true;
            taskNameEditText.setText(dataItem.getTaskName());
            fromTimeEditText.setText(dataItem.getStartTime());
            toTimeEditText.setText(dataItem.getEndTime());
        }

        if(editMode) {
            fromTimeEditText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String time[] = dataItem.getStartTime().split(":");
                    final int hour = Integer.parseInt(time[0]);
                    final int minute = Integer.parseInt(time[1]);
                    final int second = Integer.parseInt(time[2]);

                    TimePickerDialog timePickerDialog = new TimePickerDialog(TimeFormActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            fromTimeEditText.setText(hourOfDay + ":" + minute + ":" + second);
                        }
                    }, hour, minute, false);
                    timePickerDialog.setTitle("Select Time");
                    timePickerDialog.show();
                }
            });

            toTimeEditText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String time[] = dataItem.getEndTime().split(":");
                    final int hour = Integer.parseInt(time[0]);
                    final int minute = Integer.parseInt(time[1]);
                    final int second = Integer.parseInt(time[2]);

                    TimePickerDialog timePickerDialog = new TimePickerDialog(TimeFormActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            toTimeEditText.setText(hourOfDay + ":" + minute + ":" + second);
                        }
                    }, hour, minute, false);
                    timePickerDialog.setTitle("Select Time");
                    timePickerDialog.show();
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG,"onStart...");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG,"onPause...");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                putResult();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        putResult();
        Log.v(TAG,"onBackPressed....");
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG,"onStop...");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG,"onDestroy...");
    }

    private void putResult(){
        Log.v(TAG,"putResult....");
        TaskInfo returnItem = new TaskInfo();

        Intent returnIntent = new Intent();

        returnItem.setTaskName(taskNameEditText.getText().toString());
        returnItem.setStartTime(fromTimeEditText.getText().toString());

        if(editMode){
            returnItem.setEndTime(toTimeEditText.getText().toString());
            returnItem.setDate(dataItem.getDate());
            returnItem.setID(dataItem.getID());
            returnItem.setElapsedTime(getValidElapsedTime());
        }

        returnIntent.putExtra(ITEM,returnItem);
        setResult(RESULT_OK,returnIntent);
        finish();
    }


    private long getValidElapsedTime(){
        try {
            Date startTime = new SimpleDateFormat("h:mm:ss", Locale.ENGLISH).parse(fromTimeEditText.getText().toString());
            Date endTime = new SimpleDateFormat("h:mm:ss", Locale.ENGLISH).parse(toTimeEditText.getText().toString());
            long elapsedTime = (endTime.getTime() - startTime.getTime())/1000;

            Log.v(TAG,"getValidElapsedTime...." + elapsedTime);
            return elapsedTime;
        }catch (Exception e){
            Log.e(TAG,e.toString());
        }

        return 0;
    }
}


