package item;

import android.provider.ContactsContract;

import java.util.List;

/**
 * Created by Jerry on 1/12/2017.
 */

public class DataModel {
    private String sectionTitle;
    private List<RecyclerViewItem> itemList;

    public DataModel(){

    }

    public DataModel(String sectionTitle, List<RecyclerViewItem> itemList){
        this.sectionTitle = sectionTitle;
        this.itemList = itemList;
    }

    public String getSectionTitle(){
        return sectionTitle;
    }

    public void setSectionTitle(String sectionTitle){
        this.sectionTitle = sectionTitle;
    }

    public List<RecyclerViewItem> getItemList(){
        return itemList;
    }

    public void setItemList(List<RecyclerViewItem> itemList){
        this.itemList = itemList;
    }
}
