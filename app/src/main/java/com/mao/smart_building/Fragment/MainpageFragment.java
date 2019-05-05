package com.mao.smart_building.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mao.smart_building.R;
import com.mao.smart_building.Util.CharFormatUtil;
import com.mao.smart_building.Util.ToastUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Mingpeidev on 2019/4/1.
 */

public class MainpageFragment extends Fragment {

    private TextView temp_tv = null;
    private TextView humi_tv = null;
    private TextView light_tv = null;
    private ImageButton lamp_Btn = null;
    private ImageButton air_Btn = null;
    private ImageButton humi_Btn = null;


    private String temp = "";
    private String humi = "";
    private String light = "";
    private String control = "";
    private String human = "";
    private String smoke = "";

    private String lamp_control = "";
    private String air_control = "";
    private String alarm_control = "";
    private String door_control = "";


    // 主线程Handler,用于将从服务器获取的消息显示出来
    private Handler mMainHandler;

    // Socket变量
    private Socket socket = null;

    // 线程池,为了方便展示,此处直接采用线程池进行线程管理,而没有一个个开线程
    private ExecutorService mThreadPool;

    // 输入流对象
    InputStream is;

    // 输入流读取器对象
    InputStreamReader isr;
    BufferedReader br;

    // 接收服务器发送过来的消息
    String response;

    // 输出流对象
    OutputStream outputStream;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化线程池
        mThreadPool = Executors.newCachedThreadPool();

