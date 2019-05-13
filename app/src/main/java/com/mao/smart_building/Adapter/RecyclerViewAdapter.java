package com.mao.smart_building.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mao.smart_building.Pojo.Door;
import com.mao.smart_building.R;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Mingpeidev on 2019/4/23.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {


    private List<Door> mdoor;

    public RecyclerViewAdapter(List<Door> list) {
        mdoor = list;
    }

    //定义点击事件接口
    private RecyclerViewAdapter.OnItemClickListener mOnItemClickListener;
    private RecyclerViewAdapter.OnItemLongClickListener mOnItemLongClickListener;
    private RecyclerViewAdapter.OnClickListener mOnClickListener;

    //定义一个点击事件的接口
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    public interface OnClickListener {
        void onClick(int position);
    }

    //定义设置点击事件监听的方法
    public void setOnItemClickListener(RecyclerViewAdapter.OnItemClickListener mOnItemClickListener) {//按钮点击方法
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setOnItemLongClickListener(RecyclerViewAdapter.OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    public void setOnClickListener(RecyclerViewAdapter.OnClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView id;
        TextView residentname;
        TextView doorid;
        TextView sex;
        TextView state;
        TextView time;

        TextView human;
        TextView smoke;
        Button btn;

        public ViewHolder(View itemView) {
            super(itemView);
            id = (TextView) itemView.findViewById(R.id.recycleview_id);
            residentname = (TextView) itemView.findViewById(R.id.recycleview_residentname);
            doorid = (TextView) itemView.findViewById(R.id.recycleview_doorid);
            sex = (TextView) itemView.findViewById(R.id.recycleview_sex);
            state = (TextView) itemView.findViewById(R.id.recycleview_state);
            time = (TextView) itemView.findViewById(R.id.recycleview_time);

            human = (TextView) itemView.findViewById(R.id.recycleview_human);
            smoke = (TextView) itemView.findViewById(R.id.recycleview_smoke);
            btn = (Button) itemView.findViewById(R.id.recycleview_button);

        }
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_item, parent, false);
        RecyclerViewAdapter.ViewHolder viewHolder = new RecyclerViewAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewAdapter.ViewHolder holder, int position) {

        Door door = mdoor.get(position);

        holder.id.setText(String.valueOf(door.getId()));//设置元素值

        if (!door.getResidentname().equals("")) {
            holder.residentname.setText(door.getResidentname());
            holder.doorid.setText(door.getDoorid());
        }

        if (!door.getSex().equals("")) {
            holder.sex.setText(door.getSex());
        }

        if (!door.getHuman().equals("")) {
            holder.human.setText(door.getHuman());
            holder.smoke.setText(door.getSmoke());
        }

        if (!door.getTime().equals("")) {//出行信息，报警的：state，time

            holder.state.setText(door.getState());

            SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");// 指定类型

            Long date = new Long(Long.valueOf(door.getTime()));

            String time = sdfTime.format(date);// sqltime转为date指定类型

            holder.time.setText(time);
        }


        if (door.getResidentname().equals("")) {//如为报警表
            holder.human.setVisibility(View.VISIBLE);
            holder.smoke.setVisibility(View.VISIBLE);
            holder.btn.setVisibility(View.VISIBLE);

            holder.residentname.setVisibility(View.GONE);
            holder.doorid.setVisibility(View.GONE);
            holder.sex.setVisibility(View.GONE);
        } else {

            holder.human.setVisibility(View.GONE);
            holder.smoke.setVisibility(View.GONE);
            holder.btn.setVisibility(View.GONE);

            holder.residentname.setVisibility(View.VISIBLE);
            holder.doorid.setVisibility(View.VISIBLE);
            holder.sex.setVisibility(View.VISIBLE);

            if (door.getState().equals("")) {//如为居民表
                holder.state.setVisibility(View.GONE);
                holder.time.setVisibility(View.GONE);
                holder.sex.setVisibility(View.VISIBLE);
            } else {
                holder.sex.setVisibility(View.GONE);
                holder.state.setVisibility(View.VISIBLE);
                holder.time.setVisibility(View.VISIBLE);
            }
        }

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = holder.getLayoutPosition();
                    //在TextView的地方进行监听点击事件，并且实现接口
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }

        if (mOnItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int position = holder.getLayoutPosition();
                    mOnItemLongClickListener.onItemLongClick(holder.itemView, position);
                    return true;
                }
            });
        }

        if (mOnClickListener != null) {
            holder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = holder.getLayoutPosition();
                    mOnClickListener.onClick(position);
                }
            });
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
