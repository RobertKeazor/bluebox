package com.mac.bluebox.service;

import android.content.Intent;
import android.os.IBinder;
import android.os.Messenger;

import com.google.inject.Inject;

import roboguice.service.RoboService;

/**
 * Created by anyer on 6/26/15.
 */
public class BboxBluetoothService extends RoboService {
    /**
     * Handler to receive messages from UI.
     */
    @Inject
    ServiceMessagesHandler incommingMessagesHandler;

    private static final String TAG = BboxBluetoothService.class.getName();

    /**
     * Socket in client App is connected.
     */
    public static final int CLIENT_SOCKET_CONNECTED = 1;


    /**
     * The client App receives a list of tracks
     */
    public static final int CLIENT_RECEIVE_LIST_OF_TRACKS = 2;

    /**
     * Server start a bluetooth to allow clients.
     */
    public static final int SERVER_TURN_ON_BLUETOOTH = 3;

    /**
     * Server creates a connection with a new client.
     */
    public static final int SERVER_HAS_A_NEW_CLIENT_CONNECTED = 4;

    /**
     * Server stops bluetooth, so no more clients are allowed.
     */
    public static final int SERVER_TURN_OFF_BLUETOOTH = 5;

    /**
     * The client App requests to connect to server.
     */
    public static final int CLIENT_CONNECT_TO_SERVER = 6;

    /**
     * The client App requests to disconnect from server.
     */
    public static final int CLIENT_DISCONNECT_FROM_SERVER = 7;

    /**
     * Server detects a broken socket with client.
     */
    public static final int SERVER_DETECTED_A_DISCONNECTION = 8;

    /**
     * The client App receives a streaming.
     */
    public static final int CLIENT_RECEIVE_STREAM_TRACK = 9;

    /**
     * Server receives a request to play a track.
     */
    public static final int SERVER_RECEIVE_PLAY_TRACK = 10;

    /**
     * The client App requests to play a track.
     */
    public static final int CLIENT_SEND_PLAY_TRACK = 11;

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
