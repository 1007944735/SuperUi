package com.sgevf.ui.utils;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息通知工具
 */
public class NotificationUtil {
    private static List<String> channelIds;
    private static NotificationManager manager;
    private static Application mContext;

    /**
     * 初始化,适配Android 8.0 消息推送
     *
     * @param channels
     */
    public static void init(Application context, List<NotificationChannelInfo> channels) {
        mContext = context;
        channelIds = new ArrayList<>();
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (channels == null) return;
            for (NotificationChannelInfo info : channels) {
                NotificationChannel channel = new NotificationChannel(info.id, info.name, info.importance);
                manager.createNotificationChannel(channel);
                channelIds.add(info.id);
            }
        }
    }

    /**
     * 显示消息
     *
     * @param notification 消息
     * @param id           notification id
     */
    public static void notify(int id, Notification notification) {
        if (isNotificationEnabled()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (TextUtils.isEmpty(notification.getChannelId())) {
                    throw new RuntimeException("above android 8.0,notification need a channelId");
                } else {
                    manager.notify(id, notification);
                }
            } else {
                manager.notify(id, notification);
            }
        } else {
            //打开通知设置
            openNotificationSet();
        }
    }

    /**
     * 创建notification
     *
     * @param id
     * @param channelId
     * @param autoCancel
     * @param ticker
     * @param title
     * @param text
     * @param icon
     * @param intent
     */
    public static void create(int id, String channelId, boolean autoCancel, String ticker, String title, String text, int icon, PendingIntent intent) {
        Notification notification = new NotificationCompat.Builder(mContext, channelId)
                .setAutoCancel(autoCancel)
                .setTicker(ticker)
                .setContentText(text)
                .setContentTitle(title)
                .setSmallIcon(icon)
                .setContentIntent(intent)
                .build();
        notify(id, notification);
    }

    /**
     * 打开系统通知设置
     */
    private static void openNotificationSet() {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //android 8.0以上
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("android.provider.extra.APP_PACKAGE", mContext.getPackageName());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //android 5.0-7.0
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("app_package", mContext.getPackageName());
            intent.putExtra("app_uid", mContext.getApplicationInfo().uid);
        } else {
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", mContext.getPackageName(), null));
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    /**
     * 判断应用是否开启权限
     *
     * @return
     */
    public static boolean isNotificationEnabled() {
        boolean isOpened = false;
        try {
            isOpened = NotificationManagerCompat.from(mContext).areNotificationsEnabled();
        } catch (Exception e) {
            e.printStackTrace();
            isOpened = false;
        }
        return isOpened;
    }

    public static class NotificationChannelInfo {
        String id;
        String name;
        int importance;

        public NotificationChannelInfo(String id, String name, int importance) {
            this.id = id;
            this.name = name;
            this.importance = importance;
        }
    }


}
