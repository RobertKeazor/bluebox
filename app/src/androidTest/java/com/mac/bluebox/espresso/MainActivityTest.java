package com.mac.bluebox.espresso;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.support.test.espresso.Espresso;
import android.test.ActivityInstrumentationTestCase2;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.mac.bluebox.MainActivity;
import com.mac.bluebox.bluetooth.BboxBroadcastReceiver;
import com.mac.bluebox.bluetooth.BluetoothArrayAdapter;
import com.mac.bluebox.roboguice.AppModule;

import roboguice.RoboGuice;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.action.ViewActions.click;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;


/**
 * Created by anyer on 6/26/15.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2 {
    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        Context context = getInstrumentation().getTargetContext().getApplicationContext();
        Application applicationContext = (Application) context;

        Injector injector = RoboGuice.overrideApplicationInjector(applicationContext,
                getModules(applicationContext));

        injector.injectMembers(this);
    }

    private Module getModules(Application context) {
        return new AppModule();
    }

    public void testDeviceDiscoveredMustBeListed() {
        final MainActivity activity = (MainActivity) getActivity();
        activity.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        activity.getBluetoothArrayAdapter().add("0000");
                    }
                }
        );

        onData(allOf(is(instanceOf(String.class)), is("0000")))
                .perform(click());
    }
}
