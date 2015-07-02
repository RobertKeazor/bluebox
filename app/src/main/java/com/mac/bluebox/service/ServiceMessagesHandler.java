package com.mac.bluebox.service;

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
import com.mac.bluebox.helper.ArrayHelper;
import com.mac.bluebox.bluetooth.ConnectThread;
import com.mac.bluebox.bluetooth.ConnectedThread;
import com.mac.bluebox.bluetooth.ServerThread;
import com.mac.bluebox.helper.PlayAudioHelper;
import com.mac.bluebox.helper.StreamAudioHelper;
import com.mac.bluebox.receivers.DevicesBroadcastReceiver;
import com.mac.bluebox.receivers.TracksBroadcastReceiver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anyer on 6/28/15.
 */
public class ServiceMessagesHandler extends Handler {
    @Inject
    BluetoothAdapter mBluetoothAdapter;

    @Inject
    Context mContext;

    private static final String TAG = ServiceMessagesHandler.class.getName();
    private ServerThread mServerThread = null;
    private ConnectThread mConnectingThread = null;
    private ConnectedThread mConnectedThread = null;
    private ConnectedThread mClientConnectedThread = null;
    PlayAudioHelper playAudio = new PlayAudioHelper();
    StreamAudioHelper streamAudio;

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);

        switch (msg.what) {
            case BboxBluetoothService.SERVER_TURN_ON_BLUETOOTH:
                stopServer();

                mServerThread = new ServerThread(mBluetoothAdapter, ServiceMessagesHandler.this);
                mServerThread.start();

                Log.e(TAG, "SERVER_TURN_ON_BLUETOOTH ....");
                break;

            case BboxBluetoothService.SERVER_TURN_OFF_BLUETOOTH:
                stopServer();

                Log.e(TAG, "SERVER_TURN_OFF_BLUETOOTH ....");
                break;

            case BboxBluetoothService.SERVER_HAS_A_NEW_CLIENT_CONNECTED:
                List<String> musicFiles = readMusicFiles();
                String join = ArrayHelper.joinStringByComma(musicFiles);

                byte[] joinBytes = join.getBytes();
                byte[] tmpBytes = new byte[joinBytes.length + 1];

                tmpBytes[0] = ConnectedThread.SERVER_SEND_LIST_OF_TRACKS;
                for (int i = 1; i < tmpBytes.length; i++) {
                    tmpBytes[i] = joinBytes[i - 1];
                }

                stopClientConnectedThread();
                mClientConnectedThread = (ConnectedThread) msg.obj;
                mClientConnectedThread.write(tmpBytes);

                Intent intentServing = new Intent();
                intentServing.setAction(DevicesBroadcastReceiver.SERVING);
                intentServing.putExtra(DevicesBroadcastReceiver.DEVICE_NAME,
                        ((ConnectedThread) msg.obj).getDeviceName());

                mContext.sendBroadcast(intentServing);

                streamAudio = new StreamAudioHelper();

                Log.e(TAG, "SERVER_HAS_A_NEW_CLIENT_CONNECTED ...");
                break;


            case BboxBluetoothService.CLIENT_SOCKET_CONNECTED:
                stopConnectedThread();

                mConnectedThread = (ConnectedThread) msg.obj;
                mConnectedThread.start();

                Log.e(TAG, "CLIENT_SOCKET_CONNECTED ...");
                break;

            case BboxBluetoothService.CLIENT_RECEIVE_LIST_OF_TRACKS:
                byte[] bytes = (byte[]) msg.obj;

                Intent intentTracksListDiscovered = new Intent();
                intentTracksListDiscovered.setAction(
                        TracksBroadcastReceiver.TRACKS_LIST_DISCOVERED);
                intentTracksListDiscovered.putExtra(TracksBroadcastReceiver.EXTRA_TRACKS,
                        new String(bytes));
                mContext.sendBroadcast(intentTracksListDiscovered);

                Log.e(TAG, "CLIENT_RECEIVE_LIST_OF_TRACKS:");
                break;


            case BboxBluetoothService.CLIENT_CONNECT_TO_SERVER:
                stopConnectedThread();
                stopConnectingThread();

                mConnectingThread = new ConnectThread((BluetoothDevice) msg.obj, mBluetoothAdapter,
                        ServiceMessagesHandler.this);

                mConnectingThread.start();

                Log.e(TAG, "CLIENT_CONNECT_TO_SERVER ...");
                break;


            case BboxBluetoothService.CLIENT_DISCONNECT_FROM_SERVER:
                stopConnectingThread();
                stopConnectedThread();

                Log.e(TAG, "CLIENT_DISCONNECT_FROM_SERVER ....");
                break;

            case BboxBluetoothService.SERVER_DETECTED_A_DISCONNECTION:
                stopClientConnectedThread();
                mContext.sendBroadcast(new Intent(DevicesBroadcastReceiver.STOP_SERVING));

                Log.e(TAG, "SERVER_DETECTED_A_DISCONNECTION ...." + mContext);
                break;

            case BboxBluetoothService.CLIENT_RECEIVE_STREAM_TRACK:
                if (msg.obj != null) {
                    byte[] stream = (byte[]) msg.obj;

                    if (stream != null) {
                        playAudio.play(stream, msg.arg1);
                    }
                }

                Log.e(TAG, "CLIENT_RECEIVE_STREAM_TRACK ...");
                break;

            case BboxBluetoothService.SERVER_RECEIVE_PLAY_TRACK:
                String trackID = new String((byte[]) msg.obj);

                streamAudio.stream(getTrack(), mClientConnectedThread);
                Log.e(TAG, "SERVER_RECEIVE_PLAY_TRACK ...");
                break;

            case BboxBluetoothService.CLIENT_SEND_PLAY_TRACK:
                mConnectedThread.write(new byte[]{ConnectedThread.CLIENT_SEND_PLAY_TRACK, 1});

                Log.e(TAG, "CLIENT_SEND_PLAY_TRACK ...");
                break;

            default:
                super.handleMessage(msg);
        }
    }

    private void stopClientConnectedThread() {
        if (mClientConnectedThread != null) {
            mClientConnectedThread.cancel();
        }

        mClientConnectedThread = null;
    }

    private void stopConnectingThread() {
        if (mConnectingThread != null) {
            mConnectingThread.cancel();
        }

        mConnectingThread = null;
    }

    private void stopServer() {
        if (mServerThread != null) {
            mServerThread.cancel();
        }

        mServerThread = null;
    }

    private void stopConnectedThread() {
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
        }

        mConnectedThread = null;
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

    private File getTrack() {
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


        File file = null;

        while (audioFiles.moveToNext()) {
            String trackName = audioFiles.getString(audioFiles.getColumnIndex(
                    MediaStore.Files.FileColumns.DISPLAY_NAME));


            if (trackName.toLowerCase().equals("1.wav")) {
                String fileName = audioFiles.getString(audioFiles.getColumnIndex(
                        MediaStore.Audio.Media.DATA));
                file = new File(fileName);

                return file;
            }
        }

        return file;
    }
}
