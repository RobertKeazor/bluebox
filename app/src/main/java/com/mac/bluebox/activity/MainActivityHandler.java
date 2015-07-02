package com.mac.bluebox.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.TextView;

/**
 * Created by anyer on 6/30/15.
 */
public class MainActivityHandler extends Handler {
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView textViewMessage;
    private MainActivity activity;


//    private SwipeRefreshLayoutWrapper swipeRefreshLayoutWrapper;
//    private TextView textViewMessage;

//    public MainActivityHandler(SwipeRefreshLayoutWrapper swipeRefreshLayoutWrapper,
//                               TextView textViewMessage) {
//        this.swipeRefreshLayoutWrapper = swipeRefreshLayoutWrapper;
//        this.textViewMessage = textViewMessage;
//    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);

        switch (msg.what) {
            case MainActivity.UI_CHANGE_TO_SERVING:
                swipeRefreshLayout.setVisibility(View.GONE);

                textViewMessage.setVisibility(View.VISIBLE);
                textViewMessage.setText("SERVING to " + (String)msg.obj);

                break;

            case MainActivity.UI_CHANGE_TO_LIST_DEVICES:
                textViewMessage.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.VISIBLE);

                activity.refreshPairedDevices();
                break;
        }
    }

    public void setup(MainActivity activity, SwipeRefreshLayout swipeRefreshLayout,
                      TextView textViewMessage) {
        this.activity = activity;
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.textViewMessage = textViewMessage;
    }
}
