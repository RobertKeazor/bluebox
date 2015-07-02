package com.mac.bluebox.espresso;

import android.app.Application;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.mac.bluebox.activity.MainActivity;
import com.mac.bluebox.roboguice.AppModule;

import roboguice.RoboGuice;

import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


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

//    public void testDeviceDiscoveredMustBeListed() {
//        final DeviceModel deviceModel = new DeviceModel("AAAA", "0000");
//        final MainActivity activity = (MainActivity) getActivity();
//        activity.runOnUiThread(
//                new Runnable() {
//                    @Override
//                    public void run() {
//                        activity.getRecyclerViewAdapter().getDevices().add(deviceModel);
//                        activity.getRecyclerViewAdapter().notifyDataSetChanged();
//                    }
//                }
//        );
//
//        onView(withText("AAAA")).check(matches(isDisplayed()));
//    }
}
