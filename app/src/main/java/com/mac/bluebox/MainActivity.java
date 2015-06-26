package com.mac.bluebox;

import android.widget.TextView;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_main)
public class MainActivity extends RoboActivity {
    @InjectView(R.id.textViewHello)
    TextView textViewHello;

    @Override
    protected void onResume() {
        super.onResume();

        textViewHello.setText("Bluebox Rocks !!!!");
    }
}
