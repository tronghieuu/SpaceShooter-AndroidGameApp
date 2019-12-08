package com.hieulam.spaceshooter.view;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.hieulam.spaceshooter.MainActivity;
import com.hieulam.spaceshooter.R;

public class Exit {

    int width, height;
    float x, y;
    Bitmap exit;

    Exit(Resources res, int screenX, int screenY) {
        exit = BitmapFactory.decodeResource(res, R.drawable.home_pressed);

        width = exit.getWidth();
        height = exit.getHeight();

        width = (int) (width * MainActivity.density * 2.2f);
        height = (int) (height * MainActivity.density * 2.2f);

        exit = Bitmap.createScaledBitmap(exit, width, height, false);

        x = screenX - width;
        y = screenY - height;
    }

    Bitmap getExitButton() {
        return  exit;
    }


    Rect getCollisionShape() {
        return new Rect((int) x, (int) y, (int) x + width, (int) y + height);
    }
}
