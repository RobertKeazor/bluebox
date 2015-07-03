package com.mac.bluebox.helper;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

/**
 * Created by anyer on 7/1/15.
 */
public class PlayAudioHelper {
    static final String LOG_TAG = PlayAudioHelper.class.getName();

    private final AudioTrack track;

    public PlayAudioHelper() {
        track = new AudioTrack(AudioManager.STREAM_MUSIC,
                StreamAudioHelper.SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, StreamAudioHelper.BUF_SIZE,
                AudioTrack.MODE_STREAM);

        track.play();
    }

    public void play(byte[] bytes, int length) {
        track.write(bytes, 0, length);
    }
}
