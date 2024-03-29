package com.hieulam.spaceshooter;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.view.WindowManager;

import com.hieulam.spaceshooter.view.GameView;

public class GamePlayActivity extends AppCompatActivity {

    private GameView mGameView;
    public static Point point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        MainActivity.soundList.playMusic(getApplicationContext(),2);
    }

    private void init(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);

        mGameView = new GameView(this, point.x, point.y);

        setContentView(mGameView);
    }


    @Override
    protected void onPause() {
        super.onPause();
        MainActivity.soundList.pauseMusic();
        mGameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainActivity.soundList.resumeMusic();
        mGameView.resume();
    }
}
