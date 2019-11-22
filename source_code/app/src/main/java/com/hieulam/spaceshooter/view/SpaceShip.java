package com.hieulam.spaceshooter.view;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.hieulam.spaceshooter.R;

import static com.hieulam.spaceshooter.view.GameView.screenRatioX;
import static com.hieulam.spaceshooter.view.GameView.screenRatioY;

public class SpaceShip {

    int width, height, spaceShipFrame = 0;
    float x, y;
    Bitmap spaceShip1, spaceShip2, spaceShip3, spaceShip4, spaceShip5, spaceShip6, spaceShip7, spaceShip8;

    SpaceShip(int screenX, int screenY, Resources res) {
        spaceShip1 = BitmapFactory.decodeResource(res, R.drawable.spaceship1);
        spaceShip2 = BitmapFactory.decodeResource(res, R.drawable.spaceship2);
        spaceShip3 = BitmapFactory.decodeResource(res, R.drawable.spaceship3);
        spaceShip4 = BitmapFactory.decodeResource(res, R.drawable.spaceship4);
        spaceShip5 = BitmapFactory.decodeResource(res, R.drawable.spaceship5);
        spaceShip6 = BitmapFactory.decodeResource(res, R.drawable.spaceship6);
        spaceShip7 = BitmapFactory.decodeResource(res, R.drawable.spaceship7);
        spaceShip8 = BitmapFactory.decodeResource(res, R.drawable.spaceship8);

        width = spaceShip1.getWidth();
        height = spaceShip1.getHeight();

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        spaceShip1 = Bitmap.createScaledBitmap(spaceShip1, width, height, false);
        spaceShip2 = Bitmap.createScaledBitmap(spaceShip2, width, height, false);
        spaceShip3 = Bitmap.createScaledBitmap(spaceShip3, width, height, false);
        spaceShip4 = Bitmap.createScaledBitmap(spaceShip4, width, height, false);
        spaceShip5 = Bitmap.createScaledBitmap(spaceShip5, width, height, false);
        spaceShip6 = Bitmap.createScaledBitmap(spaceShip6, width, height, false);
        spaceShip7 = Bitmap.createScaledBitmap(spaceShip7, width, height, false);
        spaceShip8 = Bitmap.createScaledBitmap(spaceShip8, width, height, false);

        x = screenX / 2 - width / 2;
        y = 2000 * screenRatioY;
    }

    Bitmap getSpaceShip() {
        switch (spaceShipFrame) {
            case 0:
                spaceShipFrame++;
                return spaceShip1;
            case 1:
                spaceShipFrame++;
                return spaceShip2;
            case 2:
                spaceShipFrame++;
                return spaceShip3;
            case 3:
                spaceShipFrame++;
                return spaceShip4;
            case 4:
                spaceShipFrame++;
                return spaceShip5;
            case 5:
                spaceShipFrame++;
                return spaceShip6;
            case 6:
                spaceShipFrame++;
                return spaceShip7;
            case 7:
                spaceShipFrame = 0;
                return spaceShip8;
        }
        return spaceShip1;
    }

    Rect getCollisionShape() {
        return new Rect((int) x, (int) y, (int) (x + width), (int) (y + height));
    }
}
