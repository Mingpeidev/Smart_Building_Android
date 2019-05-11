package com.mao.smart_building.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mao.smart_building.Pojo.Door;
import com.mao.smart_building.R;

import java.util.List;

/**
 * Created by Mingpeidev on 2019/4/23.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {


    private List<Door> mdoor;

    public RecyclerViewAdapter(List<Door> list) {
        mdoor = list;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView id;
        TextView residentname;
        TextView doorid;
        TextView sex;
        TextView state;
        TextView time;

        public ViewHolder(View itemView) {
            super(itemView);
            id = (TextView) itemView.findViewById(R.id.recycleview_id);
            residentname = (TextView) itemView.findViewById(R.id.recycleview_residentname);
            doorid = (TextView) itemView.findViewById(R.id.recycleview_doorid);
            sex = (TextView) itemView.findViewById(R.id.recycleview_sex);
            state = (TextView) itemView.findViewById(R.id.recycleview_state);
            time = (TextView) itemView.findViewById(R.id.recycleview_time);

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

        Door door = mdoor.get(position);

        holder.id.setText(String.valueOf(door.getId()));//设置元素值
        holder.residentname.setText(door.getResidentname());
        holder.doorid.setText(door.getDoorid());
        holder.sex.setText(door.getSex());
        holder.state.setText(door.getState());
        holder.time.setText(door.getTime());

        if (door.getState().equals("")) {
            holder.state.setVisibility(View.GONE);
            holder.time.setVisibility(View.GONE);
            holder.sex.setVisibility(View.VISIBLE);
        } else {
            holder.sex.setVisibility(View.GONE);
            holder.state.setVisibility(View.VISIBLE);
            holder.time.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mdoor.size();
    }

    public void refreshData(List<Door> valueList) {
        this.mdoor = valueList;
        notifyDataSetChanged();
    }
}
