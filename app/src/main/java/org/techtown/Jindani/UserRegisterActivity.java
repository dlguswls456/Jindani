package org.techtown.Jindani;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserRegisterActivity extends AppCompatActivity {

    //일반 사용자용 회원가입
    private static final String TAG = "UserRegisterActivity";

    private FirebaseAuth mAuth; //파이어베이스 인증 처리
    private DatabaseReference mDatabaseReference; // 실시간 데이터베이스

    private Button btn_register;
    private RadioGroup radioGroup;
    private EditText editHeight, editWeight, editPast, editSocial, editFamily, et_email, et_pwd, et_pwd_again;
    private TextView txtDate, tv_chk_pwd, tv_chk_pwd_again;
    String birthDate;

    private DatePicker datePicker;

    private static final int MINIMUN_PWD_SIZE = 6; //최소 비밀번호 길이

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        mAuth = FirebaseAuth.getInstance();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("JindaniApp");

        et_email = findViewById(R.id.et_email);
        et_pwd = findViewById(R.id.et_pwd);
        et_pwd_again = findViewById(R.id.et_pwd_again);
        tv_chk_pwd = findViewById(R.id.tv_chk_pwd);
        tv_chk_pwd_again = findViewById(R.id.tv_chk_pwd_again);

        radioGroup = findViewById(R.id.radioGroup);
        editHeight = findViewById(R.id.editHeight);
        editWeight = findViewById(R.id.editWeight);
        txtDate = findViewById(R.id.txtDate);
        editPast = findViewById(R.id.editPast);
        editSocial = findViewById(R.id.editSocial);
        editFamily = findViewById(R.id.editFamily);

        datePicker = findViewById(R.id.datePicker);

        //생년월일 선택, 나이 계산
        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                birthDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                //확인용 text
                String birthStr = year + "년 " + (monthOfYear + 1) + "월 " + dayOfMonth + "일";
                txtDate.setText(birthStr);
            }
        });

        //회원가입 버튼
        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_email.getText().toString();
                String password = et_pwd.getText().toString();//비번 6글자 이상이어야함
                String password_again = et_pwd_again.getText().toString();//비번 6글자 이상이어야함

                //로그인 조건.. 추가 필요
                if (email.equals("") | password.equals("") | password_again.equals("")) {//빈칸인 경우
                    Toast.makeText(UserRegisterActivity.this, "모든 정보를 입력해주세요", Toast.LENGTH_SHORT).show();
                } else if (email.contains(" ") | password.contains(" ") | password_again.contains(" ")) {//공백이 추가된 경우
                    Toast.makeText(UserRegisterActivity.this, "공백은 지원하지 않습니다", Toast.LENGTH_SHORT).show();
                } else if (password.length() < MINIMUN_PWD_SIZE) {//비밀번호 글자수 제한
                    tv_chk_pwd.setText("비밀번호는 6자 이상 필수");
                } else if (!password.equals(password_again)) {//서로 일치하지 않는 비밀번호
                    tv_chk_pwd_again.setText("비밀번호 불일치");
                } else {//모든 조건 충족시
                    createAccount(email, password);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            reload();
        }
    }

    //이메일 계정 생성 및 db 저장
    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");

                            //성별 정보 가져오기
                            int radioButtonId = radioGroup.getCheckedRadioButtonId();
                            RadioButton radioButton = findViewById(radioButtonId);

                            //유저 객체 생성, db저장
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            UserAccount userAccount =
                                    new UserAccount(firebaseUser.getUid(),
                                            firebaseUser.getEmail(),
                                            password,
                                            radioButton.getText().toString(),
                                            birthDate,
                                            editHeight.getText().toString(),
                                            editWeight.getText().toString(),
                                            editPast.getText().toString(),
                                            editSocial.getText().toString(),
                                            editFamily.getText().toString());

                            //setValue: db insert
                            mDatabaseReference.child("UserAccount").child(firebaseUser.getUid()).setValue(userAccount);
                            //updateUI(user);

                            Toast.makeText(UserRegisterActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();

                            //회원가입 성공 시 직전 화면(UserSegmentActivity) 종료
                            UserSegmentActivity userSegmentActivity = (UserSegmentActivity) UserSegmentActivity.userSegmentActivity;
                            userSegmentActivity.finish();
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(UserRegisterActivity.this, "회원가입 실패",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    }

    //이메일 인증인거같은데 사용할지 말지 고민
    private void sendEmailVerification() {
        // Send verification email
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Email sent
                    }
                });
    }

    private void reload() {
    }

    private void updateUI(FirebaseUser user) {

    }
}
