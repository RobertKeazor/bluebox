package com.mac.bluebox.helper;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Created by anyer on 7/1/15.
 */
public class PlayAudioThread extends Thread{
    static final String LOG_TAG = PlayAudioThread.class.getName();
    public static final int PLAY = 1;
    public static final int WRITE = 2;

    private final AudioTrack track;
    public PlayAudioHandler handler;

    public PlayAudioThread() {
        track = new AudioTrack(AudioManager.STREAM_MUSIC,
                StreamAudioThread.SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, StreamAudioThread.BUF_SIZE,
                AudioTrack.MODE_STREAM);
    }

    @Override
    public void run() {
        Looper.prepare();

        handler = new PlayAudioHandler();

        Looper.loop();
    }

    public class PlayAudioHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PlayAudioThread.PLAY:
                    track.play();

                    break;

                case PlayAudioThread.WRITE:
                    byte[] bytes = (byte[]) msg.obj;
                    int length = msg.arg1;
                    track.write(bytes, 0, length);

                    break;
            }
        }
    }
}
