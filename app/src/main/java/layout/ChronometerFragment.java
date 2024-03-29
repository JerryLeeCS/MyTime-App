package layout;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.*;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jerrylee.mytime.R;
import com.example.jerrylee.mytime.TimeFormActivity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import at.grabner.circleprogress.CircleProgressView;
import database.TimeDatabaseHelper;
import item.TaskColorTag;
import item.TaskInfo;
import listener.onDataChangedListener;
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

    private ImageButton timerButton;
    private TextView taskNameEditText;
    private CircleProgressView circleProgressView;

    private final static int MSG_UPDATE_TIME = 0;

    private onDataChangedListener dataChangedListener;

    TaskInfo insertItem;
    TaskColorTag insertTaskColorTag;

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
    public void onStart() {
        super.onStart();
        if(Log.isLoggable(TAG, Log.VERBOSE)){
            Log.v(TAG, "Starting binding service");
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
        String taskName = taskNameEditText.getText().toString() == null ? "xx" : taskNameEditText.getText().toString();
        insertItem.setTaskName(taskName);

        outState.putSerializable("insertItem", (Serializable) insertItem);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.v(TAG,"onViewCreated...");
        timerButton = (ImageButton) getView().findViewById(R.id.start_button);
        taskNameEditText = (TextView) getView().findViewById(R.id.taskNameTextView);
        circleProgressView = (CircleProgressView) getView().findViewById(R.id.timer_circle_progress_view);
        
        final TimeDatabaseHelper timeDatabaseHelper = new TimeDatabaseHelper(getContext());

        circleProgressView.setSpinBarColor(getResources().getColor(R.color.white));

        Log.v(TAG,"onViewCreated...");
        timerButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                if(serviceBound && !timerService.isTimerRunning()){
                    onStartTimeForm();

                    startTimer();

                    setStartInsertItem();
                }
                else if(serviceBound && timerService.isTimerRunning()){
                    stopTimer();

                    setEndInsertItem();

                    setEndTaskColor();

                    timeDatabaseHelper.insertTaskInfo(insertItem);
                    timeDatabaseHelper.addOneFrequency(insertItem.getTaskName());
                    timeDatabaseHelper.addTotalTime(insertItem.getTotalTime());
                    timeDatabaseHelper.addColorTag(insertTaskColorTag);

                    dataChangedListener.onDataInserted();

                    taskNameEditText.setText("");
                }
            }
        });

        taskNameEditText.setOnClickListener(new View.OnClickListener() {
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.v(TAG,"onActivityCreated...");
        if(savedInstanceState != null){
            insertItem = (TaskInfo) savedInstanceState.getSerializable("insertItem");

            Log.v(TAG,"on savedInstanceState Restored...");
            Toast.makeText(getContext(),"savedInstanceState is restored...",Toast.LENGTH_LONG).show();
        }else{
            Log.v(TAG,"on savedInstanceState is null....");
            if(insertItem != null){

            }else{
                insertItem = new TaskInfo();
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
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
        timerService.setStartTimeInclock(getCurrentTime());
        timerService.setStartDate(getCurrentDate());
    }

    private void setEndInsertItem(){
        insertItem.setTaskName(timerService.getTaskName() == null ? "" : timerService.getTaskName());
        insertItem.setStartTime(timerService.getStartTimeInclock() == null ? "" : timerService.getStartTimeInclock());
        insertItem.setDate(timerService.getStartDate() == null ? "" : timerService.getStartDate());
        insertItem.setElapsedTime(timerService.elapsedTime());
        insertItem.setEndTime(getCurrentTime());
    }

    private void setEndTaskColor(){
        insertTaskColorTag = new TaskColorTag();
        insertTaskColorTag.setTaskColor(timerService.getColor());
        insertTaskColorTag.setTaskTag(timerService.getTaskTag());
    }

    private void onStartTimeForm(){
        TaskInfo dataItem = new TaskInfo();
        dataItem.setStartTime(getCurrentTime());

        Intent intent = new Intent(getActivity(), TimeFormActivity.class);
        intent.putExtra(TimeFormActivity.MODE,TimeFormActivity.START_MODE);
        intent.putExtra(TimeFormActivity.ITEM,dataItem);

        startActivityForResult(intent,TASK_NAME_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v(TAG,"onActivityResult...");
        if(requestCode == TASK_NAME_REQUEST && data != null){
            if(resultCode == RESULT_OK){
                TaskInfo dataItem = (TaskInfo) data.getSerializableExtra(TimeFormActivity.ITEM);
                TaskColorTag taskColorTag = (TaskColorTag) data.getSerializableExtra(TimeFormActivity.TASK_COLOR_TAG);

                taskNameEditText.setText(dataItem.getTaskName());
                timerService.setTaskName(dataItem.getTaskName());
                timerService.setColor(taskColorTag.getTaskColor());
                timerService.setTaskTag(taskColorTag.getTaskTag());
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
        Log.v(TAG,"updateUIStartRun....");
        taskNameEditText.setText(timerService.getTaskName() == null ? "" : timerService.getTaskName());
        timerButton.setImageResource(R.drawable.check);
        circleProgressView.spin();
    }

    private void updateUIStopRun(){
        Log.v(TAG,"updateUIStopRun...");
        timerButton.setImageResource(R.drawable.play);
        circleProgressView.stopSpinning();
    }

    private String formattedTimer(long time){
        long hour = time/3600;
        long minutes = (time%3600)/60;
        long seconds = time - (hour * 3600 + minutes * 60);

        SimpleDateFormat simpleDateFormat =  new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(0,0,0,(int)hour,(int)minutes,(int)seconds);
        return simpleDateFormat.format(date);
    }

    private String getCurrentTime(){
        String format = "h:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return  simpleDateFormat.format(new Date());
    }

    private String getCurrentDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return simpleDateFormat.format(date);
    }

}
