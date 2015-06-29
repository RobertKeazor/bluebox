package com.mac.bluebox.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by anyer on 6/27/15.
 */
public class ServerThread extends Thread {
    private final BluetoothServerSocket mmServerSocket;
    private final Handler mHandler;
    private List<ConnectedThread> connectedThreads;

    public ServerThread(BluetoothAdapter bluetoothAdapter, Handler handler) {
        this.mHandler = handler;
        connectedThreads = new ArrayList<ConnectedThread>();

        // Use a temporary object that is later assigned to mmServerSocket,
        // because mmServerSocket is final
        BluetoothServerSocket tmp = null;
        try {
            // MY_UUID is the app's UUID string, also used by the client code
            tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord("bboxserver",
                    new UUID(1000001, 100000001));
        } catch (IOException e) { }
        mmServerSocket = tmp;
    }

    public void run() {
        BluetoothSocket socket = null;
        // Keep listening until exception occurs or a socket is returned
        while (!isInterrupted()) {
            try {
                if(mmServerSocket != null) {
                    socket = mmServerSocket.accept();
                }
            } catch (IOException e) {
                break;
            }
            // If a connection was accepted
            if (socket != null) {
                // Do work to manage the connection (in a separate thread)
                manageConnectedSocket(socket);
//                try {
//                    mmServerSocket.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                break;
            }
        }

        for (ConnectedThread connectedThread: connectedThreads) {
            connectedThread.interrupt();
        }
    }

    private void manageConnectedSocket(BluetoothSocket socket) {
        ConnectedThread connectedThread = new ConnectedThread(socket, mHandler);
        connectedThread.start();

        connectedThreads.add(connectedThread);

        mHandler.obtainMessage(BboxBluetoothService.SERVER_HAS_CLIENT_CONNECTED, connectedThread)
                .sendToTarget();
    }
}