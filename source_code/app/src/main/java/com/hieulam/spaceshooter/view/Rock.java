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
import static com.hieulam.spaceshooter.view.GameView.screenRatioY;

public class Rock {

    int width, height, rockAngle = 0, hp;
    float x, y, min = 2f, max = 3f, radius, speedY;
    Bitmap rock;

    Rock(Resources res) {
        rock = BitmapFactory.decodeResource(res, R.drawable.rock);

        width = rock.getWidth();
        height = rock.getHeight();

        Random r = new Random();
        float random = min + r.nextFloat() * (max - min);
        width = (int) (width * MainActivity.density * random);
        height = (int) (height * MainActivity.density * random);

        rock = Bitmap.createScaledBitmap(rock, width, height, false);

        x = - 500;
        y = - 500;

        radius = width/2;
    }

    public void moveForward() {
        y += speedY;
    }

    public void setRock(float x, float y, float speedY, int hp) {
        this.x = x;
        this.y = y;
        this.speedY = speedY;
        this.hp=hp;
    }

    public boolean CircleCollisionDetect(float cRadius, float cX, float cY){
        float dx = (x+radius) - (cX+cRadius);
        float dy = (y+radius) - (cY+cRadius);
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (distance < radius + cRadius) return true;
        return false;
    }

    Bitmap getRock() {
        rockAngle++;
        if(rockAngle == 360){
            rockAngle = 0;
        }
        Bitmap rotateRock = Bitmap.createBitmap(width,
                height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(rotateRock);
        Matrix matrix = new Matrix();
        matrix.postRotate(rockAngle, width/2, height/2);
        canvas.drawBitmap(rock, matrix, new Paint());
        return rotateRock;
    }


    boolean isVisible() {
        if(x > - width && x < point.x && y > - height && y < point.y) return true;
        return false;
    }
}
