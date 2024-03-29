package layout;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jerrylee.mytime.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;


import java.util.ArrayList;
import java.util.List;

import database.TimeDatabaseHelper;
import formatter.MyBarChartXAxisValueFormatter;
import formatter.MyBarChartYAxisValueFormatter;
import formatter.timeValueFormatter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChartFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SECTION = "section3";

    private static final String TAG = ChartFragment.class.getSimpleName();

    private String section;

    private OnFragmentInteractionListener mListener;

    private PieChart pieChart;

    private BarChart barChart;

    public ChartFragment() {
        // Required empty public constructor
    }

    public static ChartFragment newInstance(String param1) {
        ChartFragment fragment = new ChartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SECTION, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            section = getArguments().getString(ARG_SECTION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_chart, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pieChart = (PieChart) getView().findViewById(R.id.pieChart);

        barChart = (BarChart) getView().findViewById(R.id.barChart);

        if(pieChart != null) {
            pieChart.setData(getPieData());
            pieChart.setTouchEnabled(false);
            pieChart.setDrawHoleEnabled(true);
            pieChart.getData().setValueFormatter(new timeValueFormatter());

            pieChart.setCenterText("Today\n");
            pieChart.setCenterTextSize(36f);

            pieChart.setEntryLabelColor(Color.BLACK);
            pieChart.setDescription(null);

            pieChart.invalidate();
        }

        if(barChart != null){
            BarData barData = getBarData();
            barData.setBarWidth(0.5f);

            Description description = new Description();
            description.setText("");
            barChart.setDescription(description);

            barChart.setData(barData);
            barChart.setTouchEnabled(false);
            barChart.setFitBars(true);
            barChart.setDrawBorders(false);
            barChart.setDrawValueAboveBar(true);
            barChart.getBarData().setValueFormatter(new timeValueFormatter());
            barChart.getBarData().setValueTextSize(13);

            XAxis xAxis = barChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawLabels(true);
            xAxis.setLabelRotationAngle(30);
            xAxis.setLabelCount(8,true);
            xAxis.setAxisMinimum(0f);
            xAxis.setAxisMaximum(7f);
            xAxis.setGranularity(1f);
            xAxis.setCenterAxisLabels(true);
            xAxis.setValueFormatter(new MyBarChartXAxisValueFormatter());

            YAxis yAxisLeft = barChart.getAxisLeft();
            YAxis yAxisRight= barChart.getAxisRight();

            yAxisRight.setDrawAxisLine(false);
            yAxisRight.setDrawGridLines(false);
            yAxisRight.setDrawLabels(false);

            yAxisLeft.setDrawLabels(true);
            yAxisLeft.setLabelCount(5);
            yAxisLeft.setAxisMinimum(0f);
            yAxisLeft.setGranularity(3600f);
            yAxisLeft.setValueFormatter(new MyBarChartYAxisValueFormatter());
            yAxisLeft.setDrawAxisLine(false);
            yAxisLeft.setDrawGridLines(true);

            barChart.invalidate();
        }
    }

    private PieData getPieData(){
        ArrayList<PieEntry> entries = new ArrayList<>();

        TimeDatabaseHelper timeDatabaseHelper = new TimeDatabaseHelper(getContext());
        entries.addAll(timeDatabaseHelper.getTotalTimePieDataList());

        PieDataSet ds1 = new PieDataSet(entries,"");
        ds1.setColors(ColorTemplate.MATERIAL_COLORS);
        ds1.setSliceSpace(2f);
        ds1.setValueTextSize(12f);

        PieData d = new PieData(ds1);

        return d;
    }

    private BarData getBarData(){
        BarData barData;

        TimeDatabaseHelper timeDatabaseHelper = new TimeDatabaseHelper(getContext());
        barData = timeDatabaseHelper.getBarChartBarData();

        return barData;
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
}
