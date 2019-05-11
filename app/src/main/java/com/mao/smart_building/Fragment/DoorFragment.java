package com.mao.smart_building.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mao.smart_building.Adapter.RecyclerViewAdapter;
import com.mao.smart_building.Pojo.Door;
import com.mao.smart_building.R;
import com.mao.smart_building.Util.HttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Mingpeidev on 2019/4/2.
 */

public class DoorFragment extends Fragment {

    private Handler UIhandler;

    private RecyclerView recyclerView;
    private Button record_Btn;
    private Button resident_Btn;

    private TextView recycleview_sex;
    private TextView recycleview_state;
    private TextView recycleview_time;

    private List<Door> doorList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.door_layout, null);

        recyclerView = view.findViewById(R.id.recycleview_all);
        record_Btn = view.findViewById(R.id.record_Btn);
        resident_Btn = view.findViewById(R.id.resident_Btn);

        recycleview_sex = view.findViewById(R.id.recycleview_sex);
        recycleview_state = view.findViewById(R.id.recycleview_state);
        recycleview_time = view.findViewById(R.id.recycleview_time);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        recycleview_state.setVisibility(View.VISIBLE);
        recycleview_time.setVisibility(View.VISIBLE);
        recycleview_sex.setVisibility(View.GONE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());//瀑布流
        recyclerView.setLayoutManager(linearLayoutManager);

        final RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(doorList);
        recyclerView.setAdapter(recyclerViewAdapter);//绑定适配器

        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));//添加分割线


        record_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetTriprecord();
            }
        });

        resident_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetResident();
            }
        });

        UIhandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        recyclerViewAdapter.refreshData(doorList);
                        System.out.println("获取居民信息！");
                        break;
                    case 1:
                        recyclerViewAdapter.refreshData(doorList);
                        System.out.println("获取出行信息！");
                        break;
                    default:
                        break;
                }
            }
        };
    }

    /**
     * 获取出行记录
     */
    public void GetTriprecord() {

        recycleview_state.setVisibility(View.VISIBLE);
        recycleview_time.setVisibility(View.VISIBLE);
        recycleview_sex.setVisibility(View.GONE);

        doorList.clear();

        HttpUtil.sendRequestWithOkhttpAsynGet("http://192.168.137.1:8080/Smart_Building/user/getTriprecordList?page=1&limit=3", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {

                    JSONObject jsonObject = new JSONObject(response.body().string());

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsondata = new JSONObject(jsonArray.get(i).toString());

                        Door door = new Door(jsondata.getInt("id"), jsondata.getString("residentname"), jsondata.getString("doorid"), "", jsondata.getString("state"), jsondata.getString("time"));
                        doorList.add(door);

                    }

                    Message msg = Message.obtain();
                    msg.what = 1;
                    UIhandler.sendMessage(msg);

                    System.out.println(jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 获取居民信息
     */
    public void GetResident() {

        recycleview_state.setVisibility(View.GONE);
        recycleview_time.setVisibility(View.GONE);
        recycleview_sex.setVisibility(View.VISIBLE);

        doorList.clear();

        HttpUtil.sendRequestWithOkhttpAsynGet("http://192.168.137.1:8080/Smart_Building/user/getResidentList?page=1&limit=2", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {

                    JSONObject jsonObject = new JSONObject(response.body().string());

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsondata = new JSONObject(jsonArray.get(i).toString());

                        Door door = new Door(jsondata.getInt("id"), jsondata.getString("residentname"), jsondata.getString("doorid"), jsondata.getString("sex"), "", "");
                        doorList.add(door);

                    }

                    Message msg = Message.obtain();
                    msg.what = 0;
                    UIhandler.sendMessage(msg);

                    System.out.println(jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
