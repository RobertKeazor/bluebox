package com.mac.bluebox.helper;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.mac.bluebox.bluetooth.ConnectedThread;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by anyer on 7/1/15.
 */
public class StreamAudioHelper {
    static final String LOG_TAG = StreamAudioHelper.class.getName();

    public static final int SAMPLE_RATE = 8000;
    public static final int SAMPLE_INTERVAL = 20; // milliseconds
    public static final int SAMPLE_SIZE = 2; // bytes per sample
    public static final int BUF_SIZE = SAMPLE_INTERVAL * SAMPLE_INTERVAL * SAMPLE_SIZE * 2;

    public void stream(File audio, ConnectedThread connectedThread) {
        try {
            Log.e(LOG_TAG, audio.getCanonicalPath().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        long file_size = 0;
        int bytes_read = 0;
        int bytes_count = 0;

        FileInputStream audio_stream = null;
        file_size = audio.length();
        byte[] buf = new byte[BUF_SIZE];

        try {
            audio_stream = new FileInputStream(audio);

            while (bytes_count < file_size) {
                bytes_read = audio_stream.read(buf, 0, BUF_SIZE);

                byte[] data = new byte[bytes_read + 1];

                data[0] = ConnectedThread.SERVER_SEND_STREAM_TRACK;

                for (int i = 0; i < bytes_read; i++) {
                    data[i + 1] = buf[i];
                }

                connectedThread.write(data);
                bytes_count += bytes_read;

                Log.d(LOG_TAG, "bytes read: " + bytes_read);
                Thread.sleep(SAMPLE_INTERVAL, 0);
            }
        } catch (Throwable e) {
            Log.e(LOG_TAG, "InterruptedException");
        }
    }
}
