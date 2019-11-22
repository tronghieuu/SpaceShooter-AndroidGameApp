package com.hieulam.spaceshooter.view;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.hieulam.spaceshooter.R;

import java.util.Random;

import static com.hieulam.spaceshooter.GamePlayActivity.point;
import static com.hieulam.spaceshooter.view.GameView.screenRatioX;

public class Rock {

    int width, height, rockFrame = 0;
    float x, y, min = 0.5f, max = 1;
    Bitmap rock1, rock2, rock3, rock4, rock5, rock6, rock7, rock8;

    Rock(Resources res) {
        rock1 = BitmapFactory.decodeResource(res, R.drawable.rock1);
        rock2 = BitmapFactory.decodeResource(res, R.drawable.rock2);
        rock3 = BitmapFactory.decodeResource(res, R.drawable.rock3);
        rock4 = BitmapFactory.decodeResource(res, R.drawable.rock4);
        rock5 = BitmapFactory.decodeResource(res, R.drawable.rock5);
        rock6 = BitmapFactory.decodeResource(res, R.drawable.rock6);
        rock7 = BitmapFactory.decodeResource(res, R.drawable.rock7);
        rock8 = BitmapFactory.decodeResource(res, R.drawable.rock8);

        width = rock1.getWidth();
        height = rock1.getHeight();

        Random r = new Random();
        float random = min + r.nextFloat() * (max - min);
        width = (int) (width * screenRatioX * random);
        height = (int) (height * screenRatioX * random);

        rock1 = Bitmap.createScaledBitmap(rock1, width, height, false);
        rock2 = Bitmap.createScaledBitmap(rock2, width, height, false);
        rock3 = Bitmap.createScaledBitmap(rock3, width, height, false);
        rock4 = Bitmap.createScaledBitmap(rock4, width, height, false);
        rock5 = Bitmap.createScaledBitmap(rock5, width, height, false);
        rock6 = Bitmap.createScaledBitmap(rock6, width, height, false);
        rock7 = Bitmap.createScaledBitmap(rock7, width, height, false);
        rock8 = Bitmap.createScaledBitmap(rock8, width, height, false);

        x = - 100;
        y = - 100;
    }

    Bitmap getRock() {
        switch (rockFrame) {
            case 0:
                rockFrame++;
                return rock1;
            case 1:
                rockFrame++;
                return rock2;
            case 2:
                rockFrame++;
                return rock3;
            case 3:
                rockFrame++;
                return rock4;
            case 4:
                rockFrame++;
                return rock5;
            case 5:
                rockFrame++;
                return rock6;
            case 6:
                rockFrame++;
                return rock7;
            case 7:
                rockFrame = 0;
                return rock8;
        }
        return rock1;
    }

    Rect getCollisionShape() {
        return new Rect((int) x, (int) y, (int) x + width, (int) y + height);
    }

    boolean isVisible() {
        if(x > - width && x < point.x && y > - height && y < point.y) return true;
        return false;
    }
}
