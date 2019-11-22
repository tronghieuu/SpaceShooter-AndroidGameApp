package com.hieulam.spaceshooter.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {

    private List<Rock> rocks;
    private List<Bullet> bullets;
    private Thread thread;
    private boolean isPlaying;
    private int screenX, screenY;
    private SpaceShip spaceShip;
    public static float screenRatioX, screenRatioY;
    private float currentTouchX, currentTouchY, previousTouchX, previousTouchY;
    private Paint paint;
    private Background background1, background2;
    private float shootingTime = 0, rockDropTime = 0;

    public GameView(Context context, int screenX, int screenY) {
        super(context);

        this.screenX = screenX;
        this.screenY = screenY;
        screenRatioX = screenX / 1440f;
        screenRatioY = screenY / 3120f;

        background1 = new Background(screenX, screenY, getResources());
        background2 = new Background(screenX, screenY, getResources());

        spaceShip = new SpaceShip(screenX, screenY, getResources());

        bullets = new ArrayList<>();
        for(int i = 0; i < 20; i++) {
            bullets.add(new Bullet(getResources()));
        }

        rocks = new ArrayList<>();
        for(int i = 0; i < 20; i++) {
            rocks.add(new Rock(getResources()));
        }

        background2.y = - screenY;

        paint = new Paint();
    }

    @Override
    public void run() {
        while (isPlaying) {
            update();
            draw();
            sleep();
        }
    }

    private void update() {
        background1.y += 10 * screenRatioY;
        background2.y += 10 * screenRatioY;

        if(background1.y - background1.background.getHeight() > 0) {
            background1.y = - screenY;
        }

        if(background2.y - background2.background.getHeight() > 0) {
            background2.y = - screenY;
        }

        shootingTime++;
        if(shootingTime > 10) {
            shootingTime = 0;
            for(Bullet bullet : bullets) {
                if(!bullet.isVisible()) {
                    bullet.x = spaceShip.x + (float) spaceShip.width / 2;
                    bullet.y = spaceShip.y;
                    return;
                }
            }
        }

        rockDropTime++;
        if(rockDropTime > 15) {
            rockDropTime = 0;
            for(Rock rock : rocks) {
                if(!rock.isVisible()) {
                    rock.x = (int)(Math.random()*(screenX - rock.width)+1);
                    rock.y = - rock.height + 1;
                    return;
                }
            }
        }
    }

    private void draw() {
        if(getHolder().getSurface().isValid()) {

            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint);

            canvas.drawBitmap(spaceShip.getSpaceShip(), spaceShip.x, spaceShip.y, paint);

            for(Bullet bullet : bullets) {
                if(bullet.isVisible()) {
                    bullet.y = bullet.y - 30 * screenRatioY;
                    canvas.drawBitmap(bullet.getBullet(), bullet.x, bullet.y, paint);
                }
            }

            for (Rock rock : rocks) {
                if(rock.isVisible()) {
                    rock.y = rock.y + 20 * screenRatioY;
                    canvas.drawBitmap(rock.getRock(), rock.x, rock.y, paint);
                }
            }
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void sleep() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pause() {
        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        currentTouchX = event.getX();
        currentTouchY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                previousTouchX = event.getX();
                previousTouchY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if(spaceShip.getCollisionShape().contains((int) currentTouchX, (int) currentTouchY)) {
                    float distanceX = currentTouchX - previousTouchX;
                    float distanceY = currentTouchY - previousTouchY;
                    spaceShip.x += distanceX;
                    spaceShip.y += distanceY;
                    if(spaceShip.x < - (float) (spaceShip.width / 2) || spaceShip.x > screenX - (float) (spaceShip.width / 2)) {
                        spaceShip.x -= distanceX;
                    }
                    if(spaceShip.y < 0 || spaceShip.y > screenY - spaceShip.height) {
                        spaceShip.y -= distanceY;
                    }
                }
                previousTouchX = currentTouchX;
                previousTouchY = currentTouchY;
                break;
        }
        return true;
    }
}
