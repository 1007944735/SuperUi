package com.sgevf.ui.utils;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;

import com.sgevf.ui.BuildConfig;

import java.io.File;

//多媒体工具，包括系统相机，系统相册，裁剪
public class MediaUtil {
    /**
     * 打开系统相机
     *
     * @param context
     * @param requestCode
     */
    public static void openSysCamera(Activity context, File file, int requestCode) {
        int version = Build.VERSION.SDK_INT;
        if (version >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.CAMERA}, requestCode);
            } else {
                openCamera(context,file);
            }

        } else {
            openCamera(context,file);
        }
    }

    //打开相机
    private static Uri openCamera(Context context, File file) {
        if (file == null) return null;
        Uri uri = null;
        int version = Build.VERSION.SDK_INT;
        if (version >= Build.VERSION_CODES.N) {
            File folder = file.getParentFile();
            if (folder != null && !folder.exists()) {
                folder.mkdirs();
            }
            uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileProvider", file);
        } else {
            uri = Uri.fromFile(file);

        }
        return uri;
    }
}
