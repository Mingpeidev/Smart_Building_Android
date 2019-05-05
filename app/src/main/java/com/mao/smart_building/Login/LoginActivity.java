package com.mao.smart_building.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.mao.smart_building.Activity.MainActivity;
import com.mao.smart_building.R;
import com.mao.smart_building.Util.HttpUtil;
import com.mao.smart_building.Util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Mingpeidev on 2019/3/15.
 */

public class LoginActivity extends AppCompatActivity {

    private Handler loginhander;

    private EditText usernameEdit = null;
    private EditText psdEdit = null;
    private Button loginBtn = null;
    private Button registerBtn = null;
    private CheckBox rememberpsdBox;
    private Button jumpBtn = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        setContentView(R.layout.login_layout);

        usernameEdit = (EditText) findViewById(R.id.username_Edit);
        psdEdit = (EditText) findViewById(R.id.password_Edit);
        loginBtn = (Button) findViewById(R.id.login_Btn);
        registerBtn = (Button) findViewById(R.id.register_Btn);
        rememberpsdBox = (CheckBox) findViewById(R.id.rememberpassword_Box);
        jumpBtn = (Button) findViewById(R.id.jump_Btn);

        psdEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());//设置密码不可见
        psdEdit.setSelection(psdEdit.getText().toString().length());//设置光标在不可见后


        SharedPreferences spf = getSharedPreferences("loginsuccess", MODE_PRIVATE);//保存记住密码：用户名，密码，记住密码状态
        final SharedPreferences.Editor editor = spf.edit();
        boolean isRemember = spf.getBoolean("rememberpassword", false);
        if (isRemember) {
            //将账号和密码都设置到登陆界面文本中
            String username = spf.getString("username", "");
            String password = spf.getString("password", "");
            usernameEdit.setText(username);
            psdEdit.setText(password);
            rememberpsdBox.setChecked(true);
        }


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (usernameEdit.getText().toString().trim().equals("")) {
                    ToastUtil.showToast(LoginActivity.this, "用户名不能为空！", Toast.LENGTH_SHORT);
                } else if (usernameEdit.getText().length() < 3 || usernameEdit.getText().length() > 10) {
                    ToastUtil.showToast(LoginActivity.this, "用户名在3-10位之间！", Toast.LENGTH_SHORT);
                } else if (psdEdit.getText().toString().trim().equals("")) {
                    ToastUtil.showToast(LoginActivity.this, "密码不能为空！", Toast.LENGTH_SHORT);
                } else if (psdEdit.getText().length() < 6 || psdEdit.getText().length() > 10) {
                    ToastUtil.showToast(LoginActivity.this, "密码在6-10位之间！", Toast.LENGTH_SHORT);
                } else {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            int logindata = 2;

                            RequestBody body = new FormBody.Builder()
                                    .add("username", usernameEdit.getText().toString().trim())
                                    .add("password", psdEdit.getText().toString().trim())
                                    .build();
                            //获取用户信息
                            String message = HttpUtil.sendRequestWithOkhttpSynPost("http://192.168.137.1:8080/Smart_Building/user/logininphone"
                                    , body);

                            //解析json
                            try {
                                if (message != null) {
                                    JSONObject jsonObject = new JSONObject(message);
                                    logindata = jsonObject.getInt("data");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Message msg = Message.obtain();
                            msg.what = logindata;
                            loginhander.sendMessage(msg);

                            Log.d("haha", "login: " + logindata);

                        }
                    }).start();
                }

            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        jumpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        loginhander = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        ToastUtil.showToast(LoginActivity.this, "登陆失败！", Toast.LENGTH_SHORT);
                        break;
                    case 1:
                        if (rememberpsdBox.isChecked()) {//记住密码，登陆用
                            editor.putBoolean("rememberpassword", true);
                            editor.putString("username", usernameEdit.getText().toString().trim());
                            editor.putString("password", psdEdit.getText().toString().trim());
                        } else {
                            editor.putBoolean("rememberpassword", false);
                            editor.putString("username", usernameEdit.getText().toString().trim());
                            editor.putString("password", psdEdit.getText().toString().trim());
                        }
                        editor.apply();

                        Intent intent = new Intent();
                        ToastUtil.showToast(LoginActivity.this, "登陆成功！", Toast.LENGTH_SHORT);
                        intent.setClass(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case 2:
                        ToastUtil.showToast(LoginActivity.this, "网络未连接！", Toast.LENGTH_SHORT);
                        break;
                    default:
                        break;
                }
            }
        };
    }


}
