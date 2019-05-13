package com.mao.smart_building.Receiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.mao.smart_building.Activity.AlarmInfoActivity;
import com.mao.smart_building.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Mingpeidev on 2019/5/12.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        int count = intent.getIntExtra("count", 0);

        String action = intent.getAction();

        System.out.println("广播" + action + count);

        /*if (count != 0) {
            String id = "channel_background";
            String name = "后台显示通知";

            Intent intent1 = new Intent(context, AlarmInfoActivity.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            Notification notification = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
                // 设置通知出现时的闪灯（如果 android 设备支持的话）
                //mChannel.enableLights(true);
                // 设置通知出现时的震动（如果 android 设备支持的话）
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300});
                // 自定义声音
                mChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM), null);
                //最后在notificationmanager中创建该通知渠道
                notificationManager.createNotificationChannel(mChannel);
                notification = new Notification.Builder(context)
                        .setChannelId(id)
                        .setContentTitle("智能楼宇安防报警信息")
                        .setContentText(count + "条")
                        .setWhen(System.currentTimeMillis())
                        .setOngoing(true)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.logo).build();
            } else {
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                        .setContentTitle("寸金")
                        .setContentText("寸金寸光阴!")
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.logo)
                        .setOngoing(true)
                        .setContentIntent(pendingIntent);
                notification = notificationBuilder.build();
            }
            notificationManager.notify(1, notification);
        } else {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);//关闭通知栏
            notificationManager.cancel(1);
        }
*/
    }
}