        CreateAndOpenSocket();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mainpage_layout, null);

        temp_tv = (TextView) view.findViewById(R.id.temp_tv);
        humi_tv = (TextView) view.findViewById(R.id.humi_tv);
        light_tv = (TextView) view.findViewById(R.id.light_tv);
        lamp_Btn = (ImageButton) view.findViewById(R.id.lamp_Btn);
        air_Btn = (ImageButton) view.findViewById(R.id.air_Btn);
        humi_Btn = (ImageButton) view.findViewById(R.id.addhumi_Btn);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        mMainHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        temp_tv.setText(temp + "℃");
                        break;
                    case 1:
                        humi_tv.setText(humi + "%RH");
                        break;
                    case 2:
                        light_tv.setText(light);
                        break;
                    case 3:
                        String x = CharFormatUtil.hexString2binaryString(control);

                        lamp_control = x.substring(4, 5);//00001111
                        air_control = x.substring(5, 6);
                        alarm_control = x.substring(6, 7);
                        door_control = x.substring(7, 8);

                        if (lamp_control.equals("1")) {
                            lamp_Btn.setBackgroundResource(R.drawable.lamp);
                        } else if (lamp_control.equals("0")) {
                            lamp_Btn.setBackgroundResource(R.drawable.lamp1);
                        }

                        if (air_control.equals("1")) {
                            air_Btn.setBackgroundResource(R.drawable.air);
                        } else if (air_control.equals("0")) {
                            air_Btn.setBackgroundResource(R.drawable.air1);
                        }

                        if (alarm_control.equals("1")) {
                            humi_Btn.setBackgroundResource(R.drawable.water);
                        } else if (alarm_control.equals("0")) {
                            humi_Btn.setBackgroundResource(R.drawable.water1);
                        }

                        break;
                    default:
                        break;
                }
            }
        };

        lamp_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lamp_control.equals("1")) {
                    ToastUtil.showToast(getActivity(), "关灯", Toast.LENGTH_LONG);
                    SendDataToService("0" + CharFormatUtil.binaryString2hexString("0" + air_control + alarm_control + door_control));
                }
                if (lamp_control.equals("0")) {
                    ToastUtil.showToast(getActivity(), "开灯", Toast.LENGTH_LONG);
                    SendDataToService("0" + CharFormatUtil.binaryString2hexString("1" + air_control + alarm_control + door_control));
                }
            }
        });

        air_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (air_control.equals("1")) {
                    ToastUtil.showToast(getActivity(), "关空调", Toast.LENGTH_LONG);
                    SendDataToService("0" + CharFormatUtil.binaryString2hexString(lamp_control + "0" + alarm_control + door_control));
                }
                if (air_control.equals("0")) {
                    ToastUtil.showToast(getActivity(), "开空调", Toast.LENGTH_LONG);
                    SendDataToService("0" + CharFormatUtil.binaryString2hexString(lamp_control + "1" + alarm_control + door_control));
                }
            }
        });

        humi_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (alarm_control.equals("1")) {
                    ToastUtil.showToast(getActivity(), "关加湿器", Toast.LENGTH_LONG);
                    SendDataToService("0" + CharFormatUtil.binaryString2hexString(lamp_control + air_control + "0" + door_control));
                }
                if (alarm_control.equals("0")) {
                    ToastUtil.showToast(getActivity(), "开加湿器", Toast.LENGTH_LONG);
                    SendDataToService("0" + CharFormatUtil.binaryString2hexString(lamp_control + air_control + "1" + door_control));
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (socket != null) {
            try {
                // 断开 客户端发送到服务器 的连接，即关闭输出流对象OutputStream
                outputStream.close();

                // 断开 服务器发送到客户端 的连接，即关闭输入流读取器对象BufferedReader
                br.close();

                // 最终关闭整个Socket连接
                socket.close();

                // 判断客户端和服务器是否已经断开连接
                System.out.println("关闭socket" + socket.isClosed());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.d("haha", "onDestroy: ");
    }

    private void CreateAndOpenSocket() {
        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {

                try {
                    //创建Socket对象&指定服务端的IP及端口号
                    socket = new Socket("192.168.137.1", 8989);
                    //判断客户端和服务器是否连接成功
                    System.out.println("连接socket" + socket.isClosed() + "@" + socket);

                    // 创建输入、输出流对象
                    is = socket.getInputStream();
                    outputStream = socket.getOutputStream();

                    // 创建输入流读取器对象 并传入输入流对象
                    // 该对象作用：获取服务器返回的数据
                    isr = new InputStreamReader(is);
                    br = new BufferedReader(isr);

                    // 步骤2：写入需要发送的数据到输出流对象中。特别注意：数据的结尾加上换行符才可让服务器端的readline()停止阻塞
                    outputStream.write(("111" + "\n").getBytes("utf-8"));

                    // 步骤3：发送数据到服务端
                    outputStream.flush();


                    while (true) {
                        try {

                            if (socket.isClosed() == true) {
                                System.out.println("socket已关闭，跳出读取循环");
                                break;
                            }

                            // 步骤3：通过输入流读取器对象 接收服务器发送过来的数据
                            response = br.readLine();

                            if (response == null) {
                                System.out.println("服务器发送数据为空，跳出读取循环");
                                break;
                            }

                            String s = new String("");
                            String t = new String("");

                            int x = response.split(" ").length;
                            String[] handler = response.split(" ");//02 07 18 00 f1 00 00 01 44 44 ff
                            if (x == 10) {
                                // s传感器短地址，t传感器获取数据
                                s = handler[5] + handler[6] + handler[7];
                                t = handler[8];
                                //System.out.println("网络地址：" + s + " 值：" + t);
                            } else if (x == 11) {
                                s = handler[5] + handler[6] + handler[7];//010201 2345
                                t = handler[9] + handler[8];
                                //System.out.println("网络地址：" + s + " 值：" + t);
                            }

                            // 步骤4:通知主线程,将接收的消息显示到界面
                            Message msg = Message.obtain();

                            if (s.equals("C43601")) {
                                temp = Integer.parseInt(t, 16) / 100.00 + "";
                                msg.what = 0;
                                System.out.println("temp:" + temp);
                            } else if (s.equals("C43602")) {
                                humi = Integer.parseInt(t, 16) / 100.00 + "";
                                msg.what = 1;
                                System.out.println("humi:" + humi);
                            } else if (s.equals("046301")) {
                                light = Integer.parseInt(t, 16) / 100.00 + "";
                                msg.what = 2;
                                System.out.println("light:" + light);
                            } else if (s.equals("AE7601")) {
                                control = t;
                                msg.what = 3;
                                System.out.println("control:" + control);
                            } else {
                                System.out.println("其他:" + s + t);
                            }

                            mMainHandler.sendMessage(msg);


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void SendDataToService(final String data) {
        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {

                try {
                    // 步骤1：从Socket 获得输出流对象OutputStream
                    // 该对象作用：发送数据
                    outputStream = socket.getOutputStream();

                    // 步骤2：写入需要发送的数据到输出流对象中
                    outputStream.write((data + "\n").getBytes("utf-8"));
                    // 特别注意：数据的结尾加上换行符才可让服务器端的readline()停止阻塞

                    // 步骤3：发送数据到服务端
                    outputStream.flush();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }


}
