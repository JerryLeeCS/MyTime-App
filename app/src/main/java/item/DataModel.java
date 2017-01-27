package item;

import android.provider.ContactsContract;

import java.util.List;

/**
 * Created by Jerry on 1/12/2017.
 */

public class DataModel {
    private String sectionTitle;
    private List<DatabaseInsertItem> itemList;

    public DataModel(){
    }

    public DataModel(String sectionTitle, List<DatabaseInsertItem> itemList){
        this.sectionTitle = sectionTitle;
        this.itemList = itemList;
    }

    public String getSectionTitle(){
        return sectionTitle;
    }

    public void setSectionTitle(String sectionTitle){
        this.sectionTitle = sectionTitle;
    }

    public List<DatabaseInsertItem> getItemList(){
        return itemList;
    }

    public void setItemList(List<DatabaseInsertItem> itemList){
        this.itemList = itemList;
    }
}
