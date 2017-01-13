package adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.example.jerrylee.mytime.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import item.DataModel;
import item.RecyclerViewItem;

/**
 * Created by Jerry on 1/12/2017.
 */

public class RecyclerViewSectionAdapter extends SectionedRecyclerViewAdapter<RecyclerView.ViewHolder> {
    private List<DataModel> allData;


    public RecyclerViewSectionAdapter(List<DataModel> itemList){
        this.allData = itemList;
    }


    @Override
    public int getSectionCount() {
        return allData.size();
    }

    @Override
    public int getItemCount(int section) {
        return allData.get(section).getItemList().size();
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int section) {

        String sectionName = allData.get(section).getSectionTitle();
        SectionViewHolder sectionViewHolder = (SectionViewHolder) holder;
        sectionViewHolder.sectionTitle.setText(sectionName);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int section, int relativePosition, int absolutePosition) {
        List<RecyclerViewItem> itemsInSection = allData.get(section).getItemList();
        String taskName = itemsInSection.get(relativePosition).getTaskName();
        String time = itemsInSection.get(relativePosition).getTimeElapsed();

        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

        itemViewHolder.taskView.setText(taskName);
        itemViewHolder.timeView.setText(formattedTimer(Long.parseLong(time)));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        if(viewType == VIEW_TYPE_HEADER){
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.listfragment_list_section,parent,false);
            return  new SectionViewHolder(v);
        }else{
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.listfragment_list_item,parent,false);
            return new ItemViewHolder(v);
        }

    }

    public static class SectionViewHolder extends RecyclerView.ViewHolder {

        protected TextView sectionTitle;

        public SectionViewHolder(View itemView){
            super(itemView);

            sectionTitle = (TextView) itemView.findViewById(R.id.sectionTextView);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        protected TextView taskView;
        protected TextView timeView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            this.taskView = (TextView) itemView.findViewById(R.id.taskNameView);
            this.timeView = (TextView) itemView.findViewById(R.id.timeElapsedView);
        }

    }

    private String formattedTimer(long time){
        long hour = time/3600;
        long minutes = (time%3600)/60;
        long seconds = time - (hour * 3600 + minutes * 60);

        SimpleDateFormat simpleDateFormat =  new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(0,0,0,(int)hour,(int)minutes,(int)seconds);
        return simpleDateFormat.format(date);
    }
}
