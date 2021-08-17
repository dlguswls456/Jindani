package org.techtown.Jindani.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.techtown.Jindani.R;
import org.techtown.Jindani.models.DoctorAccount;
import org.techtown.Jindani.models.UserAccount;

public class LoginActivity extends AppCompatActivity {

    private SignInButton btn_google;//구글 로그인 버튼
    private Button btn_login;//일반 로그인 버튼
    private Button btn_register;//이메일 회원가입 버튼

    private EditText et_email, et_pwd;

    private FirebaseAuth auth;//파이어 베이스 인증 객체
    private GoogleSignInClient googleSignInClient;//구글 API 클라이언트 객체
    private DatabaseReference databaseReference; // 실시간 데이터베이스
    private static final int RC_SIGN_IN = 100;//로그인 결과 코드

    private final static String TAG = "LoginTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 파이에베이스 인증 객체 초기화
        auth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference("JindaniApp");

        //자동 로그인
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            checkUserOrDoctor(currentUser);
        }

        //일반 이메일 로그인하는 경우
        et_email = findViewById(R.id.et_email);
        et_pwd = findViewById(R.id.et_pwd);

        //일반 로그인 버튼
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_email.getText().toString();
                String password = et_pwd.getText().toString();//비번 6글자 이상이어야함

                if (email.equals("") | password.equals("")) {//빈칸인 경우
                    Toast.makeText(LoginActivity.this, "모든 정보를 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {//모든 조건 충족시 로그인 시도
                    signIn(email, password);
                }

            }
        });

        //구글 로그인하는 경우
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        //구글 로그인 버튼
        btn_google = findViewById(R.id.btn_google);
        btn_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        //회원가입 버튼
        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, UserSegmentActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        //자동 로그인
//        FirebaseUser currentUser = auth.getCurrentUser();
//        if (currentUser != null) {
//            checkUserOrDoctor(currentUser);
//        }
    }

    //일반 로그인
    private void signIn(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();
                            //updateUI(user);

                            //의사인지 아닌지 확인 및 의사라면 권한 있는지 확인
                            checkUserOrDoctor(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "아이디, 비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });

    }

    //로그인 계정 유형 확인
    private void checkUserOrDoctor(FirebaseUser user) {
        databaseReference.child("UserOrDoctor").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {//사용자 데이터 성공적으로 가져오면
                String UserOrDoctor = snapshot.getValue(String.class);

                if (UserOrDoctor.equals("user")) {
                    //로그인한 사람이 일반 사용자 계정이라면
                    Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, UserMainActivity.class);
                    startActivity(intent);
                    finish();
                } else if (UserOrDoctor.equals("doctor")){
                    //의사 계정이라면
                    checkIsAuthorized(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {//데이터 가져오기 실패
                Log.e(TAG, String.valueOf(error.toException()));
            }
        });
    }

    //의사 가입 권한 있는지 확인
    private void checkIsAuthorized(FirebaseUser user) {
        databaseReference.child("DoctorAccount").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {//사용자 데이터 성공적으로 가져오면
                DoctorAccount doctorAccount = snapshot.getValue(DoctorAccount.class);

                if (doctorAccount.isAuthorized()) {
                    //권한이 있는 의사라면
                    Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, DoctorMainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    //권한이 없는 의사라면
                    Toast.makeText(LoginActivity.this, "부여된 권한이 없습니다", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {//데이터 가져오기 실패
                Log.e(TAG, String.valueOf(error.toException()));
            }
        });
    }

    //구글 로그인
    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {//인증 성공 여부 확인
                // Google Sign In was successful, authenticate with Firebase
                // account는 구글 로그인 정보 담고 있음(이메일 등등)
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(getApplicationContext(), "Google sign in Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {//실제로 로그인 성공했는지
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {//로그인 성공
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                            //updateUI(user);
                            Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();

                            readFirebase(user);
                        } else {//로그인 실패
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();

                            // updateUI(null);
                        }
                    }
                });
    }

    //파이어베이스에서 구글 유저 데이터 확인하기
    public void readFirebase(FirebaseUser user) {
        databaseReference.child("UserAccount").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {//사용자 데이터 성공적으로 가져오면
                UserAccount userAccount = snapshot.getValue(UserAccount.class);

                if (userAccount != null) {
                    //기본정보가 등록되어있는 유저라면 UserMainActivity로 이동
                    Intent intent = new Intent(LoginActivity.this, UserMainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    //처음 구글로그인으로 가입한 사람일 때 -> PersonInfo 페이지로 이동해 디비 넣어줘야함
                    Intent intent = new Intent(LoginActivity.this, PersonInfoActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {//데이터 가져오기 실패
                Log.e(TAG, String.valueOf(error.toException()));
            }
        });
    }

    //로그아웃
    private void signOut() {
        // Firebase sign out
        auth.signOut();

//        // Google sign out
//        googleSignInClient.signOut().addOnCompleteListener(this,
//                new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        Toast.makeText(getApplicationContext(), "로그아웃 성공", Toast.LENGTH_SHORT).show();
//                    }
//                });
    }

    private void revokeAccess() {
        // Firebase sign out
        auth.signOut();

        // Google revoke access
        googleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Complete", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateUI(FirebaseUser user) {

    }

}
