package formatter;

import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by jerrylee on 3/6/17.
 */

public class MyBarChartXAxisValueFormatter implements IAxisValueFormatter {

    static final String TAG = MyBarChartXAxisValueFormatter.class.getSimpleName();

    private String[] mValues;
    public MyBarChartXAxisValueFormatter(){
        this.mValues = new String[]{"MON","TUES","WED","THUR","FRI","SAT","SUN",""};
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        Log.v(TAG,"value: " + value);
        return mValues[(int) value];
    }
}
