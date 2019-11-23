package com.hieulam.spaceshooter.view;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.hieulam.spaceshooter.R;

public class SoundManager extends Activity
{
    static SoundPool soundPool;
    static int[] sm;
   public void InitSound(Context mContext) {

        int maxStreams = 4;
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(maxStreams)
                    .build();


        sm = new int[3];
        // fill your sounds
        sm[0] = soundPool.load(mContext, R.raw.rock_shot_divide, 1);
        sm[1] = soundPool.load(mContext, R.raw.rock_shot_destroy, 1);
        sm[2] = soundPool.load(mContext, R.raw.ship_explosion, 1);

    }

    static void playSound(int sound) {

        soundPool.play(sm[sound], 1, 1, 1, 0, 1f);
    }

    public final void cleanUpIfEnd() {
        sm = null;
        soundPool.release();
        soundPool = null;
    }
}