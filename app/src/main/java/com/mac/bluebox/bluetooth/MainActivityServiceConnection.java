package com.mac.bluebox.bluetooth;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

/**
 * Created by anyer on 6/28/15.
 */
public class MainActivityServiceConnection implements ServiceConnection{
    private static final String TAG = MainActivityServiceConnection.class.getName();
    private IBinder service = null;
    boolean mIsServiceBounded = false;

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        this.service = service;
        mIsServiceBounded = true;

        Log.e(TAG, "MainActivity is bonded to service.");
        turnOnBluetoothServer();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mIsServiceBounded = false;
    }

    public void turnOffBluetoothServer() {
        sendMessage(BboxBluetoothService.TURN_OFF_BLUETOOTH_SERVER);
    }

    private void turnOnBluetoothServer() {
        sendMessage(BboxBluetoothService.TURN_ON_BLUETOOTH_SERVER);
    }

    private void sendMessage(int what) {
        if (isServiceBounded()) {
            Messenger messenger = new Messenger(service);
            Message msg =  Message.obtain(null, what);
            try {
                messenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isServiceBounded() {
        return mIsServiceBounded;
    }
}
