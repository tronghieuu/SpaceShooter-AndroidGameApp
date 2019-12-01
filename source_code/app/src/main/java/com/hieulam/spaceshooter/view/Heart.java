package com.hieulam.spaceshooter.view;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.hieulam.spaceshooter.R;

import static com.hieulam.spaceshooter.view.GameView.screenRatioY;

public class Heart {

    int width, height;
    float x, y;
    Bitmap heart, heart_grey;
    boolean isLive = true;

    Heart(int screenX, int screenY, Resources res) {
        heart = BitmapFactory.decodeResource(res, R.drawable.heart);
        heart_grey = BitmapFactory.decodeResource(res, R.drawable.heart_grey);

        width = (int) (heart.getWidth() * screenRatioY * 3f);
        height = (int) (heart.getHeight() * screenRatioY * 3f);

        heart = Bitmap.createScaledBitmap(heart, width, height, false);
        heart_grey = Bitmap.createScaledBitmap(heart_grey, width, height, false);

        x = screenX - width;
        y = 0;
    }

    Bitmap getHeart() {
        if(isLive) return heart;
        return heart_grey;
    }
}
