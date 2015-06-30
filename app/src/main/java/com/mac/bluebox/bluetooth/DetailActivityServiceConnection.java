package com.mac.bluebox.bluetooth;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

/**
 * Created by anyer on 6/28/15.
 */
public class DetailActivityServiceConnection implements ServiceConnection{
    private IBinder service = null;
    boolean mIsServiceBounded = false;
    private Object device;

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        this.service = service;
        mIsServiceBounded = true;

        connectToServer();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mIsServiceBounded = false;
    }


    private void connectToServer() {
        sendMessage(BboxBluetoothService.CONNECT_SOCKET);
    }

    private void sendMessage(int what) {
        if (isServiceBounded()) {
            Messenger messenger = new Messenger(service);
            Message msg =  Message.obtain(null, what);

            switch (what) {
                case BboxBluetoothService.CONNECT_SOCKET:
                    msg.obj = device;
                    break;
            }

            try {
                messenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void disconnectFromServer() {
        sendMessage(BboxBluetoothService.DISCONNECT_SOCKET);
    }

    private boolean isServiceBounded() {
        return mIsServiceBounded;
    }

    public void setDevice(Object device) {
        this.device = device;
    }
}
