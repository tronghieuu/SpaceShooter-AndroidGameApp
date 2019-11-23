package com.hieulam.spaceshooter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_menu);

        init();
        addListener();
        MainActivity.soundList.playMusic(3);
    }

    private void init(){
        mTvPlay = findViewById(R.id.tvPlay);
    }

    private  void addListener(){
        mTvPlay.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvPlay:
                MainActivity.soundList.stopMusic(3);
                startActivity(new Intent(this, GamePlayActivity.class));
                break;
        }
    }
}
