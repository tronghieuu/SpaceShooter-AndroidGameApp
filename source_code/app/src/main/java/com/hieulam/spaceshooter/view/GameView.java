package com.hieulam.spaceshooter.view;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceView;

import com.hieulam.spaceshooter.GameOverActivity;
import com.hieulam.spaceshooter.GamePlayActivity;
import com.hieulam.spaceshooter.MainActivity;
import com.hieulam.spaceshooter.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GameView extends SurfaceView implements Runnable {

    private List<Rock> rocks;
    private List<Bullet> bullets;
    private List<Boss> bosses;
    private List<BossBullet> bossBullets;
    private List<Item> items;
    private Thread thread;
    private boolean isPlaying, isBossesAlive;
    private int screenX, screenY, score = 0, heartNumber = 3, stage = 1, shipBulletCountMax=1;
    private SpaceShip spaceShip;
    public static float screenRatioX, screenRatioY;
    private float currentTouchX, currentTouchY, previousTouchX, previousTouchY, backgroundMove, scoreX, scoreY;
    private Paint paintScore;
    private Background background1, background2;
    private float shootingTime = 0,  rockDropTime = 0, bossTimer = 0;
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

        for(int i = 0; i < 100; i++) {
            bullets.add(new Bullet(getResources()));
        }

        // ROCK FOR HINDRANCE

        rocks = new ArrayList<>();

        for(int i = 0; i < 30; i++) {
            rocks.add(new Rock(getResources()));
        }

        // ITEMS
        items = new ArrayList<>();

        for(int i = 0; i < 15; i++) {
            items.add(new Item(getResources(),1));
        }
        items.add(new Item(getResources(),2));
        items.add(new Item(getResources(),3));
        items.add(new Item(getResources(),4));

        // BOSS
        bosses = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            bosses.add(new Boss(getResources()));
        }

        // BOSS BULLET
        bossBullets = new ArrayList<>();
        for(int i = 0; i < 150; i++) {
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

        // CHECK IMPACT BETWEEN ITEM AND SPACESHIP
        for(Item item : items) {
            if(item.isVisible()) {
                    if(spaceShip.CircleCollisionDetect(item.radius,item.x,item.y))  {
                        item.x = - 500;
                        if (item.type==1)
                            score += 50;
                        else if (item.type==2)
                            score += 500;
                        else if (item.type==3){
                            score += 200;
                            if (heartNumber<3) {
                                hearts.get(2 - heartNumber).isLive = true;
                                heartNumber++;
                            }
                        }
                        else if (item.type==4){
                            score += 200;
                            if (shipBulletCountMax<6)
                                shipBulletCountMax++;
                        }

                    }
            }
        }


        // CHECK IMPACT BETWEEN ROCK, BULLET AND SPACESHIP
        for(Rock rock : rocks) {
            if(rock.isVisible()) {
                for(Bullet bullet : bullets) {
                    if(bullet.isVisible()) {
                        if(rock.CircleCollisionDetect(bullet.radius,bullet.x,bullet.y)) {
                            if (rock.hp <= 1) {
                                if (Math.random() < 0.3)
                                    itemGenerate(rock.x, rock.y, true);

                                bullet.x = -500;
                                rock.x = -500;
                                score += 10;

                                MainActivity.soundList.playSound(1);
                            }
                            else rock.hp--;
                        }
                    }

                    if(spaceShip.CircleCollisionDetect(rock.radius,rock.x,rock.y))  {
                        rock.x = - 500;
                        spaceShipGotShot();
                    }
                }
            }
        }

        // BOSS BULLET CHECK
        for(BossBullet bossBullet : bossBullets) {
            if (bossBullet.isVisible()) {
                if (spaceShip.CircleCollisionDetect(bossBullet.radius,bossBullet.x,bossBullet.y)) {
                    bossBullet.x = -500;
                    spaceShipGotShot();
                }
            }
        }
        // CHECK BOSS
        isBossesAlive=false;
        for(Boss boss : bosses) {
            if (boss.hp > 0) {
                isBossesAlive=true;
                for (Bullet bullet : bullets) {
                    if (bullet.isVisible()) {
                        if (boss.CircleCollisionDetect(bullet.radius, bullet.x, bullet.y)) {
                            bullet.x = -500;
                            score += 5;
                            boss.hp--;
                            if (boss.hp <= 0) {
                                itemGenerate(boss.x, boss.y, false);
                                score += 1000;
                                bossTimer = 0;
                                stage++;
                            }
                            MainActivity.soundList.playSound(1);
                        }
                    }


                }
                if (boss.CircleCollisionDetect(spaceShip.radius, spaceShip.x, spaceShip.y)) {
                    spaceShipGotShot();
                }

                //BOSS SHOOTING
                boss.bossShootingTime++;
                if (boss.bossShootingTime > 50) {
                    boss.bossShootingTime = 0;
                    int bulletCountMax, bulletCount = 1;
                    if (boss.hp > 60) bulletCountMax = 1;
                    else if (boss.hp > 10) bulletCountMax = 3;
                    else bulletCountMax = 5;
                    for (BossBullet bossBullet : bossBullets) {
                        if (!bossBullet.isVisible()) {
                            bossBullet.x = boss.x + boss.radius;
                            bossBullet.y = boss.y + boss.radius;
                            int angle = (int) (Math.atan2(spaceShip.y + spaceShip.height / 2 - bossBullet.y, spaceShip.x + spaceShip.width / 2 - bossBullet.x) * 180 / Math.PI);

                            if (bulletCount % 2 == 1)
                                angle += (bulletCount / 2) * 10;
                            else if (bulletCount % 2 == 0)
                                angle -= (bulletCount / 2) * 10;
                            if (angle < 0) {
                                angle += 360;
                            } else if (angle > 360) {
                                angle -= 360;
                            }
                            bossBullet.setBullet(angle, 4);
                            bulletCount++;
                            if (bulletCount > bulletCountMax)
                                break;
                        }
                    }
                }
            }
        }
        if (!isBossesAlive) {
            // BOSS SPAWN COUNTING TIME
            bossTimer++;
            if (bossTimer>5000) {
                int maxBoss;
                if (stage<3) maxBoss=stage;
                else maxBoss=3;
                for(int b=0;b<maxBoss;b++)
                    bosses.get(b).spawnBoss(screenX,screenY,1,3, 100);
            }
            else {
                // GENERATE ROCK
                rockDropTime++;
                if (rockDropTime > 50) {
                    rockDropTime = 0;
                    int rockCountMax = ThreadLocalRandom.current().nextInt(1, 3 + 1), rockCount = 1;
                    for (Rock rock : rocks) {
                        if (!rock.isVisible()) {
                            rock.setRock((float) (Math.random() * (screenX - rock.width)) + 1, -rock.height + 1, 8,2);
                            rockCount++;
                            if (rockCount > rockCountMax)
                                break;
                        }
                    }
                }
            }
        }

        // SHOOTING
        shootingTime++;
        if(shootingTime > 10) {
            shootingTime = 0;
            int shipBulletCount = 1, angle;
            for(Bullet bullet : bullets) {
                if(!bullet.isVisible()) {
                    angle = 270;
                    bullet.x = spaceShip.x + spaceShip.radius - bullet.radius;
                    bullet.y = spaceShip.y;
                    if (shipBulletCount % 2 == 1)
                        angle += (shipBulletCount / 2) * 5;
                    else if (shipBulletCount % 2 == 0)
                        angle -= (shipBulletCount / 2) * 5;
                    if (angle < 0) {
                        angle += 360;
                    } else if (angle > 360) {
                        angle -= 360;
                    }
                    bullet.setBullet(angle, 10);
                    shipBulletCount++;
                    if (shipBulletCount > shipBulletCountMax)
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
                    bullet.moveForward();
                    canvas.drawBitmap(bullet.getBullet(), bullet.x, bullet.y, null);
                }
            }

            for (Rock rock : rocks) {
                if(rock.isVisible()) {
                    rock.moveForward();
                    canvas.drawBitmap(rock.getRock(), rock.x, rock.y, null);
                }
            }

            for (Item item : items) {
                if(item.isVisible()) {
                    item.moveForward();
                    canvas.drawBitmap(item.getItem(), item.x, item.y, null);
                }
            }

            for (BossBullet bossBullet : bossBullets) {
                if (bossBullet.isVisible()) {
                    bossBullet.moveForward();
                    canvas.drawBitmap(bossBullet.getBullet(), bossBullet.x, bossBullet.y, null);
                }
            }

            for(Boss boss : bosses) {
                if (boss.hp > 0) {
                    boss.moveOneStepWithCollisionDetection();
                    canvas.drawBitmap(boss.getBoss(), boss.x, boss.y, null);
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

    private void itemGenerate(float x, float y, boolean stage){
        int itemType;
        if (stage)
            itemType = (int) (Math.random() * (17+30) - 30);
        else itemType = (int) (Math.random() * (3) + 15);
        if (itemType<15)
            for (int i=0; i<15; i++) {
                if (!items.get(i).isVisible()) {
                    items.get(i).setItem(x, y, 4);
                    return;
                }
            }
        else if (!items.get(itemType).isVisible()) {
            items.get(itemType).setItem(x, y, 4);
            return;
        }
        for (int i=15; i<18; i++)
            if (!items.get(i).isVisible()) {
                items.get(i).setItem(x, y, 4);
                return;
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
                previousTouchX = currentTouchX;
                previousTouchY = currentTouchY;
                break;
        }
        return true;
    }
}
