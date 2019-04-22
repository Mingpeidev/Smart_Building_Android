package com.mao.smart_building.Util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mao.smart_building.R;

/**
 * Created by Mingpeidev on 2019/4/2.
 */

public class ToastUtil {
    private static Toast mToast = null;

    public static void showToastOnce(Context context, String text, int duration) {

        if (mToast == null) {
            mToast = Toast.makeText(context, "", duration);
            mToast.setText(text);
        } else {
            mToast.setText(text);
            mToast.setDuration(duration);
        }
        mToast.show();
    }

    public static void showToast1(Context context, String text, int duration) {

        mToast = Toast.makeText(context, "", duration);
        mToast.setText(text);
        mToast.show();
    }

    public static void showToast(Context context, String text, int duration) {

        LayoutInflater inflater = LayoutInflater.from(context);//设置界面
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.toast_layout, null);
        TextView toastv = (TextView) view.findViewById(R.id.toast_tv);
        toastv.setText(text);

        mToast = Toast.makeText(context, "", duration);//初始化toast

        mToast.setGravity(Gravity.CENTER, 0, 0);//设置位置

        mToast.setView(view);//设置自定义界面
        mToast.show();
    }
}