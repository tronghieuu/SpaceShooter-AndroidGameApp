package com.hieulam.spaceshooter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int GG_SIGN_IN = 1;
    private GoogleSignInClient mGoogleSignInClient;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_menu);

        init();
        MainActivity.soundList.playMusic(getApplicationContext(),1);
    }

    private void init(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        findViewById(R.id.tvPlay).setOnClickListener(this);
        findViewById(R.id.tvRank).setOnClickListener(this);
        findViewById(R.id.tvAccount).setOnClickListener(this);

        dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_progress);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvPlay:
                MainActivity.soundList.stopMusic();
                startActivity(new Intent(this, GamePlayActivity.class));
                break;
            case R.id.tvRank:
                if(FirebaseAuth.getInstance().getCurrentUser() != null) {
                    startActivity(new Intent(this, RankingActivity.class));
                } else {
                    Toast.makeText(this, "You need to login!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tvAccount:
                if(FirebaseAuth.getInstance().getCurrentUser() == null) {
                    login();
                } else {
                    startActivity(new Intent(this, AccountActivity.class));
                }
        }
    }

    private void login() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GG_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GG_SIGN_IN) {
            dialog.show();
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            final GoogleSignInAccount account;
            try {
                account = task.getResult(ApiException.class);
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                FirebaseAuth.getInstance().signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        FirebaseFirestore.getInstance().collection("user")
                                .document(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    dialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Login success!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Map<String, Object> data = new HashMap<>();
                                    if(account.getPhotoUrl() != null) {
                                        data.put("image", account.getPhotoUrl().toString());
                                    } else data.put("image", "");
                                    data.put("username", account.getDisplayName());
                                    data.put("age", "");
                                    data.put("gender", "");
                                    data.put("highScore", 0);
                                    FirebaseFirestore.getInstance().collection("user")
                                            .document(user.getUid())
                                            .set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            dialog.dismiss();
                                            Toast.makeText(getApplicationContext(), "Login success!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        });
                    }
                });
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }
}
