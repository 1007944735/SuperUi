package com.sgevf.ui.banner;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;

public class ScrollingHandler extends Handler {
    private ViewPager viewPager;
    private int delayTime;
    private boolean state;
    private boolean isStop;

    public ScrollingHandler(ViewPager viewPager, int delayTime) {
        this.viewPager = viewPager;
        this.delayTime = delayTime;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        int action = msg.getData().getInt("action");
        if (action == ScrollingBanner.START_ROLL) {
            state = true;
            Message m = Message.obtain();
            Bundle bundle = new Bundle();
            bundle.putInt("action", ScrollingBanner.SCROLLING);
            m.setData(bundle);
            sendMessage(m);
        } else if (action == ScrollingBanner.STOP_ROLL) {
            state = false;
            isStop = true;
        } else if (state && action == ScrollingBanner.SCROLLING) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            Message m = Message.obtain();
            Bundle bundle = new Bundle();
            bundle.putInt("action", ScrollingBanner.SCROLLING);
            m.setData(bundle);
            if (isStop) {
                sendMessageDelayed(m, delayTime * 2);
                isStop = false;
            } else {
                sendMessageDelayed(m, delayTime);
            }
        }
    }
}
