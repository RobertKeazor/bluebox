package com.mac.bluebox.instrumentation;

import android.support.annotation.NonNull;
import android.test.InstrumentationTestCase;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.mac.bluebox.bluetooth.BluetoothHelper;
import com.mac.bluebox.roboguice.AppTestModule;

import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by anyer on 6/26/15.
 */
public class FriendListAdapterTest extends InstrumentationTestCase {
    @Inject
    BluetoothHelper blueboxBluetoothAdapter;

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
        return new Module[] {new AppTestModule(mock(BluetoothHelper.class))};
    }

    public void testConnectToFriend() {
        when(blueboxBluetoothAdapter.getOnlineDevices())
                .thenReturn(Arrays.asList(new String[]{"0000", "1111"}));

        blueboxBluetoothAdapter.connect(blueboxBluetoothAdapter.getOnlineDevices().get(0));
    }
}
