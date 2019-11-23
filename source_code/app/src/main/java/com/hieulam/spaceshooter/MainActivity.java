package com.hieulam.spaceshooter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {
    public static SoundManager soundList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        soundList = new SoundManager();
        soundList.InitSound(getApplicationContext());
        SystemClock.sleep(1000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Apply activity transition
            startActivity(new Intent(this, MainMenuActivity.class));
            finish();
        } else {
            // Swap without transition
            startActivity(new Intent(this, MainMenuActivity.class));
            finish();
        }
    }
}
