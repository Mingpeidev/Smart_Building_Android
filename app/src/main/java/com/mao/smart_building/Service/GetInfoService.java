package com.mao.smart_building.Service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.mao.smart_building.Receiver.AlarmReceiver;
import com.mao.smart_building.Util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Mingpeidev on 2019/5/12.
 */

public class GetInfoService extends Service {

    private AlarmReceiver alarmReceiver;
    private String state = "true";//判断service是否关闭

    private MyTread myTread;
    private int count = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //动态注册广播，相当于一个过滤器，只接收此action广播
        alarmReceiver = new AlarmReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.mao.alarminfo");
        registerReceiver(alarmReceiver, intentFilter);

        state = "true";
        //读取数据进程
        myTread = new MyTread();
        myTread.start();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //注销广播
        state = "false";
        unregisterReceiver(alarmReceiver);
    }

    private class MyTread extends Thread {
        @Override
        public void run() {
            super.run();
            while (true) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (state == "false") {
                    break;
                }

                GetTriprecord();

                //动态注册方式广播发送
                Intent intent = new Intent();
                intent.setAction("com.mao.alarminfo");
                intent.putExtra("count", count);
                sendBroadcast(intent);

                /*
                //不注册也可以使用广播？？？？？
                Intent intent = new Intent(GetInfoService.this,AlarmReceiver.class);
                intent.setAction("com.mao.alarminfoaaa");
                intent.putExtra("count", 1);
                sendBroadcast(intent);*/
            }

        }
    }

    /**
     * 获取出行记录
     */
    public void GetTriprecord() {

        HttpUtil.sendRequestWithOkhttpAsynGet("http://192.168.137.1:8080/Smart_Building/user/getAlarm", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {

                    JSONObject jsonObject = new JSONObject(response.body().string());

                    count = jsonObject.getInt("count");//获取alarm数量

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
