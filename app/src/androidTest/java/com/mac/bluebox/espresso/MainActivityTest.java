package com.mac.bluebox.espresso;

import android.app.Application;
import android.content.Context;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.matcher.ViewMatchers;
import android.test.ActivityInstrumentationTestCase2;

import com.google.inject.Module;
import com.mac.bluebox.MainActivity;
import com.mac.bluebox.R;
import com.mac.bluebox.roboguice.AppModule;

import java.util.regex.Matcher;

import roboguice.RoboGuice;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.typeText;
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
        onView(withId(R.id.textViewHello)).check(matches(isDisplayed()));
    }

    public void testChangeFriendEditText() {
        getActivity();
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.editTextFriend)).perform(clearText(), typeText("Anyer 1234567890!!"));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.editTextFriend)).perform(clearText(), typeText("Arturo abcdefghijk!!"));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.editTextFriend)).perform(clearText(), typeText("Hector !!"));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.editTextFriend)).check(matches(withText("Hector !!")));
    }
}
