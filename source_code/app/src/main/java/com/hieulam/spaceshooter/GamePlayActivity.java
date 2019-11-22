package com.hieulam.spaceshooter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GamePlayActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);

        init();
        addListener();
    }

    private void init(){
        mBtnBack = findViewById(R.id.btnBack);
    }

    private void addListener(){
        mBtnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnBack:
                finish();
        }
    }
}
