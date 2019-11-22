package com.hieulam.spaceshooter.view;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import com.hieulam.spaceshooter.R;

import java.util.Random;

import static com.hieulam.spaceshooter.GamePlayActivity.point;
import static com.hieulam.spaceshooter.view.GameView.screenRatioX;

public class Rock {

    int width, height, rockAngle = 0;
    float x, y, min = 0.7f, max = 1.3f;
    Bitmap rock;

    Rock(Resources res) {
        rock = BitmapFactory.decodeResource(res, R.drawable.rock);

        width = rock.getWidth();
        height = rock.getHeight();

        Random r = new Random();
        float random = min + r.nextFloat() * (max - min);
        width = (int) (width * screenRatioX * random);
        height = (int) (height * screenRatioX * random);

        rock = Bitmap.createScaledBitmap(rock, width, height, false);

        x = - 500;
        y = - 500;
    }

    Bitmap getRock() {
        rockAngle++;
        if(rockAngle == 180){
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

    Rect getCollisionShape() {
        return new Rect((int) x, (int) y, (int) x + width, (int) y + height);
    }

    boolean isVisible() {
        if(x > - width && x < point.x && y > - height && y < point.y) return true;
        return false;
    }
}
