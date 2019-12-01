package com.hieulam.spaceshooter.view;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.hieulam.spaceshooter.R;

import static com.hieulam.spaceshooter.GamePlayActivity.point;
import static com.hieulam.spaceshooter.view.GameView.screenRatioY;

public class Bullet {

    int width, height;
    float x, y, radius;
    Bitmap bullet;

    Bullet(Resources res) {
        bullet = BitmapFactory.decodeResource(res, R.drawable.bullet);

        width = bullet.getWidth();
        height = bullet.getHeight();
        width = (int) (width * screenRatioY * 2f);
        height = (int) (height * screenRatioY * 2f);

        bullet = Bitmap.createScaledBitmap(bullet, width, height, false);

        x = - 500;
        y = - 500;

        radius = width/2;
    }

    Bitmap getBullet() {
        return bullet;
    }

    Rect getCollisionShape() {
        return new Rect((int) x, (int) y, (int) x + width, (int) y + height);
    }

    boolean isVisible() {
        if(x > - width && x < point.x && y > - height && y < point.y) return true;
        return false;
    }
}
