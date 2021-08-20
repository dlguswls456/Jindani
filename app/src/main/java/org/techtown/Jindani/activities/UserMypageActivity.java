package org.techtown.Jindani.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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

public class UserMypageActivity extends AppCompatActivity {

    TextView tv_id, tv_sex, tv_birth, tv_height_weight, tv_family, tv_past, tv_social;
    Button btn_logout, btn_delete, btn_updateInfo, btn_updateQnA;

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference databaseReference; // 실시간 데이터베이스

    UserAccount userAccount;

    private final static String TAG = "MyPageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_mypage);

        tv_id = findViewById(R.id.tv_id);
        tv_sex = findViewById(R.id.tv_sex);
        tv_birth = findViewById(R.id.tv_birth);
        tv_height_weight = findViewById(R.id.tv_height_weight);
        tv_family = findViewById(R.id.tv_family);
        tv_past = findViewById(R.id.tv_past);
        tv_social = findViewById(R.id.tv_social);

        databaseReference = FirebaseDatabase.getInstance().getReference("JindaniApp");
        setUserInfo();

        btn_logout = findViewById(R.id.btn_logout);
        btn_delete = findViewById(R.id.btn_delete);
        btn_updateInfo = findViewById(R.id.btn_updateInfo);
        btn_updateQnA = findViewById(R.id.btn_updateQnA);

        //각 버튼 클릭
        btn_logout.setOnClickListener(buttonClickListener);
        btn_delete.setOnClickListener(buttonClickListener);
        btn_updateInfo.setOnClickListener(buttonClickListener);
        btn_updateQnA.setOnClickListener(buttonClickListener);


    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.child("UserAccount").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {//사용자 데이터 성공적으로 가져오면
                userAccount = snapshot.getValue(UserAccount.class);

                tv_id.setText("아이디: " + user.getEmail());
                tv_sex.setText("성별: " + userAccount.getSex());
                tv_birth.setText("생년월일: " + userAccount.getBirthDate());
                tv_height_weight.setText("키/몸무게: " + userAccount.getHeight() + "cm/" + userAccount.getWeight() + "kg");
                tv_family.setText("가족력: " + userAccount.getFamily());
                tv_past.setText("과거력: " + userAccount.getPast());
                tv_social.setText("사회력: " + userAccount.getSocial());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //데이터 가져오기 실패 처리
                Log.e(TAG, String.valueOf(error.toException()));
            }
        });
    }

    private void setUserInfo() {
//        databaseReference.child("UserAccount").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {//사용자 데이터 성공적으로 가져오면
//                userAccount = snapshot.getValue(UserAccount.class);
//
//        tv_id.setText("아이디: " + user.getEmail());
//        tv_sex.setText("성별: " + userAccount.getSex());
//        tv_birth.setText("생년월일: " + userAccount.getBirthDate());
//        tv_height_weight.setText("키/몸무게: " + userAccount.getHeight() + "cm/" + userAccount.getWeight() + "kg");
//        tv_family.setText("가족력: " + userAccount.getFamily());
//        tv_past.setText("과거력: " + userAccount.getPast());
//        tv_social.setText("사회력: " + userAccount.getSocial());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                //데이터 가져오기 실패 처리
//                Log.e(TAG, String.valueOf(error.toException()));
//            }
//        });
    }

    View.OnClickListener buttonClickListener = view -> {
        switch (view.getId()) {
            case R.id.btn_logout: { //로그아웃 버튼
                showLogoutDialog();
                break;
            }
            case R.id.btn_delete: { //회원 탈퇴 버튼
                showDeleteDialog();
                break;
            }
            case R.id.btn_updateInfo: { //계정 정보 변경 버튼
                updateUserInfo();
                break;
            }
            case R.id.btn_updateQnA: { //질문 목록 변경 버튼
                updateQnA();
                break;
            }
        }
    };

    //질문목록 수정
    private void updateQnA() {
        Intent intent = new Intent(UserMypageActivity.this, UserUpdateQnaActivity.class);
        intent.putExtra("userId", user.getUid());

        startActivity(intent);
    }

    //사용자 정보 변경
    private void updateUserInfo() {
        Intent intent = new Intent(UserMypageActivity.this, UpdateUserInfoActivity.class);
        intent.putExtra("userAccount", userAccount);
        startActivity(intent);
    }

    private void signOutAndFinish() {
        signOut();
        //모든 액티비티 삭제
        ActivityCompat.finishAffinity(UserMypageActivity.this);

        Intent intent = new Intent(UserMypageActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    //로그아웃 대화창
    public void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserMypageActivity.this);
        builder.setMessage("로그아웃 하시겠습니까?");
        builder.setCancelable(false);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                signOutAndFinish();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    //회원 탈퇴 대화창
    public void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserMypageActivity.this);
        builder.setMessage("회원 탈퇴 하시겠습니까?");
        builder.setCancelable(false);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteUser();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
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
                        Toast.makeText(UserMypageActivity.this, "로그아웃 성공", Toast.LENGTH_LONG).show();
                    }
                });
    }

    //회원 탈퇴
    public void deleteUser() {
        ////관련된 모든 db삭제
        databaseReference.child("UserAccount").child(user.getUid()).removeValue();
        databaseReference.child("UserOrDoctor").child(user.getUid()).removeValue();

        //계정 삭제
        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {//계정 삭제 성공
                            Log.d(TAG, "계정 탈퇴 성공");

                            Toast.makeText(UserMypageActivity.this, "계정이 정상적으로 탈퇴처리 되었습니다", Toast.LENGTH_LONG).show();

                            //모든 액티비티 삭제 및 로그인 페이지로 이동
                            ActivityCompat.finishAffinity(UserMypageActivity.this);

                            Intent intent = new Intent(UserMypageActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {//계정 삭제 실패
                            //db 다시 복구
                            databaseReference.child("UserAccount").child(user.getUid()).setValue(userAccount);
                            databaseReference.child("UserOrDoctor").child(user.getUid()).setValue("user");

                            Toast.makeText(UserMypageActivity.this, "탈퇴 실패", Toast.LENGTH_LONG).show();
                        }
                    }

                });
    }

}
