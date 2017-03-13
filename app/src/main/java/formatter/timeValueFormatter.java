package formatter;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * Created by jerrylee on 3/12/17.
 */

public class timeValueFormatter implements IValueFormatter {
    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return getTimeValue(value);
    }

    private String getTimeValue(float value){
        int mValue = (int) value;

        int hours = mValue / 3600;
        int minutes = (mValue % 3600) / 60;

        if(hours == 0){
            return String.valueOf(minutes) + " mins";
        }else{
            return String.valueOf(hours) + " hrs " + String.valueOf(minutes) + " mins ";
        }

    }
}
