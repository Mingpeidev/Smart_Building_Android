package com.mao.smart_building.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mao.smart_building.Activity.SmartsetActivity;
import com.mao.smart_building.Login.LoginActivity;
import com.mao.smart_building.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Mingpeidev on 2019/4/2.
 */

public class SetFragment extends Fragment {
    private Button exitBtn = null;
    private Button smartset_Btn = null;
    private TextView welcome_Text = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.set_layout, null);
        exitBtn = view.findViewById(R.id.exitBtn);
        smartset_Btn = view.findViewById(R.id.samrtset_Btn);
        welcome_Text = view.findViewById(R.id.welcome_Text);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        //获取登录数据,并显示
        SharedPreferences spf = this.getActivity().getSharedPreferences("loginsuccess", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = spf.edit();
        String username = spf.getString("username", "");
        welcome_Text.setText("欢迎您！" + username);

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*editor.clear();//清空登录信息。注销
                editor.apply();*/

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        smartset_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SmartsetActivity.class);
                startActivity(intent);
            }
        });
    }
}
