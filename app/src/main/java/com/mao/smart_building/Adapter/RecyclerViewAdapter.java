package com.mao.smart_building.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mao.smart_building.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mingpeidev on 2019/4/23.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {


    private List<String> mlist = new ArrayList<String>();

    public RecyclerViewAdapter(List<String> list) {
        mlist = list;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.recycleview_item);

        }
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_item, parent, false);
        RecyclerViewAdapter.ViewHolder viewHolder = new RecyclerViewAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        holder.textView.setText(mlist.get(position));//设置元素值
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public void refreshData(List<String> valueList) {
        this.mlist = valueList;
        notifyDataSetChanged();
    }
}
