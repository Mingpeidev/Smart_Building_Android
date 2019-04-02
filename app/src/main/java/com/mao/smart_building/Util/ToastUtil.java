package com.mao.smart_building.Util;

import android.content.Context;
import android.widget.Toast;

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

    public static void showToast(Context context, String text, int duration) {

        mToast = Toast.makeText(context, "", duration);
        mToast.setText(text);
        mToast.show();
    }
}
