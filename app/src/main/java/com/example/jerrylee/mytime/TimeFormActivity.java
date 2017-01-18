package com.example.jerrylee.mytime;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class TimeFormActivity extends AppCompatActivity {

    private static final String TAG = TimeFormActivity.class.getSimpleName();

    public static final String MODE = "TIME_FORM_MODE";
    public static final String START_MODE= "START";
    public static final String EDIT_MODE = "EDIT";

    public static final String FROM_TIME = "TIME_FORM_FROM_TIME";
    public static final String TO_TIME = "TIME_FORM_TO_TIME";

    public static final String TASK_NAME = "TASK_NAME";

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
            Log.v(TAG,"getStringExtra(MODE) =" + intent.getStringExtra(MODE));
            toTimeEditText.setVisibility(View.INVISIBLE);

            fromTimeEditText.setText(intent.getStringExtra(FROM_TIME));
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG,"onDestroy...");
        Intent intent = new Intent();
        intent.putExtra(TASK_NAME, taskNameEditText.getText().toString());
        setResult(RESULT_OK,intent);
        finish();
    }
}
