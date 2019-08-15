package com.sgevf.ui.utils;

import android.app.Dialog;
import android.content.Context;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class DialogHelper {
    public static void showDialog(Context context,Class clazz) {
        try {
            Constructor<?> constructor= clazz.getConstructor(Context.class);
            Object o=constructor.newInstance(context);
            if(o instanceof Dialog){
                ((Dialog) o).show();
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
