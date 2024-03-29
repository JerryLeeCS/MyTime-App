package com.example.jerrylee.mytime;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import database.TimeDatabaseHelper;
import item.TaskColorTag;
import item.TaskInfo;
import petrov.kristiyan.colorpicker.ColorPicker;

public class TimeFormActivity extends AppCompatActivity {

    private static final String TAG = TimeFormActivity.class.getSimpleName();

    public static final String MODE = "TIME_FORM_MODE";
    public static final String START_MODE= "START";
    public static final String EDIT_MODE = "EDIT";

    public static final String ITEM = "DAT_ITEM";
    public static final String TASK_COLOR_TAG = "DAT_COLOR_TAG";

    public static final String DATA_CHANGED_TYPE = "DATA_CHANGED_TYPE";

    public static final String TASK_NAME = "TASK_NAME";
    public static final String TASK_CHANGED_FROM = "TASK_CHANGED_FROM";

    public static final String CHANGED_ELAPSED_TIME_DIFFERENCE = "CHANGED_ELAPSED_TIME_DIFFERENCE";
    public static final String ELAPSED_TIME_MINUS = "ELAPSED_TIME_MINUS";

    private TaskInfo dataItem;
    private Toolbar toolBar;
    private TextView currentDateTextView;

    public enum DataChanged{
        TASK_CHANGED,
        ELAPSED_CHANGED,
        TASK_AND_ELAPSED_TIME_CHANGED,
        NOTHING_CHANGED
    }

    private boolean editMode;

    private int colorTag = -1;

