package com.mac.bluebox.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.IBinder;
import android.os.Messenger;

import com.google.inject.Inject;

import roboguice.service.RoboService;

/**
 * Created by anyer on 6/26/15.
 */
public class BboxBluetoothService extends RoboService {
    @Inject
    BluetoothAdapter mBluetoothAdapter;

    @Inject
    IncommingMessagesHandler incommingMessagesHandler;

    private static final String TAG = BboxBluetoothService.class.getName();

    public static final int SOCKET_CONNECTED = 1;
    public static final int SOCKET_MESSAGE_READ = 2;
    public static final int TURN_ON_BLUETOOTH_SERVER = 3;
    public static final int NEW_CLIENT_CONNECTED = 4;
    public static final int TURN_OFF_BLUETOOTH_SERVER = 5;
    public static final int CONNECT_SOCKET = 6;
    public static final int DISCONNECT_SOCKET = 7;
    public static final int SOCKET_DISCONNECTED = 8;


    /**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    Messenger mMessenger;

    @Override
    public void onCreate() {
        super.onCreate();

        mMessenger = new Messenger(incommingMessagesHandler);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}
