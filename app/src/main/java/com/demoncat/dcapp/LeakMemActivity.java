package com.demoncat.dcapp;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

/**
 * @Class: LeakMemActivity
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/22
 */
public class LeakMemActivity extends Activity {
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leak_mem);
        handler.post(new Runnable() {

            @Override
            public void run() {
                handler.postDelayed(this, 1000l);
            }
        });
    }

    /**
     * 如果不增加该代码，则会出现内存溢出。
     * 因为handler在轮训发送消息到队列中，并且它持有activity的this对象，
     * 在handler处理消息的时候，即使退出activity，也无法释放该activity。
     * 并且该activity引用了handler内部类对象，导致该内部类无法被GC。
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }
}
