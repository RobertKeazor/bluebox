package com.mac.bluebox.helper;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

/**
 * Created by anyer on 7/1/15.
 */
public class PlayAudioThread extends Thread{
    static final String LOG_TAG = PlayAudioThread.class.getName();

    private final AudioTrack track;

    public PlayAudioThread() {
        track = new AudioTrack(AudioManager.STREAM_MUSIC,
                StreamAudioThread.SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, StreamAudioThread.BUF_SIZE,
                AudioTrack.MODE_STREAM);
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                Thread.sleep(StreamAudioThread.SAMPLE_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void play() {
        track.play();
    }

    public void write(byte[] bytes, int length) {
        track.write(bytes, 0, length);
    }
}
