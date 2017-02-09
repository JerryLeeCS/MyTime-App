package database.data.repo;

import android.content.ContentValues;
import android.content.Context;

import database.TimeDatabaseHelper;
import database.data.model.Frequency;

/**
 * Created by jerrylee on 2/9/17.
 */

public class FrequencyRepo {

    private Context context;

    public FrequencyRepo(Context context){
        this.context = context;
    }

    public static String createTable(){
        return "CREATE TABLE " + Frequency.TABLE + "("
                + Frequency.TASK_COLUMN + " PRIMARY KEY ,"
                + Frequency.FREQUENCY_COLUMN + " INTEGER )";
    }

    public int insert(Frequency frequency){
        int majorInt;

        TimeDatabaseHelper timeDatabaseHelper = new TimeDatabaseHelper(context);
        ContentValues contentValues = new ContentValues();
        contentValues.put(Frequency.TASK_COLUMN, frequency.getTaskName());
        contentValues.put(Frequency.FREQUENCY_COLUMN, frequency.getFrequency());

        majorInt = (int) timeDatabaseHelper.getWritableDatabase().insert(Frequency.TABLE, null,contentValues);

        timeDatabaseHelper.close();
        return majorInt;
    }

}
