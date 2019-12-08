package com.hieulam.spaceshooter.view;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.hieulam.spaceshooter.MainActivity;
import com.hieulam.spaceshooter.R;

public class Pause {

    int width, height;
    boolean isPaused = false;
    float x, y;
    Bitmap pause, resume;

    Pause(Resources res, int screenX, int screenY) {
        pause = BitmapFactory.decodeResource(res, R.drawable.pause_normal);
        resume = BitmapFactory.decodeResource(res, R.drawable.play_normal);

        width = pause.getWidth();
        height = pause.getHeight();

        width = (int) (width * MainActivity.density * 3f);
        height = (int) (height * MainActivity.density * 3f);

        pause = Bitmap.createScaledBitmap(pause, width, height, false);
        resume = Bitmap.createScaledBitmap(resume, width, height, false);

        x = 0;
        y = screenY - height;
    }

    Bitmap getPauseButton() {
        if(isPaused) return resume;
        else return pause;
    }


    Rect getCollisionShape() {
        return new Rect((int) x, (int) y, (int) x + width, (int) y + height);
    }
}
