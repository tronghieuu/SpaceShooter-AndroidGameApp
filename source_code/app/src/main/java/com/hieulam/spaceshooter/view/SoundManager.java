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


        sm = new int[6];
        // fill your sounds
        sm[0] = soundPool.load(mContext, R.raw.rock_shot_divide, 1);
        sm[1] = soundPool.load(mContext, R.raw.rock_shot_destroy, 1);
        sm[2] = soundPool.load(mContext, R.raw.ship_explosion, 1);
        sm[3] = soundPool.load(mContext, R.raw.music_main_menu, 1);
        sm[4] = soundPool.load(mContext, R.raw.music_in_game, 1);
        sm[5] = soundPool.load(mContext, R.raw.music_gameover, 1);
    }

    static void playSound(int sound) {
        soundPool.play(sm[sound], 1, 1, 1, 0, 1f);
    }
    
    static void playMusic(int sound) {

        soundPool.play(sm[sound], 1, 1, 1, 1, 1f);
    }

    public final void cleanUpIfEnd() {
        sm = null;
        soundPool.release();
        soundPool = null;
    }
}