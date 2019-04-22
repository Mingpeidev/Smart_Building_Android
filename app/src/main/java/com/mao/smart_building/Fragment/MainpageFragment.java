package com.mao.smart_building.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mao.smart_building.R;

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

    /**
     * 主 变量
     */
    // 主线程Handler
    // 用于将从服务器获取的消息显示出来
    private Handler mMainHandler;

    // Socket变量
    private Socket socket;

    // 线程池
    // 为了方便展示,此处直接采用线程池进行线程管理,而没有一个个开线程
    private ExecutorService mThreadPool;

    /**
     * 接收服务器消息 变量
     */
    // 输入流对象
    InputStream is;

    // 输入流读取器对象
    InputStreamReader isr;
    BufferedReader br;

    // 接收服务器发送过来的消息
    String response;

    /**
     * 发送消息到服务器 变量
     */
    // 输出流对象
    OutputStream outputStream;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mainpage_layout, null);

        // 初始化线程池
        mThreadPool = Executors.newCachedThreadPool();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void Opensocket() {
        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {

                try {
                    // 创建Socket对象 & 指定服务端的IP 及 端口号
                    socket = new Socket("192.168.137.1", 8989);
                    // 判断客户端和服务器是否连接成功

                    System.out.println(socket.isConnected());

                    outputStream = socket.getOutputStream();

                    // 步骤2：写入需要发送的数据到输出流对象中
                    outputStream.write(("" + "\n").getBytes("utf-8"));
                    // 特别注意：数据的结尾加上换行符才可让服务器端的readline()停止阻塞

                    // 步骤3：发送数据到服务端
                    outputStream.flush();

                    // 步骤1：创建输入流对象InputStream
                    is = socket.getInputStream();

                    // 步骤2：创建输入流读取器对象 并传入输入流对象
                    // 该对象作用：获取服务器返回的数据
                    isr = new InputStreamReader(is);
                    br = new BufferedReader(isr);

                    while (true) {
                        try {

                            // 步骤3：通过输入流读取器对象 接收服务器发送过来的数据
                            response = br.readLine();

                            String s = new String("");
                            String t = new String("");

                            int x = response.split(" ").length;
                            String[] handler = response.split(" ");//02 07 18 00 f1 00 00 01 44 44 ff
                            if (x == 10) {
                                // s传感器短地址，t传感器获取数据
                                s = handler[6] + handler[5] + handler[7];
                                t = handler[8];
                                System.out.println("网络地址：" + s + " 值：" + t);
                            } else if (x == 11) {
                                s = handler[6] + handler[5] + handler[7];//010201 2345
                                t = handler[9] + handler[8];
                                // System.err.println(s);
                                // System.err.println(t);
                                System.out.println("网络地址：" + s + " 值：" + t);
                            }

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

}
