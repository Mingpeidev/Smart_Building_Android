package com.mao.smart_building.Fragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mao.smart_building.Adapter.RecyclerViewAdapter;
import com.mao.smart_building.Pojo.Door;
import com.mao.smart_building.R;
import com.mao.smart_building.Util.HttpUtil;
import com.mao.smart_building.Util.ToastUtil;

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

    //状态判断值，判断是否使用适配器点击事件
    private Boolean btnstate = true;

    private Handler UIhandler;

    private RecyclerView recyclerView;
    private Button record_Btn;
    private Button resident_Btn;

    private TextView recycleview_sex;
    private TextView recycleview_state;
    private TextView recycleview_time;

    private String residentname = null;

    String idtoservice;
    String residentnametosercice = "";
    String dooidtosercice = "";
    String sextosercice = "";

    String datatoclient = "";

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

        btnstate = true;

        recycleview_state.setVisibility(View.VISIBLE);
        recycleview_time.setVisibility(View.VISIBLE);
        recycleview_sex.setVisibility(View.GONE);

        record_Btn.setBackgroundColor(Color.parseColor("#1cf718"));
        resident_Btn.setBackgroundColor(Color.parseColor("#858be9"));

        //此及以上，初始化界面
        GetTriprecord();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());//瀑布流
        recyclerView.setLayoutManager(linearLayoutManager);

        final RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(doorList);
        recyclerView.setAdapter(recyclerViewAdapter);//绑定适配器

        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));//添加分割线


        record_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnstate = true;
                record_Btn.setBackgroundColor(Color.parseColor("#1cf718"));
                resident_Btn.setBackgroundColor(Color.parseColor("#858be9"));
                GetTriprecord();
            }
        });

        resident_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnstate = false;
                record_Btn.setBackgroundColor(Color.parseColor("#858be9"));
                resident_Btn.setBackgroundColor(Color.parseColor("#1cf718"));
                GetResident();
            }
        });

        recyclerViewAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (btnstate == false) {

                    int firstVisibleItems;//第一个可见item
                    firstVisibleItems = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                    View view1 = recyclerView.getChildAt(position - firstVisibleItems);

                    TextView id_tv = view1.findViewById(R.id.recycleview_id);
                    TextView residentname_tv = view1.findViewById(R.id.recycleview_residentname);
                    TextView doorid_tv = view1.findViewById(R.id.recycleview_doorid);
                    TextView sex_tv = view1.findViewById(R.id.recycleview_sex);

                    //初始化popwindow
                    View view2 = View.inflate(getActivity(), R.layout.resident_popwindow, null);
                    final PopupWindow popupWindow = new PopupWindow(view2, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    popupWindow.setFocusable(true);
                    popupWindow.setAnimationStyle(R.style.popwindow_anim);
                    popupWindow.update();
                    popupWindow.showAtLocation(view2, Gravity.RIGHT | Gravity.CENTER_HORIZONTAL, 0, 0);

                    //初始化
                    final EditText residentname_TV = view2.findViewById(R.id.residentname_tv);
                    final EditText dooid_TV = view2.findViewById(R.id.doorid_tv);
                    Spinner sex_TV = view2.findViewById(R.id.sexselect_Sp);
                    Button cancel_BTN = view2.findViewById(R.id.canceledit_Btn);
                    Button submit_BTN = view2.findViewById(R.id.submit_Btn);

                    //设置初值
                    idtoservice = id_tv.getText().toString();
                    residentname_TV.setText(residentname_tv.getText().toString());
                    dooid_TV.setText(doorid_tv.getText().toString());
                    if (sex_tv.getText().toString().equals("男")) {
                        sex_TV.setSelection(0, true);
                        sextosercice = "男";
                    } else {
                        sex_TV.setSelection(1, true);
                        sextosercice = "女";
                    }

                    //popwindow内部按钮点击
                    sex_TV.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                            String s = adapterView.getItemAtPosition(position).toString();
                            sextosercice = s;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    cancel_BTN.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popupWindow.dismiss();
                        }
                    });

                    submit_BTN.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            residentnametosercice = residentname_TV.getText().toString();
                            dooidtosercice = dooid_TV.getText().toString();

                            //修改居民信息
                            HttpUtil.sendRequestWithOkhttpAsynGet("http://192.168.137.1:8080/Smart_Building/user/editResidentInfo?id=" + idtoservice + "&residentname=" + residentnametosercice + "&doorid=" + dooidtosercice
                                    + "&sex=" + sextosercice, new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    Message msg = Message.obtain();
                                    msg.what = 4;
                                    UIhandler.sendMessage(msg);
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {

                                    try {
                                        JSONObject jsonObject = new JSONObject(response.body().string());
                                        datatoclient = jsonObject.getString("data");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Message msg = Message.obtain();
                                    msg.what = 3;
                                    UIhandler.sendMessage(msg);
                                }
                            });

                            popupWindow.dismiss();

                        }
                    });

                    System.out.println("短按：" + id_tv.getText().toString()
                            + "@" + residentname_tv.getText().toString()
                            + "@" + doorid_tv.getText().toString()
                            + "@" + sex_tv.getText().toString());

                }
            }
        });

        recyclerViewAdapter.setOnItemLongClickListener(new RecyclerViewAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, final int position) {

                if (btnstate == false) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("提示")
                            .setIcon(R.drawable.logo)
                            .setMessage("是否删除选中居民信息?")
                            .setPositiveButton("删除",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            int firstVisibleItems;//第一个可见item
                                            firstVisibleItems = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                                            View view1 = recyclerView.getChildAt(position - firstVisibleItems);

                                            TextView id_tv = view1.findViewById(R.id.recycleview_id);
                                            final TextView residentname_tv = view1.findViewById(R.id.recycleview_residentname);
                                            TextView doorid_tv = view1.findViewById(R.id.recycleview_doorid);
                                            TextView sex_tv = view1.findViewById(R.id.recycleview_sex);

                                            System.out.println("长按：" + id_tv.getText().toString()
                                                    + "@" + residentname_tv.getText().toString()
                                                    + "@" + doorid_tv.getText().toString()
                                                    + "@" + sex_tv.getText().toString());

                                            HttpUtil.sendRequestWithOkhttpAsynGet("http://192.168.137.1:8080/Smart_Building/user/deleteResidentInfo?id=" + id_tv.getText().toString().trim(), new Callback() {
                                                @Override
                                                public void onFailure(Call call, IOException e) {
                                                    Message msg = Message.obtain();
                                                    msg.what = 4;
                                                    UIhandler.sendMessage(msg);
                                                }

                                                @Override
                                                public void onResponse(Call call, Response response) throws IOException {

                                                    Message msg = Message.obtain();
                                                    msg.what = 2;
                                                    UIhandler.sendMessage(msg);

                                                    residentname = residentname_tv.getText().toString();

                                                }
                                            });

                                            doorList.remove(position);
                                            recyclerViewAdapter.notifyItemRemoved(position);

                                            dialog.dismiss();

                                        }
                                    })
                            .setNegativeButton("取消",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).create().show();
                }

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
                    case 2:
                        ToastUtil.showToast(getActivity(), "删除住户 " + residentname + " 的信息成功", Toast.LENGTH_LONG);
                        break;
                    case 3:
                        GetResident();
                        //修改居民提示
                        ToastUtil.showToast(getActivity(), datatoclient, Toast.LENGTH_LONG);
                        break;
                    case 4:
                        ToastUtil.showToast(getActivity(), "未联网！请联网", Toast.LENGTH_LONG);
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

        HttpUtil.sendRequestWithOkhttpAsynGet("http://192.168.137.1:8080/Smart_Building/user/getTriprecordList?page=1&limit=100", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg = Message.obtain();
                msg.what = 4;
                UIhandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {

                    JSONObject jsonObject = new JSONObject(response.body().string());

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsondata = new JSONObject(jsonArray.get(i).toString());

                        Door door = new Door(jsondata.getInt("id"), jsondata.getString("residentname"), jsondata.getString("doorid"), "", jsondata.getString("state"), jsondata.getString("time"), "", "");
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

        HttpUtil.sendRequestWithOkhttpAsynGet("http://192.168.137.1:8080/Smart_Building/user/getResidentList?page=1&limit=100", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg = Message.obtain();
                msg.what = 4;
                UIhandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {

                    JSONObject jsonObject = new JSONObject(response.body().string());

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsondata = new JSONObject(jsonArray.get(i).toString());

                        Door door = new Door(jsondata.getInt("id"), jsondata.getString("residentname"), jsondata.getString("doorid"), jsondata.getString("sex"), "", "", "", "");
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