    private EditText taskNameEditText;
    private EditText colorTagEditText;
    private TextView fromTimeEditText;
    private TextView toTimeEditText;
    private LinearLayout taskNameLinearLayout;
    private LinearLayout colorTagLinearLayout;
    private ImageButton colorImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_form);

        Log.v(TAG,"onCreate...");

        taskNameEditText = (EditText) findViewById(R.id.taskNameEditText);
        colorTagEditText = (EditText) findViewById(R.id.colorTagEditText);
        fromTimeEditText = (TextView) findViewById(R.id.fromTimeEditText);
        toTimeEditText = (TextView) findViewById(R.id.toTimeEditText);
        taskNameLinearLayout = (LinearLayout) findViewById(R.id.taskNameScrollLinearLayout);
        colorTagLinearLayout = (LinearLayout) findViewById(R.id.colorTagLinearLayout);
        toolBar = (Toolbar) findViewById(R.id.timeform_toolbar);
        toolBar.setNavigationIcon(R.drawable.ic_navigate_before_black_48dp);
        currentDateTextView = (TextView) findViewById(R.id.currentDateTextView);
        colorImageButton = (ImageButton) findViewById(R.id.colorButton);

        currentDateTextView.setText(getCurrentDate() + " " + getDayOfWeek());

        setSupportActionBar(toolBar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();

        if(intent.getStringExtra(MODE).equals(START_MODE)){
            editMode = false;
        }else{
            editMode = true;
        }

        dataItem = (TaskInfo) intent.getSerializableExtra(ITEM);

        if(!editMode){
            toTimeEditText.setVisibility(View.INVISIBLE);
            fromTimeEditText.setText(dataItem.getStartTime());
            setUpTaskNameLinearLayout();
            setUpColorTagLinearLayout();
        }else{
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

        colorImageButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                final ColorPicker colorPicker = new ColorPicker(TimeFormActivity.this);
                colorPicker.setOnFastChooseColorListener(new ColorPicker.OnFastChooseColorListener() {
                    @Override
                    public void setOnFastChooseColorListener(int position, int color) {
                        colorImageButton.setColorFilter(color);
                        colorTag = color;
                    }

                    @Override
                    public void onCancel() {

                    }
                }).disableDefaultButtons(true).show();

            }
        });
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
        TaskInfo returnTaskInfo = new TaskInfo();
        TaskColorTag returnTaskColorTag = new TaskColorTag();

        Intent returnIntent = new Intent();

        returnTaskInfo.setTaskName(taskNameEditText.getText().toString());
        returnTaskInfo.setStartTime(fromTimeEditText.getText().toString());

        returnTaskColorTag.setTaskColor(colorTag);
        returnTaskColorTag.setTaskTag(colorTagEditText.getText().toString());

        if(editMode) {//Haven't add the TaskColorTag in the editMode yet
            returnTaskInfo.setEndTime(toTimeEditText.getText().toString());
            returnTaskInfo.setDate(dataItem.getDate());
            returnTaskInfo.setID(dataItem.getID());
            returnTaskInfo.setElapsedTime(getValidElapsedTime());

            if (!dataItem.getTaskName().equals(returnTaskInfo.getTaskName())
                    && getElapsedTimeDifference(dataItem.getElapsedTime(), returnTaskInfo.getElapsedTime()) >2 ) {
                returnIntent.putExtra(DATA_CHANGED_TYPE, DataChanged.TASK_AND_ELAPSED_TIME_CHANGED);
                returnIntent.putExtra(TASK_CHANGED_FROM, dataItem.getTaskName());
                returnIntent.putExtra(ELAPSED_TIME_MINUS, dataItem.getElapsedTime());
            } else {
                if (!dataItem.getTaskName().equals(returnTaskInfo.getTaskName())) {
                    returnIntent.putExtra(DATA_CHANGED_TYPE, DataChanged.TASK_CHANGED);
                    returnIntent.putExtra(TASK_CHANGED_FROM, dataItem.getTaskName());
                } else if (getElapsedTimeDifference(dataItem.getElapsedTime(), returnTaskInfo.getElapsedTime()) != 0) {
                    returnIntent.putExtra(DATA_CHANGED_TYPE, DataChanged.ELAPSED_CHANGED);
                    returnIntent.putExtra(CHANGED_ELAPSED_TIME_DIFFERENCE, getElapsedTimeDifference(returnTaskInfo.getElapsedTime(), dataItem.getElapsedTime() ));
                } else {
                    returnIntent.putExtra(DATA_CHANGED_TYPE, DataChanged.NOTHING_CHANGED);
                }
            }
        }
        returnIntent.putExtra(ITEM,returnTaskInfo);
        returnIntent.putExtra(TASK_COLOR_TAG,returnTaskColorTag);

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

    private long getElapsedTimeDifference(long fromElapsedTime, long toElapsedTime){
        return fromElapsedTime - toElapsedTime;
    }

    private void setUpTaskNameLinearLayout(){
        TimeDatabaseHelper helper = new TimeDatabaseHelper(this);
        final List<String> taskList = helper.getMostFrequentTaskList();

        for(int i = 0; i < taskList.size();i++){
            Button button = new Button(this);
            final String task = taskList.get(i);
            button.setText(task);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    taskNameEditText.setText(task);
                }
            });
            taskNameLinearLayout.addView(button);
        }
    }

    private void setUpColorTagLinearLayout(){
        TimeDatabaseHelper timeDatabaseHelper = new TimeDatabaseHelper(this);
        final List<TaskColorTag> colorTagList = timeDatabaseHelper.getColorTagList();

        for(int i = 0; i < colorTagList.size(); i++){
            Button button = new Button(this);
            final TaskColorTag taskColorTag = colorTagList.get(i);
            button.setText(taskColorTag.getTaskTag());
            button.setTextColor(taskColorTag.getTaskColor());

            button.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    colorTagEditText.setText(taskColorTag.getTaskTag());
                    colorImageButton.setColorFilter(taskColorTag.getTaskColor());
                }
            });
            colorTagLinearLayout.addView(button);
        }

    }

    private String getCurrentDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return simpleDateFormat.format(date);
    }

    private String getDayOfWeek(){
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date date1 = simpleDateFormat.parse(getCurrentDate());
            calendar.setTime(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Toast.makeText(this,"Week of day: " + calendar.get(Calendar.DAY_OF_WEEK),Toast.LENGTH_SHORT).show();

        switch(calendar.get(Calendar.DAY_OF_WEEK)){
            case 2:
                return "MON";
            case 3:
                return "TUES";
            case 4:
                return "WED";
            case 5:
                return "THUR";
            case 6:
                return "FRI";
            case 7:
                return "SAT";
            case 8:
                return "SUN";
        }
        return "";
    }
}


