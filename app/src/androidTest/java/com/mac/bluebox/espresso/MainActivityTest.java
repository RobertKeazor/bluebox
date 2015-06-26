package com.mac.bluebox.espresso;

import android.app.Application;
import android.content.Context;
import android.support.test.espresso.matcher.ViewMatchers;
import android.test.ActivityInstrumentationTestCase2;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.mac.bluebox.MainActivity;
import com.mac.bluebox.R;
import com.mac.bluebox.roboguice.AppModule;

import roboguice.RoboGuice;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
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

        Context applicationContext = getInstrumentation().getTargetContext().getApplicationContext();
        RoboGuice.overrideApplicationInjector((Application) applicationContext, getModules());
    }

    private Module getModules() {
        return new AppModule();
    }

    public void testMainScreenHasTextView(){
        getActivity();
        onView(ViewMatchers.withId(R.id.textViewHello)).check(matches(isDisplayed()));
    }
}
