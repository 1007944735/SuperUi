package com.sgevf.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.sgevf.ui.R;

public abstract class BoxDialog extends Dialog {
    public Window window;
    public WindowManager.LayoutParams lp;
    public BoxDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(layoutResId());
        init();
        initView();
        initEvent();
    }

    protected abstract void initEvent();

    protected abstract void initView();

    private void init() {
        window=getWindow();
        lp=window.getAttributes();
        lp.gravity=Gravity.BOTTOM;
        lp.width=WindowManager.LayoutParams.MATCH_PARENT;
        lp.height=WindowManager.LayoutParams.WRAP_CONTENT;
        window.getDecorView().setPadding(0,0,0,0);
        getWindow().getDecorView().setBackgroundColor(Color.WHITE);
        dialogAnimations(R.style.DialogBottomAnimation);
    }

    public void dialogAnimations(int styleId) {
        window.setWindowAnimations(styleId);
    }

    protected abstract int layoutResId();
}
