package com.hieulam.spaceshooter.view;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.hieulam.spaceshooter.MainActivity;
import com.hieulam.spaceshooter.R;

import java.util.concurrent.ThreadLocalRandom;

import static com.hieulam.spaceshooter.GamePlayActivity.point;
import static com.hieulam.spaceshooter.view.GameView.screenRatioY;

public class Boss {

    int width, height, bossAngle = 0, hp = 0;
    float x, y, speedX, speedY, boxX, boxY, radius, bossShootingTime = 0;
    Bitmap boss;

    Boss(Resources res) {
        boss = BitmapFactory.decodeResource(res, R.drawable.boss);

        width = boss.getWidth();
        height = boss.getHeight();


        width = (int) (width * MainActivity.density * 0.6f);
        height = (int) (height * MainActivity.density * 0.6f);

        boss = Bitmap.createScaledBitmap(boss, width, height, false);

        x = - 500;
        y = - 500;

        radius = width/2;
    }

    public void spawnBoss(float screenX, float screenY, int boxSize, float speed, int hp){
        this.hp = hp;
        x = screenX / 2 - width / 2;
        y = height;

        int angle = ThreadLocalRandom.current().nextInt(1, 360 + 1);
        while (angle%90 < 20 || angle%90 > 70 ){
            angle = ThreadLocalRandom.current().nextInt(1, 360 + 1);
        }
        speedX = (float) Math.cos(Math.toRadians(angle))*speed;
        speedY = (float) Math.sin(Math.toRadians(angle))*speed;
        this.boxX = screenX;
        if (boxSize<5)
            this.boxY = screenY*(boxSize/5f);
        else this.boxY = screenY;

    }

    Bitmap getBoss() {
        bossAngle++;
        if(bossAngle == 360){
            bossAngle = 0;
        }
        Bitmap rotateBoss = Bitmap.createBitmap(width,
                height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(rotateBoss);
        Matrix matrix = new Matrix();
        matrix.postRotate(bossAngle, width/2, height/2);
        canvas.drawBitmap(boss, matrix, new Paint());
        return rotateBoss;
    }

    /**
     * Make one move, check for collision and react accordingly if collision occurs.
     *
     * boxX, boxY: the container (obstacle) for this boss.
     */
    public void moveOneStepWithCollisionDetection() {
        // Get the ball's bounds, offset by the radius of the ball
        float ballMinX = 0;
        float ballMinY = 0;
        float ballMaxX = boxX - width;
        float ballMaxY = boxY - height;

        // Calculate the ball's new position
        x += speedX;
        y += speedY;
        // Check if the ball moves over the bounds. If so, adjust the position and speed.
        if (x < ballMinX) {
            speedX = -speedX; // Reflect along normal
            x = ballMinX;     // Re-position the ball at the edge
        } else if (x > ballMaxX) {
            speedX = -speedX;
            x = ballMaxX;
        }
        // May cross both x and y bounds
        if (y < ballMinY) {
            speedY = -speedY;
            y = ballMinY;
        } else if (y > ballMaxY) {
            speedY = -speedY;
            y = ballMaxY;
        }
    }

    public boolean CircleCollisionDetect(float cRadius, float cX, float cY){
        float dx = (x+radius) - (cX+cRadius);
        float dy = (y+radius) - (cY+cRadius);
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (distance < radius + cRadius) return true;
        return false;
    }

    boolean isVisible() {
        if(x > - width && x < point.x && y > - height && y < point.y) return true;
        return false;
    }

}
