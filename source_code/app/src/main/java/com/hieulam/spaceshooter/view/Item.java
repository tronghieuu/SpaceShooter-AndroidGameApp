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

public class Item {

    int width, height, type;
    float x, y, radius, speedY;
    Bitmap item;

    Item(Resources res, int type) {
        this.type = type;
        if (type==1)
            item = BitmapFactory.decodeResource(res, R.drawable.item_coin);
        else if (type==2)
            item = BitmapFactory.decodeResource(res, R.drawable.item_diamond);
        else if (type==3)
            item = BitmapFactory.decodeResource(res, R.drawable.item_heart_up);
        else if (type==4)
            item = BitmapFactory.decodeResource(res, R.drawable.item_blueprint);

        width = item.getWidth();
        height = item.getHeight();
        width = (int) (width * MainActivity.density * 1f);
        height = (int) (height * MainActivity.density * 1f);

        item = Bitmap.createScaledBitmap(item, width, height, false);

        x = - 500;
        y = - 500;

        radius = width/2;
    }

    public void moveForward() {
        y += speedY;
    }

    public void setItem(float x, float y, float speedY) {
        this.x = x;
        this.y = y;
        this.speedY = speedY;
    }

    public boolean CircleCollisionDetect(float cRadius, float cX, float cY){
        float dx = (x+radius) - (cX+cRadius);
        float dy = (y+radius) - (cY+cRadius);
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (distance < radius + cRadius) return true;
        return false;
    }

    Bitmap getItem() {
        return item;
    }


    Rect getCollisionShape() {
        return new Rect((int) x, (int) y, (int) x + width, (int) y + height);
    }

    boolean isVisible() {
        if(x > - width && x < point.x && y > - height && y < point.y) return true;
        return false;
    }
}
