package com.hieulam.spaceshooter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RankingActivity extends AppCompatActivity {

    private TextView mTvYourRank, mTvYourScore;
    private FirebaseUser mUser;
    private List<Rank> mRanks;
    private RecyclerView recyclerView;
    private RankAdapter mAdapter;
    private SwipeRefreshLayout mSwipe;
    private boolean isLoading = false;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ranking);

        mTvYourRank = findViewById(R.id.tvYourRank);
        mTvYourScore = findViewById(R.id.tvYourScore);
        mRanks = new ArrayList<>();
        mSwipe = findViewById(R.id.swipeRefreshLayout);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new RankAdapter(this, mRanks, recyclerView);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemListener(new RankAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Rank item) {

            }
        });
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!isLoading) {
                    mRanks.clear();
                    mAdapter.notifyDataSetChanged();
                    getData();
                }
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        findViewById(R.id.tvLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                mGoogleSignInClient.signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        finish();
                    }
                });
            }
        });

        findViewById(R.id.tvBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getData();
    }

    private void getData() {
        isLoading = true;
        FirebaseFirestore.getInstance().collection("user")
                .orderBy("highScore", Query.Direction.DESCENDING)
                .limit(10).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int rank = 0;
                for (DocumentSnapshot doc : queryDocumentSnapshots) {
                    rank++;
                    mRanks.add(new Rank(doc.getId(), doc.getString("username"),
                            doc.getLong("highScore")+"",
                            doc.getString("image"),
                            doc.getString("age"),
                            doc.getString("gender"),
                            rank));
                    mAdapter.notifyDataSetChanged();
                }
                mSwipe.setRefreshing(false);
                isLoading = false;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                isLoading = false;
                mSwipe.setRefreshing(false);
            }
        });
        FirebaseFirestore.getInstance().collection("user").document(mUser.getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    mTvYourScore.setText(documentSnapshot.getLong("highScore")+"");
                    final long score = documentSnapshot.getLong("highScore");
                    FirebaseFirestore.getInstance().collection("user")
                            .whereGreaterThan("highScore", score).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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
