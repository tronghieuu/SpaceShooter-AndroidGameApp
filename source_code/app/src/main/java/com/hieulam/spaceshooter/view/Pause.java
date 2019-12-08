package com.hieulam.spaceshooter.view;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import com.hieulam.spaceshooter.MainActivity;
import com.hieulam.spaceshooter.R;

import java.util.Random;

import static com.hieulam.spaceshooter.GamePlayActivity.point;

public class Pause {

    int width, height, state = 1;
    float x, y;
    Bitmap pause_normal, pause_pressed, resume_normal, resume_pressed;

    Pause(Resources res, int screenX, int screenY) {
        pause_normal = BitmapFactory.decodeResource(res, R.drawable.pause_normal);
        pause_pressed = BitmapFactory.decodeResource(res, R.drawable.pause_pressed);
        resume_normal = BitmapFactory.decodeResource(res, R.drawable.play_normal);
        resume_pressed = BitmapFactory.decodeResource(res, R.drawable.play_pressed);

        width = pause_normal.getWidth();
        height = pause_pressed.getHeight();

        width = (int) (width * MainActivity.density * 0.5f);
        height = (int) (height * MainActivity.density * 0.5f);

        pause_normal = Bitmap.createScaledBitmap(pause_normal, width, height, false);
        pause_pressed = Bitmap.createScaledBitmap(pause_pressed, width, height, false);
        resume_normal = Bitmap.createScaledBitmap(resume_normal, width, height, false);
        resume_pressed = Bitmap.createScaledBitmap(resume_pressed, width, height, false);

        x = screenX / 2;
        y = screenY / 2;
    }

    Bitmap getRock() {
        switch (state) {
            case 1:
                return pause_normal;
            case 2:
                return pause_pressed;
            case 3:
                return resume_normal;
            case 4:
                return resume_pressed;
        }
        return pause_normal;
    }


    Rect getCollisionShape() {
        return new Rect((int) x, (int) y, (int) x + width, (int) y + height);
    }
}
