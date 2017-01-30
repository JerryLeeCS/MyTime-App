package com.example.jerrylee.mytime;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jerrylee.mytime.R;

import item.DataItem;

public class TimeFormActivity extends AppCompatActivity {

    private static final String TAG = TimeFormActivity.class.getSimpleName();

    public static final String MODE = "TIME_FORM_MODE";
    public static final String START_MODE= "START";
    public static final String EDIT_MODE = "EDIT";

    public static final String ITEM = "DAT_ITEM";

    public static final String TASK_NAME = "TASK_NAME";

    private DataItem dataItem;

    private boolean editMode;

    private EditText taskNameEditText;
    private EditText fromTimeEditText;
    private EditText toTimeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_form);

        Log.v(TAG,"onCreate...");

        taskNameEditText = (EditText) findViewById(R.id.taskNameEditText);
        fromTimeEditText = (EditText) findViewById(R.id.fromTimeEditText);
        toTimeEditText = (EditText) findViewById(R.id.toTimeEditText);

        Intent intent = getIntent();

        if(intent.getStringExtra(MODE).equals(START_MODE)){
            editMode = false;
        }else{
            editMode = true;
        }

        dataItem = (DataItem)intent.getSerializableExtra(ITEM);

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
        DataItem returnItem = new DataItem();

        Intent returnIntent = new Intent();

        returnItem.setTaskName(taskNameEditText.getText().toString());
        //returnIntent.putExtra(TASK_NAME,taskNameEditText.getText().toString());
        if(editMode){
            returnItem.setStartTime(fromTimeEditText.getText().toString());
            returnItem.setEndTime(toTimeEditText.getText().toString());
            returnItem.setDate(dataItem.getDate());
            returnItem.setDatabaseID(dataItem.getDatabaseID());
            
        }

        returnIntent.putExtra(ITEM,returnItem);
        setResult(RESULT_OK,returnIntent);
        finish();
    }
}
