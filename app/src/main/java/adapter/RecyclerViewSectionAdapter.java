package adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.example.jerrylee.mytime.R;
import com.example.jerrylee.mytime.TimeFormActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import item.DataItem;
import item.DataModel;

/**
 * Created by Jerry on 1/12/2017.
 */

public class RecyclerViewSectionAdapter extends SectionedRecyclerViewAdapter<RecyclerView.ViewHolder> {
    private List<DataModel> allData;

    private Context context;

    public RecyclerViewSectionAdapter(List<DataModel> itemList,Context context){
        this.allData = itemList;
        this.context = context;
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int section, final int relativePosition, final int absolutePosition) {
        final List<DataItem> itemsInSection = allData.get(section).getItemList();
        String taskName = itemsInSection.get(relativePosition).getTaskName();
        String time = itemsInSection.get(relativePosition).getElapsedTimeString();

        final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

        itemViewHolder.taskView.setText(taskName);
        itemViewHolder.timeView.setText(formattedTimer(Long.parseLong(time)));


        itemViewHolder.taskView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DataItem item = itemsInSection.get(relativePosition);
                Intent intent = new Intent(itemViewHolder.taskView.getContext(), TimeFormActivity.class);
                intent.putExtra(TimeFormActivity.MODE,TimeFormActivity.EDIT_MODE);
                intent.putExtra(TimeFormActivity.ITEM,item);

                context.startActivity(intent);
            }
        });
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
