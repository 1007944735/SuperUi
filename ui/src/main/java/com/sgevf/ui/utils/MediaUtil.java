package com.sgevf.ui.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.sgevf.ui.BuildConfig;

import java.io.File;

//多媒体工具，包括系统相机，系统相册，裁剪
public class MediaUtil {
    /**
     * 打开系统相机
     * @param context
     * @param file
     * @param requestCode
     * @return
     */
    private static Uri openCamera(Context context, File file, int requestCode) {
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
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        ((Activity) context).startActivityForResult(intent,requestCode);
        return uri;
    }

    /**
     * 打开系统相册
     * @param context
     * @param requestCode
     */
    private static void openAlbum(Context context,int requestCode){
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
        ((Activity) context).startActivityForResult(intent,requestCode);
    }

}
