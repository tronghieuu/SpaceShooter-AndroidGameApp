package com.hieulam.spaceshooter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class RankingActivity extends AppCompatActivity {

    private TextView mTvYourRank, mTvYourScore;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        mTvYourRank = findViewById(R.id.tvYourRank);
        mTvYourScore = findViewById(R.id.tvYourScore);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        getData();
    }

    private void getData() {
        FirebaseFirestore.getInstance().collection("user").document(mUser.getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    mTvYourScore.setText(documentSnapshot.getLong("highScore")+"");
                    final long score = documentSnapshot.getLong("highScore");
                    FirebaseFirestore.getInstance().collection("user")
                            .whereLessThan("highScore", score).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            mTvYourRank.setText("#"+(queryDocumentSnapshots.size()+1));
                        }
                    });
                }
            }
        });
    }
}
