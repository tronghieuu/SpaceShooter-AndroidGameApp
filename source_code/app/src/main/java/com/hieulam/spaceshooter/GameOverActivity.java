package com.hieulam.spaceshooter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class GameOverActivity extends AppCompatActivity {

    private String mHighScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        mHighScore = getIntent().getStringExtra("high_score");
        TextView tvHighScore = findViewById(R.id.tvHighScore);
        tvHighScore.setText(mHighScore);
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

        updateScore();
    }

    private void updateScore() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            FirebaseFirestore.getInstance().collection("user").document(user.getUid())
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("timestamp", FieldValue.serverTimestamp());
                    data.put("highScore", Integer.parseInt(mHighScore));
                    if(documentSnapshot.exists()) {
                        FirebaseFirestore.getInstance().collection("user")
                                .document(user.getUid()).update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });
                    } else {
                        FirebaseFirestore.getInstance().collection("user")
                                .document(user.getUid()).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });
                    }
                }
            });
        }
    }
}
