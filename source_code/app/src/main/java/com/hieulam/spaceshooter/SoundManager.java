package com.hieulam.spaceshooter;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.hieulam.spaceshooter.R;

public class SoundManager extends Activity
{
    static SoundPool soundPool;
    static int[] sm;
    static int[] streamIDs;
   public void InitSound(Context mContext) {

        int maxStreams = 4;
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(maxStreams)
                    .build();

        streamIDs = new int[6];
        sm = new int[6];
        // fill your sounds
        sm[0] = soundPool.load(mContext, R.raw.rock_shot_divide, 1);
        sm[1] = soundPool.load(mContext, R.raw.rock_shot_destroy, 1);
        sm[2] = soundPool.load(mContext, R.raw.ship_explosion, 1);
        sm[3] = soundPool.load(mContext, R.raw.music_main_menu, 2);
        sm[4] = soundPool.load(mContext, R.raw.music_in_game, 2);
        sm[5] = soundPool.load(mContext, R.raw.music_gameover, 2);
    }

    public static void playSound(int sound) {
        soundPool.play(sm[sound], 1, 1, 1, 0, 1f);
    }

    public static void playMusic(int sound) {
        streamIDs[sound] = soundPool.play(sm[sound], (float)0.3, (float)0.3, 2, -1, 1f);
    }

    public static void stopMusic(int sound) {
            soundPool.stop(streamIDs[sound]);
    }

    public final void cleanUpIfEnd() {
        sm = null;
        soundPool.release();
        soundPool = null;
    }
}