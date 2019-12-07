package com.hieulam.spaceshooter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class GameOverActivity extends AppCompatActivity {

    private Long mHighScore;
    private Dialog dialog;
    TextView mTvHighScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        mHighScore = Long.parseLong(getIntent().getStringExtra("high_score"));
        mTvHighScore = findViewById(R.id.tvHighScore);
        mTvHighScore.setText("SCORE: "+mHighScore);
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

        dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_progress);

        updateScore();
    }

    private void updateScore() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            dialog.show();
            FirebaseFirestore.getInstance().collection("user")
                    .document(user.getUid())
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists()) {
                        if(documentSnapshot.getLong("highScore") < mHighScore) {
                            mTvHighScore.setText("HIGH SCORE: "+mHighScore);
                            Map<String, Object> data = new HashMap<>();
                            data.put("highScore", mHighScore);
                            FirebaseFirestore.getInstance().collection("user")
                                    .document(user.getUid())
                                    .update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    dialog.dismiss();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    dialog.dismiss();
                                }
                            });
                        } else dialog.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                }
            });
        }
    }
}
