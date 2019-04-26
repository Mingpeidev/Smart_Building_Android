package com.mao.smart_building.Activity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TimePicker;

import com.mao.smart_building.R;
import com.mao.smart_building.Util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Mingpeidev on 2019/4/22.
 */

public class SmartsetActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton cancel_Btn = null;

    private Button tempselect_Btn = null;
    private Button humiselect_Btn = null;
    private Button lightselect_Btn = null;
    private Button timeonselect_Btn = null;
    private Button timeoffselect_Btn = null;
    private Button smartselect_Btn = null;
    private Button submitset_Btn = null;


    private String Stemp = null;
    private String Shumi = null;
    private String Slight = null;
    private String Stimeon = null;
    private String Stimeoff = null;
    private String Smart = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        setContentView(R.layout.smartset_layout);

        getData();

        initView();
    }

    public void getData() {
        HttpUtil.sendRequestWithOkhttpGet("http://192.168.137.1:8080/Smart_Building/user/selectSetting", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());

                    Stimeon = jsonObject.getString("timeon");
                    Stimeoff = jsonObject.getString("timeoff");
                    Smart = jsonObject.getString("smart");

                    Stemp = jsonObject.getJSONObject("data").getString("temp");
                    Shumi = jsonObject.getJSONObject("data").getString("humi");
                    Slight = jsonObject.getJSONObject("data").getString("light");

                    /*Message msg = Message.obtain();
                    msg.what = 0;
                    UIHandler.sendMessage(msg);*/


                    Log.d("haha", "onResponse: " + Stemp + "@" + Shumi + "@" + Slight + "@" + Stimeon + "@" + Stimeoff + "@" + Smart);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void initView() {
        tempselect_Btn = (Button) findViewById(R.id.tempselect_Btn);
        tempselect_Btn.setOnClickListener(this);
        humiselect_Btn = (Button) findViewById(R.id.humiselect_Btn);
        humiselect_Btn.setOnClickListener(this);
        lightselect_Btn = (Button) findViewById(R.id.lightselect_Btn);
        lightselect_Btn.setOnClickListener(this);
        timeonselect_Btn = (Button) findViewById(R.id.timeonselect_Btn);
        timeonselect_Btn.setOnClickListener(this);
        timeoffselect_Btn = (Button) findViewById(R.id.timeoffselect_Btn);
        timeoffselect_Btn.setOnClickListener(this);
        smartselect_Btn = (Button) findViewById(R.id.smartselect_Btn);
        smartselect_Btn.setOnClickListener(this);


        submitset_Btn = (Button) findViewById(R.id.submitset_Btn);
        submitset_Btn.setOnClickListener(this);
        cancel_Btn = (ImageButton) findViewById(R.id.cancel_Btn);
        cancel_Btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tempselect_Btn:
                break;
            case R.id.humiselect_Btn:
                break;
            case R.id.lightselect_Btn:
                break;
            case R.id.timeonselect_Btn:
                new TimePickerDialog(SmartsetActivity.this, android.R.style.Theme_Holo_Light_Panel, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourofday, int minute) {
                        timeonselect_Btn.setText(hourofday + ":" + minute);
                    }
                }, 0, 0, true).show();
                break;
            case R.id.timeoffselect_Btn:
                new TimePickerDialog(SmartsetActivity.this, android.R.style.Theme_Holo_Light_Panel, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourofday, int minute) {
                        timeoffselect_Btn.setText(hourofday + ":" + minute);
                    }
                }, 0, 0, true).show();
                break;
            case R.id.smartselect_Btn:
                break;
            case R.id.cancel_Btn:
                finish();
                break;
            case R.id.submitset_Btn:
                break;
            default:
                break;
        }
    }
}
