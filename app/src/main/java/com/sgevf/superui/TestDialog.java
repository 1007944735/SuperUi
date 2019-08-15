package com.sgevf.superui;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.sgevf.ui.BoxDialog;

public class TestDialog extends BoxDialog implements View.OnClickListener {
    private TextView address;
    public TestDialog(Context context) {
        super(context);
    }

    @Override
    protected void initView() {
        address=findViewById(R.id.address);
    }

    @Override
    protected int layoutResId() {
        return R.layout.dialog_test;
    }

    @Override
    protected void initEvent() {
        address.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        address.setText("address"+Math.random()*1000);
    }

}
