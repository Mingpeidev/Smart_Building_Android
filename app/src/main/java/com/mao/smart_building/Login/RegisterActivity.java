package com.mao.smart_building.Login;

import android.content.Intent;
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
import android.widget.EditText;
import android.widget.Toast;

import com.mao.smart_building.R;
import com.mao.smart_building.Util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Mingpeidev on 2019/3/17.
 */

public class RegisterActivity extends AppCompatActivity {

    private Handler registerhander;

    private EditText usernameEdit1 = null;
    private EditText psdEdit1 = null;
    private EditText psdEdit2 = null;
    private Button registerBtn1 = null;
    private Button registercancel_Btn = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        setContentView(R.layout.register_layout);

        usernameEdit1 = (EditText) findViewById(R.id.username_Edit1);
        psdEdit1 = (EditText) findViewById(R.id.password_Edit1);
        psdEdit2 = (EditText) findViewById(R.id.password_Edit2);
        registerBtn1 = (Button) findViewById(R.id.register_Btn1);
        registercancel_Btn = (Button) findViewById(R.id.registercancel_Btn);

        psdEdit1.setTransformationMethod(PasswordTransformationMethod.getInstance());//设置密码不可见
        psdEdit1.setSelection(psdEdit1.getText().toString().length());//设置光标在不可见后
        psdEdit2.setTransformationMethod(PasswordTransformationMethod.getInstance());//设置密码不可见
        psdEdit2.setSelection(psdEdit2.getText().toString().length());//设置光标在不可见后

        registerBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (usernameEdit1.getText().toString().trim().equals("")) {
                    ToastUtil.showToast(RegisterActivity.this, "用户名不能为空！", Toast.LENGTH_SHORT);
                } else if (usernameEdit1.getText().length() < 3 || usernameEdit1.getText().length() > 10) {
                    ToastUtil.showToast(RegisterActivity.this, "用户名在3-10位之间！", Toast.LENGTH_SHORT);
                } else if (psdEdit1.getText().toString().trim().equals("") || psdEdit2.getText().toString().trim().equals("")) {
                    ToastUtil.showToast(RegisterActivity.this, "密码不能为空！", Toast.LENGTH_SHORT);
                } else if (psdEdit1.getText().length() < 6 || psdEdit1.getText().length() > 10) {
                    ToastUtil.showToast(RegisterActivity.this, "密码在6-10位之间！", Toast.LENGTH_SHORT);
                } else if (!psdEdit1.getText().toString().trim().equals(psdEdit2.getText().toString().trim())) {
                    ToastUtil.showToast(RegisterActivity.this, "前后两次密码不相同！", Toast.LENGTH_SHORT);
                    psdEdit1.setText("");
                    psdEdit2.setText("");
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            int registerdata = 2;

                            try {
                                OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
                                Request request = new Request.Builder()
                                        .url("http://192.168.137.1:8080/Smart_Building/user/registerinphone?username=" + usernameEdit1.getText() +
                                                "&password=" + psdEdit1.getText())
                                        .build();//创建Request 对象
                                Response response = null;
                                response = client.newCall(request).execute();//得到Response 对象
                                if (response.isSuccessful()) {
                                    //解析json
                                    JSONObject jsonObject = new JSONObject(response.body().string());
                                    registerdata = jsonObject.getInt("data");
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Message msg = Message.obtain();
                            msg.what = registerdata;
                            registerhander.sendMessage(msg);

                            Log.d("haha", "register: " + registerdata);
                        }
                    }).start();
                }
            }
        });

        registerhander = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        ToastUtil.showToast(RegisterActivity.this, "注册失败！", Toast.LENGTH_SHORT);
                        break;
                    case 1:
                        ToastUtil.showToast(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT);
                        finish();
                        break;
                    case 2:
                        ToastUtil.showToast(RegisterActivity.this, "网络未连接,请连接网络后再试！", Toast.LENGTH_SHORT);
                        finish();
                        break;
                    default:
                        break;
                }
            }
        };
        registercancel_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
