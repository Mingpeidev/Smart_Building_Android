package com.mao.smart_building.Activity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mao.smart_building.R;
import com.mao.smart_building.Util.HttpUtil;
import com.mao.smart_building.Util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Mingpeidev on 2019/4/22.
 */

public class SmartsetActivity extends AppCompatActivity implements View.OnClickListener {

    private Handler UIhandler;

    private ImageButton cancel_Btn = null;

    private Button timeonselect_Btn = null;
    private Button timeoffselect_Btn = null;
    private Button smartselect_Btn = null;
    private Button submitset_Btn = null;

    private Spinner tempselect_Sp = null;
    private Spinner humiselect_Sp = null;
    private Spinner lightselect_Sp = null;

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

        initView();

        getData();//获取设置数据

        UIhandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {//初始化
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        System.out.println(Stemp + "@" + Shumi + "@" + Slight);
                        tempselect_Sp.setSelection(Integer.valueOf(Stemp) - 20, true);
                        humiselect_Sp.setSelection((Integer.valueOf(Shumi) - 30) / 10, true);
                        if (Slight == "null") {
                            System.out.println(Slight);
                            lightselect_Sp.setSelection(0, true);
                        }
                        if (Slight.equals("300")) {
                            System.out.println(Slight);
                            lightselect_Sp.setSelection(1, true);
                        }
                        if (Slight.equals("400")) {
                            System.out.println(Slight);
                            lightselect_Sp.setSelection(2, true);
                        }
                        if (Slight.equals("500")) {
                            System.out.println(Slight);
                            lightselect_Sp.setSelection(3, true);
                        }

                        timeonselect_Btn.setText(Stimeon);
                        timeoffselect_Btn.setText(Stimeoff);
                        smartselect_Btn.setText(Smart);
                        break;
                    default:
                        break;
                }
            }
        };

        tempselect_Sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String s = adapterView.getItemAtPosition(position).toString();
                Stemp = s;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        humiselect_Sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String s = adapterView.getItemAtPosition(position).toString();
                Shumi = s;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        lightselect_Sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String s = adapterView.getItemAtPosition(position).toString();
                Slight = s;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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

                    Stemp = jsonObject.getJSONObject("data").getString("temp");
                    Shumi = jsonObject.getJSONObject("data").getString("humi");
                    Slight = jsonObject.getJSONObject("data").getString("light");
                    Smart = jsonObject.getJSONObject("data").getString("smart");

                    Message msg = Message.obtain();
                    msg.what = 0;
                    UIhandler.sendMessage(msg);


                    Log.d("haha", "onResponse: " + Stemp + "@" + Shumi + "@" + Slight + "@" + Stimeon + "@" + Stimeoff + "@" + Smart);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void initView() {

        tempselect_Sp = findViewById(R.id.tempselect_Sp);
        humiselect_Sp = findViewById(R.id.humiselect_Sp);
        lightselect_Sp = findViewById(R.id.lightselect_Sp);

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
            case R.id.timeonselect_Btn:
                if (Stimeon != null) {
                    String[] timeon = Stimeon.split(":");
                    new TimePickerDialog(SmartsetActivity.this, android.R.style.Theme_Holo_Light_Panel, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hourofday, int minute) {
                            timeonselect_Btn.setText(hourofday + ":" + minute);
                            Stimeon = hourofday + ":" + minute;
                        }
                    }, Integer.valueOf(timeon[0]), Integer.valueOf(timeon[1]), true).show();
                } else {
                    new TimePickerDialog(SmartsetActivity.this, android.R.style.Theme_Holo_Light_Panel, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hourofday, int minute) {
                            timeonselect_Btn.setText(hourofday + ":" + minute);
                            Stimeon = hourofday + ":" + minute;
                        }
                    }, 0, 0, true).show();
                }
                break;
            case R.id.timeoffselect_Btn:
                if (Stimeoff != null) {
                    String[] timeoff = Stimeoff.split(":");
                    new TimePickerDialog(SmartsetActivity.this, android.R.style.Theme_Holo_Light_Panel, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hourofday, int minute) {
                            timeoffselect_Btn.setText(hourofday + ":" + minute);
                            Stimeoff = hourofday + ":" + minute;
                        }
                    }, Integer.valueOf(timeoff[0]), Integer.valueOf(timeoff[1]), true).show();
                } else {
                    new TimePickerDialog(SmartsetActivity.this, android.R.style.Theme_Holo_Light_Panel, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hourofday, int minute) {
                            timeoffselect_Btn.setText(hourofday + ":" + minute);
                            Stimeoff = hourofday + ":" + minute;
                        }
                    }, 0, 0, true).show();
                }
                break;
            case R.id.smartselect_Btn:
                if (Smart != null) {
                    if (Smart.equals("on")) {
                        smartselect_Btn.setText("off");
                        Smart = "off";
                    } else if (Smart.equals("off")) {
                        smartselect_Btn.setText("on");
                        Smart = "on";
                    }
                } else {
                    smartselect_Btn.setText("未联网");
                }
                break;
            case R.id.cancel_Btn:
                finish();
                break;
            case R.id.submitset_Btn:
                if (Smart != null) {
                    RequestBody body = new FormBody.Builder()
                            .add("temp", Stemp)
                            .add("humi", Shumi)
                            .add("light", Slight)
                            .add("timeon", Stimeon)
                            .add("timeoff", Stimeoff)
                            .add("smart", Smart)
                            .build();
                    HttpUtil.sendRequestWithOkhttpPost("http://192.168.137.1:8080/Smart_Building/user/updateSetting", body, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                        }
                    });
                    ToastUtil.showToast(SmartsetActivity.this, "设置成功！", Toast.LENGTH_LONG);
                    finish();
                } else {
                    ToastUtil.showToast(SmartsetActivity.this, "未联网，请联网后再设置！", Toast.LENGTH_LONG);
                    finish();
                }
                break;
            default:
                break;
        }
    }
}
