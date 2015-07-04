package com.mac.bluebox.helper;

import android.util.Log;

import com.mac.bluebox.bluetooth.ConnectedThread;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by anyer on 7/1/15.
 */
public class StreamAudioThread extends  Thread{
    static final String LOG_TAG = StreamAudioThread.class.getName();

    public static final int SAMPLE_RATE = 8000;
    public static final int SAMPLE_INTERVAL = 20; // milliseconds
    public static final int BUF_SIZE = 800;
    private File audio;
    private ConnectedThread connectedThread;

    public void stream(File audio, ConnectedThread connectedThread) {
        setName(StreamAudioThread.class.getName());
        this.audio = audio;
        this.connectedThread = connectedThread;
    }

    @Override
    public void run() {
        while(!isInterrupted()) {
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

                    byte[] data = ArrayHelper.encodePacket(ConnectedThread.SERVER_SEND_STREAM_TRACK,
                            buf, bytes_read);

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
}
