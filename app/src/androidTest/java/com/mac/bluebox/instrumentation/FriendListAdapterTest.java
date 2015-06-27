package com.mac.bluebox.instrumentation;

import android.support.annotation.NonNull;
import android.test.InstrumentationTestCase;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.mac.bluebox.bluetooth.BboxBluetoothService;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by anyer on 6/26/15.
 */
public class FriendListAdapterTest extends InstrumentationTestCase {
    @Inject
    BboxBluetoothService blueboxBluetoothAdapter;

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
        return new Module[]{};
    }

    public void testGetOnlineDevices() {
        //verify(blueboxBluetoothAdapter, times(2)).discovery();

        //blueboxBluetoothAdapter.getOnlineDevices();
    }
}
