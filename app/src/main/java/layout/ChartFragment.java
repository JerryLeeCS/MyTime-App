package layout;

import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jerrylee.mytime.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

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

    private String section;

    private OnFragmentInteractionListener mListener;

    private PieChart pieChart;

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
        pieChart = (PieChart) container.findViewById(R.id.pieChart);
        pieChart.getDescription().setEnabled(false);

        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf");

        pieChart.setCenterTextTypeface(typeface);
        pieChart.setCenterTextSize(10f);
        pieChart.setCenterTextTypeface(typeface);

        pieChart.setHoleRadius(45f);
        pieChart.setTransparentCircleRadius(50f);

        Legend legend = pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);



        pieChart.setData(generatePieData());

        return inflater.inflate(R.layout.fragment_chart, container, false);
    }

    protected PieData generatePieData(){

        int count = 4;

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        for(int i = 0; i < count; i++){
            entries.add(new PieEntry((float) ((Math.random() * 60) + 40), "Quarter " + (i+1)));
        }

        PieDataSet ds1 = new PieDataSet(entries, "Quarterly Revenues 2017");
        ds1.setColors(ColorTemplate.VORDIPLOM_COLORS);
        ds1.setSliceSpace(2f);
        ds1.setValueTextColor(Color.WHITE);
        ds1.setValueTextSize(12f);

        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");

        PieData d = new PieData(ds1);
        d.setValueTypeface(typeface);

        return d;

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
