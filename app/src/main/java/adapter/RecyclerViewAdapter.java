package adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jerrylee.mytime.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import item.RecyclerViewItem;

/**
 * Created by jerrylee on 1/3/17.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.CustomViewHolder> {

    private List<RecyclerViewItem> itemList;


    public RecyclerViewAdapter(List<RecyclerViewItem> itemList){
        this.itemList = itemList;
}

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listfragment_cardview,null);
        CustomViewHolder customViewHolder = new CustomViewHolder(view);

        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        RecyclerViewItem recyclerViewItem = itemList.get(position);

        holder.taskView.setText(recyclerViewItem.getTaskName());
        holder.timeView.setText(recyclerViewItem.getTimeElapsed());
    }

    @Override
    public int getItemCount() {
        return (null != itemList ? itemList.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder{
        protected TextView taskView;
        protected TextView timeView;

        public CustomViewHolder(View itemView) {
            super(itemView);
            this.taskView = (TextView) itemView.findViewById(R.id.taskNameView);
            this.timeView = (TextView) itemView.findViewById(R.id.timeElapsedView);
        }

    }
}

