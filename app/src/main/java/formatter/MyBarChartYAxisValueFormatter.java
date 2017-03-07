package formatter;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;

/**
 * Created by jerrylee on 3/6/17.
 */

public class MyBarChartYAxisValueFormatter implements IAxisValueFormatter {

    private DecimalFormat mFormat;

    public MyBarChartYAxisValueFormatter(){
        mFormat = new DecimalFormat("###,###,##0.0");
    }


    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        int hours = ((int)value)/3600;

        return hours + " hr";
    }
}
