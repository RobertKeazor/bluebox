package com.mac.bluebox.service;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

/**
 * Created by anyer on 6/28/15.
 */
public class DetailActivityServiceConnection implements ServiceConnection {
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
        sendMessage(BboxBluetoothService.CLIENT_CONNECT_TO_SERVER);
    }

    private void sendMessage(int what) {
        if (isServiceBounded()) {
            Messenger messenger = new Messenger(service);
            Message msg = Message.obtain(null, what);

            switch (what) {
                case BboxBluetoothService.CLIENT_CONNECT_TO_SERVER:
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
        sendMessage(BboxBluetoothService.CLIENT_DISCONNECT_FROM_SERVER);
    }

    private boolean isServiceBounded() {
        return mIsServiceBounded;
    }

    public void setDevice(Object device) {
        this.device = device;
    }

    public void playTrack(int index) {
        if (isServiceBounded()) {
            Messenger messenger = new Messenger(service);
            Message msg = Message.obtain(null, BboxBluetoothService.CLIENT_SEND_PLAY_TRACK);
            msg.obj = index;
            try {
                messenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
