package com.hieulam.spaceshooter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class GameOverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        TextView tvHighScore = findViewById(R.id.tvHighScore);
        tvHighScore.setText(getIntent().getStringExtra("high_score"));
        MainActivity.soundList.playMusic(getApplicationContext(),3);
        findViewById(R.id.tvMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.soundList.stopMusic();
                startActivity(new Intent(getApplicationContext(), MainMenuActivity.class));
                finish();
            }
        });

        findViewById(R.id.tvReplay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.soundList.stopMusic();
                startActivity(new Intent(getApplicationContext(), GamePlayActivity.class));
                finish();
            }
        });
    }
}
