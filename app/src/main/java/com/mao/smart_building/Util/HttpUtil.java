package com.mao.smart_building.Util;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Mingpeidev on 2019/4/23.
 */

public class HttpUtil {
    /**
     * 异步Get方法
     *
     * @param address
     * @param callback
     */
    public static void sendRequestWithOkhttpAsynGet(String address, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).get().build();
        client.newCall(request).enqueue(callback);//异步
    }

    /**
     * 异步Post方法
     *
     * @param address
     * @param body
     * @param callback
     */
    public static void sendRequestWithOkhttpAsynPost(String address, RequestBody body, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).post(body).build();
        client.newCall(request).enqueue(callback);//异步
    }

    /**
     * 同步Get方法
     *
     * @param address
     * @return
     */
    public static String sendRequestWithOkhttpSynGet(String address) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).get().build();

        String message = null;
        try {
            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                message = response.body().string();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }

    /**
     * 同步Post方法
     *
     * @param address
     * @param body
     * @return
     */
    public static String sendRequestWithOkhttpSynPost(String address, RequestBody body) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).post(body).build();

        String message = null;
        try {
            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                message = response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }
}
