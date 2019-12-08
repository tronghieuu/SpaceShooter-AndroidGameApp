package com.hieulam.spaceshooter.view;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.hieulam.spaceshooter.MainActivity;
import com.hieulam.spaceshooter.R;

import static com.hieulam.spaceshooter.GamePlayActivity.point;
import static com.hieulam.spaceshooter.view.GameView.screenRatioY;

public class Bullet {

    int width, height;
    float x, y, a, speedX, speedY, radius;
    Bitmap bullet;

    Bullet(Resources res) {
        bullet = BitmapFactory.decodeResource(res, R.drawable.bullet);

        width = bullet.getWidth();
        height = bullet.getHeight();
        width = (int) (width * MainActivity.density * 2f);
        height = (int) (height * MainActivity.density * 2f);

        bullet = Bitmap.createScaledBitmap(bullet, width, height, false);

        x = - 500;
        y = - 500;

        radius = width/2;
    }

    Bitmap getBullet() {
        return bullet;
    }

    public void moveForward() {
        x += speedX;
        y += speedY;
    }

    public void setBullet(int angle, float bSpeed) {
        a = angle;
        speedX = (float) Math.cos(Math.toRadians(angle))*bSpeed;
        speedY = (float) Math.sin(Math.toRadians(angle))*bSpeed;
    }

    Rect getCollisionShape() {
        return new Rect((int) x, (int) y, (int) x + width, (int) y + height);
    }

    boolean isVisible() {
        if(x > - width && x < point.x && y > - height && y < point.y) return true;
        return false;
    }
}
