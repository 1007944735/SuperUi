package com.sgevf.ui.utils;

import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;

public class SafelyHandlerWarpper extends Handler {
    private Handler impl;

    public SafelyHandlerWarpper(Handler impl){
        this.impl=impl;
    }

    @Override
    public void dispatchMessage(Message msg) {
        try {
            super.dispatchMessage(msg);
        }catch (WindowManager.BadTokenException e){

        }
    }

    @Override
    public void handleMessage(Message msg) {
        impl.handleMessage(msg);
    }
}
