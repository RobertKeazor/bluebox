package com.mac.bluebox.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;

import com.google.inject.Inject;
import com.mac.bluebox.ArrayHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anyer on 6/28/15.
 */
public class IncommingMessagesHandler extends Handler {;
    @Inject
    BluetoothAdapter mBluetoothAdapter;

    @Inject
    Context mContext;

    private static final String TAG = IncommingMessagesHandler.class.getName();
    private ServerThread serverThread = null;
    private ConnectThread clientThread = null;

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);

        switch (msg.what) {
            case BboxBluetoothService.CLIENT_SERVER_PAIRED:
                ConnectedThread socket = (ConnectedThread) msg.obj;

                socket.start();
                break;

            case BboxBluetoothService.SOCKET_MESSAGE_READ:
                byte[] bytes = (byte[]) msg.obj;

                Intent intent = new Intent();
                intent.setAction(BboxTracksBroadcastReceiver.TRACKS_LIST_DISCOVERED);
                intent.putExtra(BboxTracksBroadcastReceiver.EXTRA_TRACKS, new String(bytes));
                mContext.sendBroadcast(intent);

                break;

            case BboxBluetoothService.SERVER_HAS_CLIENT_CONNECTED:
                List<String> musicFiles = readMusicFiles();
                String join = ArrayHelper.joinStringByComma(musicFiles);

                ((ConnectedThread) msg.obj).write(join.getBytes());

                break;

            case BboxBluetoothService.CONNECT_TO_SERVER:
                clientThread = new ConnectThread((BluetoothDevice) msg.obj, mBluetoothAdapter,
                        IncommingMessagesHandler.this);

                clientThread.start();

                Log.e(TAG, "Client trying to connect ...");
                break;

            case BboxBluetoothService.TURN_ON_BLUETOOTH_SERVER:
                serverThread = new ServerThread(mBluetoothAdapter, IncommingMessagesHandler.this);
                serverThread.start();

                Log.e(TAG, "Server Started ....");
                break;

            case BboxBluetoothService.TURN_OFF_BLUETOOTH_SERVER:
                serverThread.interrupt();

                Log.e(TAG, "Server interrupted ....");
                break;

            case BboxBluetoothService.DISCONNECT_FROM_SERVER:
                clientThread.interrupt();

                Log.e(TAG, "Client interrupted ....");
                break;

            default:
                super.handleMessage(msg);
        }
    }

    private List<String> readMusicFiles() {
        ContentResolver cr = mContext.getContentResolver();
        Uri uri = MediaStore.Files.getContentUri("external");

        // every column, although that is huge waste, you probably need
        // BaseColumns.DATA (the path) only.
        String[] projection = null;

        // exclude media files, they would be here also.
        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO;
        String[] selectionArgs = null; // there is no ? in selection so null here

        String sortOrder = null; // unordered
        Cursor audioFiles = cr.query(uri, projection, selection, selectionArgs, sortOrder);

        ArrayList<String> tracks = new ArrayList<String>();
        while (audioFiles.moveToNext()) {
            String trackName = audioFiles.getString(audioFiles.getColumnIndex(
                    MediaStore.Files.FileColumns.DISPLAY_NAME));

            tracks.add(trackName);
        }

        return tracks;
    }
}
