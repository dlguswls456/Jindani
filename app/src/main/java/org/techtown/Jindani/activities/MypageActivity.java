package org.techtown.Jindani.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.techtown.Jindani.R;
import org.techtown.Jindani.models.UserAccount;
import org.techtown.Jindani.network.FirebaseCallback;

import java.util.Calendar;
import java.util.HashMap;

public class MypageActivity extends AppCompatActivity {

    TextView tv_id, tv_sex, tv_birth, tv_height_weight, tv_family, tv_past, tv_social;
    Button btn_logout, btn_delete;

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference databaseReference; // 실시간 데이터베이스

    private final static String TAG = "MyPageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        tv_id = findViewById(R.id.tv_id);
        tv_sex = findViewById(R.id.tv_sex);
        tv_birth = findViewById(R.id.tv_birth);
        tv_height_weight = findViewById(R.id.tv_height_weight);
        tv_family = findViewById(R.id.tv_family);
        tv_past = findViewById(R.id.tv_past);
        tv_social = findViewById(R.id.tv_social);

        databaseReference = FirebaseDatabase.getInstance().getReference("JindaniApp");
        setUserInfo(new FirebaseCallback<UserAccount>() {
            @Override
            public void onCallback(UserAccount userAccount) {
                tv_id.append(user.getEmail());
                tv_sex.append(userAccount.getSex());
                tv_birth.append(userAccount.getBirthDate());
                tv_height_weight.append(userAccount.getHeight() + "cm / " + userAccount.getWeight() + "kg");
                tv_family.append(userAccount.getFamily());
                tv_past.append(userAccount.getPast());
                tv_social.append(userAccount.getSocial());
            }
        });

        btn_logout = findViewById(R.id.btn_logout);
        btn_delete = findViewById(R.id.btn_delete);

        //각 버튼 클릭
        btn_logout.setOnClickListener(buttonClickListener);
        btn_delete.setOnClickListener(buttonClickListener);


    }

    private void setUserInfo(FirebaseCallback<UserAccount> firebaseCallback) {
        databaseReference.child("UserAccount").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {//사용자 데이터 성공적으로 가져오면
                UserAccount userAccount = snapshot.getValue(UserAccount.class);

                firebaseCallback.onCallback(userAccount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //데이터 가져오기 실패 처리
                Log.e(TAG, String.valueOf(error.toException()));
            }
        });
    }

    View.OnClickListener buttonClickListener = view -> {
        switch (view.getId()) {
            case R.id.btn_logout: { //채팅 버튼
                signOutAndFinish();
                break;
            }
            case R.id.btn_delete: { //qna 버튼
                deleteUser();
                break;
            }
        }
    };

    private void signOutAndFinish() {
        signOut();
        Intent intent = new Intent(MypageActivity.this, LoginActivity.class);
        /////모든 액티비티 삭제하는 과정 필요
        startActivity(intent);
        finish();
    }

    //로그아웃
    private void signOut() {
        // Firebase sign out
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();

        //구글 로그인 사용자에게만 필요.. 나중에 수정 해야함
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);
        // Google sign out
        googleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(MypageActivity.this, "로그아웃 성공", Toast.LENGTH_LONG).show();
                    }
                });
    }

    //회원 탈퇴
    public void deleteUser() {
        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            /////관련된 모든 db삭제하는 과정 필요
                            databaseReference.child("UserAccount").child(user.getUid()).removeValue();
                            databaseReference.child("UserOrDoctor").child(user.getUid()).removeValue();

                            Log.d(TAG, "계정 탈퇴 성공");
                            Toast.makeText(MypageActivity.this, "계정이 정상적으로 탈퇴처리 되었습니다", Toast.LENGTH_LONG).show();

                            /////모든 액티비티 삭제하는 과정 필요
                            finish();
                        }
                    }
                });
    }

    //회원 탈퇴
    private void revokeAccess() {
        // Firebase sign out
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();


//        //구글 로그인 사용자에게만 필요.. 나중에 수정 해야함
//        GoogleSignInOptions gso = new GoogleSignInOptions
//                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();
//
//        // Build a GoogleSignInClient with the options specified by gso.
//        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);
//        // Google revoke access
//        googleSignInClient.revokeAccess().addOnCompleteListener(this,
//                new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        Toast.makeText(getApplicationContext(), "회원탈퇴 성공", Toast.LENGTH_SHORT).show();
//                    }
//                });
    }

}
