package com.hieulam.spaceshooter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        init();
    }

    private void init(){
        mBtnPlay = findViewById(R.id.btnPlay);
    }

    private  void addListener(){
        mBtnPlay.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnPlay:
                startActivity(new Intent(this, GamePlayActivity.class));
                break;
        }
    }
}
