package layout;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.*;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jerrylee.mytime.R;
import com.example.jerrylee.mytime.TimeFormActivity;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;

import database.TimeDatabaseHelper;
import item.DatabaseInsertItem;
import service.TimerService;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChronometerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChronometerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChronometerFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SECTION = "section1";

    private static final String TAG = ChronometerFragment.class.getSimpleName();

    private TimerService timerService;
    private boolean serviceBound;

    private String section;

    private OnFragmentInteractionListener mListener;

    private Button timerButton;
    private TextView timerTextView;
    private EditText taskTimeEditText;

    private final Handler mUpdateTimeHandler = new UIUpdateHandler(this);

    private final static int MSG_UPDATE_TIME = 0;

    private onDataChangedListener dataChangedListener;

    DatabaseInsertItem insertItem = new DatabaseInsertItem();

    private static final int TASK_NAME_REQUEST = 1;

    public ChronometerFragment() {
        // Required empty public constructor
    }

    public static ChronometerFragment newInstance(String param1) {
        ChronometerFragment fragment = new ChronometerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SECTION, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.v(TAG,"onAttach..." +this.getTag());
        try{
            dataChangedListener = (onDataChangedListener) context;

        }catch (ClassCastException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            section = getArguments().getString(ARG_SECTION);
        }
        Log.v(TAG,"onCreate...");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.v(TAG,"onCreateView...");
        return inflater.inflate(R.layout.fragment_chronometer, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.v(TAG,"onActivityCreated...");
        if(savedInstanceState != null){
            insertItem.setTaskName(savedInstanceState.getString("taskName"));
            insertItem.setStartTime(savedInstanceState.getString("startTime"));
            insertItem.setDate(savedInstanceState.getString("date"));
            Log.v(TAG,"on savedInstanceState...");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(Log.isLoggable(TAG, Log.VERBOSE)){
            Log.v(TAG, "Starting binding service------------------");
        }
        Intent intent = new Intent(getActivity(), TimerService.class);
        getActivity().startService(intent);
        getActivity().bindService(intent,mConnection,0);
        Log.v(TAG,"onStart()....");
    }

    //the button for viewpager
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String taskName = taskTimeEditText.getText().toString() == null ? "" : taskTimeEditText.getText().toString();
        insertItem.setTaskName(taskName);

        outState.putString("taskName", insertItem.getTaskName());
        outState.putString("startTime", insertItem.getStartTime());
        outState.putString("date", insertItem.getDate());

        Log.v(TAG,"onSaveInstanceState...");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.v(TAG,"onViewCreated...");
        timerButton = (Button) getView().findViewById(R.id.start_button);
        timerTextView = (TextView) getView().findViewById(R.id.timer_text_view);
        taskTimeEditText = (EditText) getView().findViewById(R.id.autoCompleteTextView);

        taskTimeEditText.clearFocus();

        if(savedInstanceState != null) {
            taskTimeEditText.setText(insertItem.getTaskName());
            Log.v(TAG,"savedInState is not null : taskName is ->"  + insertItem.getTaskName());
        }

        final TimeDatabaseHelper helper = new TimeDatabaseHelper(getContext());
        Log.v(TAG,"onViewCreated...");
        timerButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                timerTextView.setText(R.string.timer_text_view_empty);

                if(serviceBound && !timerService.isTimerRunning()){
                    startTimer();

                    setStartInsertItem();

                    onStartTimeForm();
                }
                else if(serviceBound && timerService.isTimerRunning()){
                    stopTimer();

                    setEndInsertItem();

                    helper.insertContent(insertItem);
                    dataChangedListener.onDataInserted();

                    taskTimeEditText.setText("");
                }
            }
        });

        taskTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(serviceBound && !timerService.isTimerRunning()){
                    startTimer();

                    setStartInsertItem();

                    onStartTimeForm();
                }
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        Log.v(TAG,"onDetach...");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v(TAG,"onStop...");
        updateUIStopRun();
        if(serviceBound){
            if(timerService.isTimerRunning()){
                timerService.foreground();
            }
            else{
                getActivity().stopService(new Intent(getActivity(), TimerService.class));
            }

            getActivity().unbindService(mConnection);
            serviceBound = false;
            Log.v(TAG,"Chrono onStop()....");
        }


    }

    private void startTimer(){
        Log.v(TAG,"Starting timer.....");
        timerService.startTimer();
        updateUIStartRun();
    }

    private void stopTimer(){
        Log.v(TAG,"Stopping timer");
        timerService.stopTimer();
        updateUIStopRun();
    }

    private void setStartInsertItem(){
        insertItem.setStartTime(getCurrentTime());
        insertItem.setDate(getCurrentDate());
    }

    private void setEndInsertItem(){
        insertItem.setTaskName(taskTimeEditText.getText().toString());
        insertItem.setElapsedTime(timerService.elapsedTime());
        insertItem.setEndTime(getCurrentTime());
    }

    private void onStartTimeForm(){
        Intent intent = new Intent(getActivity(), TimeFormActivity.class);
        intent.putExtra(TimeFormActivity.MODE,TimeFormActivity.START_MODE);
        intent.putExtra(TimeFormActivity.FROM_TIME,getCurrentTime());

        startActivityForResult(intent,TASK_NAME_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v(TAG,"onActivityResult...");
        if(requestCode == TASK_NAME_REQUEST && data != null){
            Log.v(TAG, "resultCode: " + resultCode + " compares to wanted resultCode: " + RESULT_OK);
            if(resultCode == RESULT_OK){
                Log.v(TAG,"ON SET TEXTVIEW!!");
                taskTimeEditText.setText(data.getStringExtra(TimeFormActivity.TASK_NAME));
                Log.v(TAG,"onActivityResult..." + data.getStringExtra(TimeFormActivity.TASK_NAME));
            }
        }else{
            Log.v(TAG,"DATA is null...");
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    protected ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            if(Log.isLoggable(TAG,Log.VERBOSE)){
                Log.v(TAG,"Service bound...");
            }
            TimerService.RunServiceBinder binder = (TimerService.RunServiceBinder) service;
            timerService = binder.getService();
            serviceBound = true;

            timerService.background();

            if(timerService.isTimerRunning()){
            updateUIStartRun();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            if(Log.isLoggable(TAG, Log.VERBOSE)){
                Log.v(TAG, "Service disconnect");
            }
            serviceBound = false;
        }
    };

    private void updateUIStartRun(){
        mUpdateTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME);
        timerButton.setText(R.string.timer_stop_button);
    }

    private void updateUIStopRun(){
        mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
        timerButton.setText(R.string.timer_start_button);
    }

    private void updateUITimer(){
        if(serviceBound){
            timerTextView.setText(formattedTimer(timerService.elapsedTime()));
        }
    }

    private String formattedTimer(long time){
        long hour = time/3600;
        long minutes = (time%3600)/60;
        long seconds = time - (hour * 3600 + minutes * 60);

        SimpleDateFormat simpleDateFormat =  new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(0,0,0,(int)hour,(int)minutes,(int)seconds);
        return simpleDateFormat.format(date);
    }

    static class UIUpdateHandler extends Handler{

        private final static int UPDATE_TIME_RATE = 1000;
        private final WeakReference<ChronometerFragment> fragmentWeakReference;

        UIUpdateHandler(ChronometerFragment fragment){
            this.fragmentWeakReference = new WeakReference<ChronometerFragment>(fragment);
        }
        @Override
        public void handleMessage(Message msg) {
            if(MSG_UPDATE_TIME == msg.what){
                if(Log.isLoggable(TAG,Log.VERBOSE)){
                    Log.v(TAG,"updating time......");
                }
                fragmentWeakReference.get().updateUITimer();
                sendEmptyMessageDelayed(MSG_UPDATE_TIME,UPDATE_TIME_RATE);
            }
        }
    }

    private String getCurrentTime(){
        String format = "h:mm a";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return  simpleDateFormat.format(new Date());
    }

    private String getCurrentDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-DD");
        Date date = new Date();
        return simpleDateFormat.format(date);
    }

    public interface onDataChangedListener{
        public void onDataInserted();
    }


}
