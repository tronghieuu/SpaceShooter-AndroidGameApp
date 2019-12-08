package com.hieulam.spaceshooter;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import com.hieulam.spaceshooter.R;

public class SoundManager extends Activity
{
    static SoundPool soundPool;
    static int[] sm;
    static int[] streamIDs;
    static MediaPlayer mediaPlayer;
   public void InitSound(Context mContext) {

        int maxStreams = 4;
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(maxStreams)
                    .build();

        streamIDs = new int[6];
        sm = new int[6];
        // fill your sounds
        sm[0] = soundPool.load(mContext, R.raw.boss_explosion, 2);
        sm[1] = soundPool.load(mContext, R.raw.rock_shot_destroy, 1);
        sm[2] = soundPool.load(mContext, R.raw.ship_explosion, 1);
    }

    public static void playSound(int sound) {
        soundPool.play(sm[sound], 1, 1, 1, 0, 1f);
    }

    public static void playMusic(Context context, int music) {
       if (mediaPlayer!=null)
            mediaPlayer.release();
       if (music==1)
            mediaPlayer = MediaPlayer.create(context, R.raw.music_main_menu);
       else if (music==2)
            mediaPlayer = MediaPlayer.create(context, R.raw.music_in_game);
       else if (music==3)
            mediaPlayer = MediaPlayer.create(context, R.raw.music_gameover);
        mediaPlayer.start();
        mediaPlayer.setVolume(0.4f,0.4f);
        mediaPlayer.setLooping(true);
    }

    public static void pauseMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public static void resumeMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }
}