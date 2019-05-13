package com.mao.smart_building.Activity;

import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mao.smart_building.Adapter.RecyclerViewAdapter;
import com.mao.smart_building.Pojo.Door;
import com.mao.smart_building.R;
import com.mao.smart_building.Service.GetInfoService;
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
 * Created by Mingpeidev on 2019/5/12.
 */

public class AlarmInfoActivity extends AppCompatActivity {

    private List<Door> doorList = new ArrayList<>();

    private RecyclerView recyclerView = null;

    private RecyclerViewAdapter recyclerViewAdapter = null;

    private Handler UIhandler;

    private TextView recycleview_residentname;
    private TextView recycleview_doorid;
    private TextView recycleview_sex;

    private TextView recycleview_human;
    private TextView recycleview_smoke;
    private Button btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        setContentView(R.layout.alarminfo_layout);

        initView();

        //停止服务
        Intent intent = new Intent(this, GetInfoService.class);
        stopService(intent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);//关闭通知栏
        notificationManager.cancel(1);

        GetAlarmInfo();

        recyclerViewAdapter.setOnClickListener(new RecyclerViewAdapter.OnClickListener() {
            @Override
            public void onClick(final int position) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AlarmInfoActivity.this);
                builder.setTitle("提示")
                        .setIcon(R.drawable.logo)
                        .setMessage("是否处理选中异常?")
                        .setPositiveButton("处理",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        int firstVisibleItems;//第一个可见item
                                        firstVisibleItems = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                                        View view1 = recyclerView.getChildAt(position - firstVisibleItems);

                                        TextView id_tv = view1.findViewById(R.id.recycleview_id);

                                        System.out.println("点击按钮：" + id_tv.getText().toString());

                                        HttpUtil.sendRequestWithOkhttpAsynGet("http://192.168.137.1:8080/Smart_Building/user/editAlarm?id=" + id_tv.getText().toString().trim(), new Callback() {
                                            @Override
                                            public void onFailure(Call call, IOException e) {

                                            }

                                            @Override
                                            public void onResponse(Call call, Response response) throws IOException {

                                                Message msg = Message.obtain();
                                                msg.what = 1;
                                                UIhandler.sendMessage(msg);
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
        });


        UIhandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        recyclerViewAdapter.refreshData(doorList);
                        System.out.println("获取报警信息！");
                        break;
                    case 1:
                        ToastUtil.showToast(AlarmInfoActivity.this, "处理异常成功", Toast.LENGTH_LONG);
                        break;
                    default:
                        break;
                }
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //打开服务
        Intent intent = new Intent(this, GetInfoService.class);
        startService(intent);
    }

    public void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycleview_alarm);

        recycleview_residentname = (TextView) findViewById(R.id.recycleview_residentname);
        recycleview_doorid = (TextView) findViewById(R.id.recycleview_doorid);
        recycleview_sex = (TextView) findViewById(R.id.recycleview_sex);
        recycleview_human = (TextView) findViewById(R.id.recycleview_human);
        recycleview_smoke = (TextView) findViewById(R.id.recycleview_smoke);
        btn = (Button) findViewById(R.id.recycleview_button);

        recycleview_residentname.setVisibility(View.GONE);
        recycleview_doorid.setVisibility(View.GONE);
        recycleview_sex.setVisibility(View.GONE);

        recycleview_human.setVisibility(View.VISIBLE);
        recycleview_smoke.setVisibility(View.VISIBLE);
        btn.setVisibility(View.INVISIBLE);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AlarmInfoActivity.this);//瀑布流
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerViewAdapter = new RecyclerViewAdapter(doorList);
        recyclerView.setAdapter(recyclerViewAdapter);//绑定适配器

        recyclerView.addItemDecoration(new DividerItemDecoration(AlarmInfoActivity.this, DividerItemDecoration.VERTICAL));//添加分割线
    }

    /**
     * 获取alarm信息
     */
    public void GetAlarmInfo() {

        doorList.clear();

        HttpUtil.sendRequestWithOkhttpAsynGet("http://192.168.137.1:8080/Smart_Building/user/getAlarm", new Callback() {
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

                        Door door = new Door(jsondata.getInt("id"), "", "", "", jsondata.getString("state"), jsondata.getString("date"), jsondata.getString("human"), jsondata.getString("smoke"));
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
