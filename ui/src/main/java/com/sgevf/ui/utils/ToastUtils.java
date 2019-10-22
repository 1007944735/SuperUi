package com.sgevf.ui.utils;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import java.lang.reflect.Field;

public class ToastUtils {
    private static Field sField_TN;
    private static Field sField_TN_Handler;
    static{
        try {
            sField_TN= Toast.class.getDeclaredField("mTN");
            sField_TN.setAccessible(true);
            sField_TN_Handler=sField_TN.getType().getDeclaredField("mHandler");
            sField_TN_Handler.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    /**
     * 替换Toast中的Handler，修复Android7.X中的Toast bug
     * @param toast
     */
    private static void hook(Toast toast){
        try {
            Object tn=sField_TN.get(toast);
            Handler preHandler= (Handler) sField_TN_Handler.get(tn);
            sField_TN_Handler.set(tn,new SafelyHandlerWarpper(preHandler));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void showToast(Context context,CharSequence cs,int length){
        Toast toast=Toast.makeText(context,cs,length);
        hook(toast);
        toast.show();
    }
}
