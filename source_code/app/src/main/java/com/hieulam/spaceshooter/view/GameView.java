package com.hieulam.spaceshooter.view;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceView;

import com.hieulam.spaceshooter.GameOverActivity;
import com.hieulam.spaceshooter.GamePlayActivity;
import com.hieulam.spaceshooter.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class GameView extends SurfaceView implements Runnable {

    private List<Rock> rocks;
    private List<Bullet> bullets;
    private Boss boss;
    private List<BossBullet> bossBullets;
    private Thread thread;
    private boolean isPlaying;
    private int screenX, screenY, score = 0, heartNumber = 3;
    private SpaceShip spaceShip;
    public static float screenRatioX, screenRatioY;
    private float currentTouchX, currentTouchY, previousTouchX, previousTouchY, backgroundMove, scoreX, scoreY;
    private Paint paintScore;
    private Background background1, background2;
    private float shootingTime = 0, rockDropTime = 0, bossShootingTime = 0;
    private GamePlayActivity activity;

    private List<Heart> hearts;

    public GameView(GamePlayActivity activity, int screenX, int screenY) {
        super(activity);

        this.activity = activity;

        this.screenX = screenX;
        this.screenY = screenY;
        screenRatioX = screenX / 1440f;
        screenRatioY = screenY / 3120f;

        // BACKGROUND MOVING
        backgroundMove = 10 * screenRatioY;
        background1 = new Background(screenX, screenY, getResources());
        background2 = new Background(screenX, screenY, getResources());
        background2.y = - screenY;

        // SPACESHIP
        spaceShip = new SpaceShip(screenX, screenY, getResources());

        // BULLET FOR SHOOTING
        bullets = new ArrayList<>();

        for(int i = 0; i < 20; i++) {
            bullets.add(new Bullet(getResources()));
        }

        // ROCK FOR HINDRANCE

        rocks = new ArrayList<>();
        /*
        for(int i = 0; i < 20; i++) {
            rocks.add(new Rock(getResources()));
        }
        */

        // BOSS
        boss = new Boss(getResources());
        boss.spawnBoss(screenX,screenY,1,3, 100);

        // BOSS BULLET
        bossBullets = new ArrayList<>();
        for(int i = 0; i < 40; i++) {
            bossBullets.add(new BossBullet(getResources()));
        }

        // CONFIGURE PAIN FOR SCORE TEXT
        scoreX = 0 * screenRatioX;
        scoreY = 100 * screenRatioY;
        paintScore = new Paint();
        paintScore.setColor(Color.WHITE);
        paintScore.setTextSize(100 * screenRatioY);

        // SOUND


        // HEART CONTAINER
        hearts = new ArrayList<>();
        hearts.add(new Heart(screenX, screenY, getResources()));
        hearts.add(new Heart(screenX, screenY, getResources()));
        hearts.add(new Heart(screenX, screenY, getResources()));
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

        background1.y += backgroundMove;
        background2.y += backgroundMove;

        if(background1.y - background1.background.getHeight() > 0) {
            background1.y = - screenY;
        }

        if(background2.y - background2.background.getHeight() > 0) {
            background2.y = - screenY;
        }

        // CHECK IMPACT BETWEEN ROCK, BULLET AND SPACESHIP
        for(Rock rock : rocks) {
            if(rock.isVisible()) {
                for(Bullet bullet : bullets) {
                    if(bullet.isVisible()) {
                        if(rock.CircleCollisionDetect(bullet.radius,bullet.x,bullet.y)) {
                            bullet.x = - 500;
                            rock.x = - 500;
                            score += 10;

                            MainActivity.soundList.playSound(1);
                        }
                    }

                    if(rock.CircleCollisionDetect(spaceShip.radius,spaceShip.x,spaceShip.y))  {
                        rock.x = - 500;
                        spaceShipGotShot();
                    }
                }
            }
        }
        // CHECK BOSS
        if(boss.hp>0) {
            for(Bullet bullet : bullets) {
                if(bullet.isVisible()) {
                    if(boss.CircleCollisionDetect(bullet.radius,bullet.x,bullet.y)) {
                        bullet.x = - 500;
                        score += 5;
                        boss.hp--;
                        if (boss.hp<=0) score += 1000;
                        MainActivity.soundList.playSound(1);
                    }
                }


            }
            if(boss.CircleCollisionDetect(spaceShip.radius,spaceShip.x,spaceShip.y)) {
                spaceShipGotShot();
            }
            for(BossBullet bossBullet : bossBullets) {
                if (bossBullet.isVisible()) {
                    if (spaceShip.CircleCollisionDetect(bossBullet.radius,bossBullet.x,bossBullet.y)) {
                        bossBullet.x = -500;
                        spaceShipGotShot();
                    }
                }
            }
            //BOSS SHOOTING
            bossShootingTime++;
            if(bossShootingTime > 50) {
                bossShootingTime = 0;
                int bulletCountMax, bulletCount = 1;
                if (boss.hp>60) bulletCountMax = 1;
                    else if (boss.hp>10) bulletCountMax = 3;
                        else bulletCountMax = 5;
                for(BossBullet bossBullet : bossBullets) {
                    if(!bossBullet.isVisible()) {
                        bossBullet.x = boss.x + boss.radius;
                        bossBullet.y = boss.y + boss.radius;
                        int angle = (int)(Math.atan2(spaceShip.y + spaceShip.height/2 - bossBullet.y, spaceShip.x +spaceShip.width/2 - bossBullet.x) * 180 / Math.PI);

                        if (bulletCount%2 == 1)
                            angle+=(bulletCount/2) *10;
                        else if (bulletCount%2 == 0)
                            angle-=(bulletCount/2) *10;
                        if(angle < 0){
                            angle += 360;
                        }
                        else if(angle > 360){
                            angle -= 360;
                        }
                        bossBullet.setBullet(angle,4);
                        bulletCount++;
                        if (bulletCount>bulletCountMax)
                            break;
                    }
                }
            }
        }


        // SHOOTING
        shootingTime++;
        if(shootingTime > 10) {
            shootingTime = 0;
            for(Bullet bullet : bullets) {
                if(!bullet.isVisible()) {
                    bullet.x = spaceShip.x + spaceShip.radius - bullet.radius;
                    bullet.y = spaceShip.y;
                    break;
                }
            }
        }

        // GENERATE ROCK
        rockDropTime++;
        if(rockDropTime > 15) {
            rockDropTime = 0;
            for(Rock rock : rocks) {
                if(!rock.isVisible()) {
                    rock.x = (int)(Math.random()*(screenX - rock.width)+1);
                    rock.y = - rock.height + 1;
                    break;
                }
            }
        }
    }

    private void draw() {
        if(getHolder().getSurface().isValid()) {

            Canvas canvas = getHolder().lockCanvas();

            canvas.drawColor(Color.parseColor("#19183e"));

            canvas.drawBitmap(background1.background, background1.x, background1.y, null);
            canvas.drawBitmap(background2.background, background2.x, background2.y, null);
            canvas.drawBitmap(spaceShip.getSpaceShip(), spaceShip.x, spaceShip.y, null);

            for(Bullet bullet : bullets) {
                if(bullet.isVisible()) {
                    bullet.y = bullet.y - 30 * screenRatioY;
                    canvas.drawBitmap(bullet.getBullet(), bullet.x, bullet.y, null);
                }
            }

            for (Rock rock : rocks) {
                if(rock.isVisible()) {
                    rock.y = rock.y + 20 * screenRatioY;
                    canvas.drawBitmap(rock.getRock(), rock.x, rock.y, null);
                }
            }

            if(boss.hp>0) {
                boss.moveOneStepWithCollisionDetection();
                canvas.drawBitmap(boss.getBoss(), boss.x, boss.y, null);
                for(BossBullet bossBullet : bossBullets) {
                    if(bossBullet.isVisible()) {
                        bossBullet.moveForward();
                        canvas.drawBitmap(bossBullet.getBullet(), bossBullet.x, bossBullet.y, null);
                    }
                }
            }

            canvas.drawText("Score: " + score, scoreX, scoreY, paintScore);

            int count = 1;
            for(Heart heart : hearts) {
                canvas.drawBitmap(heart.getHeart(), screenX - (heart.width * count), heart.y, null);
                count++;
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

    private void spaceShipGotShot(){
        MainActivity.soundList.playSound(2);
        if(heartNumber == 1) {
            Intent intent = new Intent(activity, GameOverActivity.class);
            intent.putExtra("high_score", score+"");
            activity.startActivity(intent);
            activity.finish();
            MainActivity.soundList.stopMusic();
            return;
        }
        heartNumber--;
        hearts.get(2 - heartNumber).isLive = false;
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
