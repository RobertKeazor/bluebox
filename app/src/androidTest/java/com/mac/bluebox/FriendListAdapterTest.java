package com.mac.bluebox;

import android.support.annotation.NonNull;
import android.test.AndroidTestCase;
import android.test.InstrumentationTestCase;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.mac.bluebox.bluetooth.BlueboxBluetoothAdapter;
import com.mac.bluebox.roboguice.AppTestModule;

import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Created by anyer on 6/26/15.
 */
public class FriendListAdapterTest extends InstrumentationTestCase {
    @Inject
    BlueboxBluetoothAdapter blueboxBluetoothAdapter;

    @Inject


    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        System.setProperty("dexmaker.dexcache",
                getInstrumentation().getTargetContext().getCacheDir().toString());

        Injector injector = Guice.createInjector(getModules());
        injector.injectMembers(this);
    }

    @NonNull
    private Module[] getModules() {
        return new Module[] {new AppTestModule(mock(BlueboxBluetoothAdapter.class))};
    }

    public void testGetFriendsIsNotEmpty() {
        blueboxBluetoothAdapter.getFriendsList();

        verify(blueboxBluetoothAdapter).findFriends();
    }
}
