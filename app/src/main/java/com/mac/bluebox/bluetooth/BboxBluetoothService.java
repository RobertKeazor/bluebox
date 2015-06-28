package com.mac.bluebox.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.provider.MediaStore;
import android.util.Log;

import com.google.inject.Inject;
import com.mac.bluebox.ArrayHelper;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import roboguice.service.RoboService;

/**
 * Created by anyer on 6/26/15.
 */
public class BboxBluetoothService extends RoboService {
    public static final int CLIENT_SERVER_PAIRED = 1;
    public static final int SOCKET_MESSAGE_READ = 2;
    public static final int CONNECT_AS_SERVER = 3;
    public static final int SERVER_CLIENT_PAIRED = 4;

    @Inject
    BluetoothAdapter bluetoothAdapter;

    public static final int REQUEST_TRACK = 1;
    private static final String TAG = BboxBluetoothService.class.getName();

    private BluetoothDevice bluetoothDevice;

    private Handler mHandler = new Handler() {
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
                    sendBroadcast(intent);

                    break;

                case BboxBluetoothService.SERVER_CLIENT_PAIRED:
                    ConnectedThread clientSocket = (ConnectedThread) msg.obj;
                    clientSocket.start();

                    List<String> musicFiles = readMusicFiles();
                    String join = ArrayHelper.joinStringByComma(musicFiles);
                    //String tracksList = "Track #1, Track #2, Track #3";
                    clientSocket.write(join.getBytes());
                    break;
            }
        }
    };

    private AcceptThread acceptThread;


    /**
     * Handler of incoming messages from clients.
     */
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REQUEST_TRACK:
                    BluetoothDevice device = (BluetoothDevice) msg.obj;
                    new ConnectThread(device, bluetoothAdapter, mHandler).start();
                    Log.e(TAG, "Trying to connect ...");
                    break;

                case CONNECT_AS_SERVER:
                    acceptThread = new AcceptThread(bluetoothAdapter, mHandler);
                    acceptThread.start();

                    Log.e(TAG, "Server Started ....");
                    break;


                default:
                    super.handleMessage(msg);
            }
        }
    }

    /**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    final Messenger mMessenger = new Messenger(new IncomingHandler());


    @Override
    public IBinder onBind(Intent intent) {
        BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

        if(bluetoothDevice != null) {
            new ConnectThread(bluetoothDevice, bluetoothAdapter, mHandler);
        }

        return mMessenger.getBinder();
    }

    private List<String> readMusicFiles() {
        ContentResolver cr = getApplicationContext().getContentResolver();
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
