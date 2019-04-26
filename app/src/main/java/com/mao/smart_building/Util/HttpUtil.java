package com.mao.smart_building.Util;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

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
    public static void sendRequestWithOkhttpGet(String address, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);//异步
    }

    /**
     * 异步Post方法
     *
     * @param address
     * @param body
     * @param callback
     */
    public static void sendRequestWithOkhttpPost(String address, RequestBody body, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).post(body).build();
        client.newCall(request).enqueue(callback);//异步
    }
}
