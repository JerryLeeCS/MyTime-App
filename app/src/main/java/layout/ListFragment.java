package layout;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.jerrylee.mytime.R;
import com.example.jerrylee.mytime.TimeFormActivity;

import adapter.RecyclerViewSectionAdapter;
import database.TimeDatabaseHelper;
import item.TaskInfo;
import item.TotalTime;
import listener.onDataChangedListener;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SECTION = "section2";

    private static final String TAG = ListFragment.class.getSimpleName();

    private String section;

    private OnFragmentInteractionListener mListener;

    private onDataChangedListener dataChangedListener;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public static final int REQUEST_FOR_EDIT = 2;

    public ListFragment() {
        // Required empty public constructor
    }

    public static ListFragment newInstance(String param1) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SECTION, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v(TAG,"ListFragment onCreate...");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            section = getArguments().getString(ARG_SECTION);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.v(TAG,"onAttack...");
        try{
            dataChangedListener = (onDataChangedListener) context;
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume...");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.v(TAG,"onCreateView...");
        return inflater.inflate(R.layout.listfragment_list_holder, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(getContext(),1));
        setUpRecyclerView();
        Log.v(TAG, "onActivityCreated...");
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        Log.v(TAG,"onDetach...");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v(TAG,"onActivityResult...");
        if(requestCode == ListFragment.REQUEST_FOR_EDIT && data != null){
            if(resultCode == RESULT_OK){
                TimeDatabaseHelper timeDatabaseHelper = new TimeDatabaseHelper(getContext());
                TaskInfo dataItem = (TaskInfo) data.getSerializableExtra(TimeFormActivity.ITEM);


                TimeFormActivity.DataChanged changedType = (TimeFormActivity.DataChanged) data.getSerializableExtra(TimeFormActivity.DATA_CHANGED_TYPE);

                if(changedType == TimeFormActivity.DataChanged.TASK_AND_ELAPSED_TIME_CHANGED){
                    Toast.makeText(getContext(), "task and elapsed time changed....", Toast.LENGTH_SHORT).show();
                    String fromTaskName = data.getStringExtra(TimeFormActivity.TASK_CHANGED_FROM);
                    TotalTime removeTotalTime = new TotalTime();
                    removeTotalTime.setTask(fromTaskName);
                    removeTotalTime.setElapsedTime(data.getLongExtra(TimeFormActivity.ELAPSED_TIME_MINUS,0));
                    //update Frequency Table based on the old Taskname and new Taskname.
                    timeDatabaseHelper.addOneFrequency(dataItem.getTaskName());
                    timeDatabaseHelper.removeOneFrequency(fromTaskName);
                    //update TotalTime based on the (old time && old taskname) and (new time && new taskname);
                    timeDatabaseHelper.addTotalTime(dataItem.getTotalTime());
                    timeDatabaseHelper.removeTotalTime(removeTotalTime);
                }else if(changedType == TimeFormActivity.DataChanged.TASK_CHANGED){
                    Toast.makeText(getContext(), "task changed...", Toast.LENGTH_SHORT).show();
                    String fromTaskName = data.getStringExtra(TimeFormActivity.TASK_CHANGED_FROM);
                    TotalTime removeTotalTime = new TotalTime();
                    removeTotalTime.setTask(fromTaskName);
                    removeTotalTime.setDate(dataItem.getDate());
                    removeTotalTime.setElapsedTime(dataItem.getElapsedTime());

                    //update the Frequency Table based on the old taskname and new taskname.
                    timeDatabaseHelper.addOneFrequency(dataItem.getTaskName());
                    timeDatabaseHelper.removeOneFrequency(fromTaskName);

                    //update the TotalTime Table based on the old taskname and new taskname.
                    timeDatabaseHelper.addTotalTime(dataItem.getTotalTime());
                    timeDatabaseHelper.removeTotalTime(removeTotalTime);
                }else if(changedType == TimeFormActivity.DataChanged.ELAPSED_CHANGED){
                    Toast.makeText(getContext(), "elapsed time changed...", Toast.LENGTH_SHORT).show();
                    //update the TotalTime Table based on the elasped time difference.
                    long elapsedTimeDifference = data.getLongExtra(TimeFormActivity.CHANGED_ELAPSED_TIME_DIFFERENCE, 0);
                    TotalTime totalTime = new TotalTime();
                    totalTime.setTask(dataItem.getTaskName());
                    totalTime.setDate(dataItem.getDate());
                    totalTime.setElapsedTime(elapsedTimeDifference);

                    timeDatabaseHelper.addTotalTime(totalTime);
                }
                //update TaskInfo Table based on the dataItem
                timeDatabaseHelper.updateTaskInfo(dataItem);
                dataChangedListener.onDataInserted();
            }
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public void refreshAdapter(){
        Log.v(TAG,"setmRecyclerView....");
        TimeDatabaseHelper timeDatabaseHelper = new TimeDatabaseHelper(getContext());

        mAdapter = new RecyclerViewSectionAdapter(timeDatabaseHelper.getDataModelListTaskInfo(),this);

        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.invalidate();
    }

    private void setUpRecyclerView(){
        Log.v(TAG,"on setUpRecyclerView...");
        TimeDatabaseHelper timeDatabaseHelper = new TimeDatabaseHelper(getContext());

        mAdapter = new RecyclerViewSectionAdapter(timeDatabaseHelper.getDataModelListTaskInfo(),this);

        mLayoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}
