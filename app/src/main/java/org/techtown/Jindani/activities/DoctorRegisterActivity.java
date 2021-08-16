package org.techtown.Jindani.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import org.techtown.Jindani.R;
import org.techtown.Jindani.models.DoctorAccount;
import org.techtown.Jindani.models.UserAccount;

public class DoctorRegisterActivity extends AppCompatActivity {

    //의사 사용자용 회원가입
    private static final String TAG = "DoctorRegisterActivity";

    private FirebaseAuth auth; //파이어베이스 인증 처리
    private DatabaseReference databaseReference; // 실시간 데이터베이스

    private Button btn_register;
    private EditText editLicenseNumber, editDept, editName, et_email, et_pwd, et_pwd_again;
    private TextView tv_chk_pwd, tv_chk_pwd_again;

    private static final int MINIMUN_PWD_SIZE = 6; //최소 비밀번호 길이

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_register);

        auth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference("JindaniApp");

        et_email = findViewById(R.id.et_email);
        et_pwd = findViewById(R.id.et_pwd);
        et_pwd_again = findViewById(R.id.et_pwd_again);
        tv_chk_pwd = findViewById(R.id.tv_chk_pwd);
        tv_chk_pwd_again = findViewById(R.id.tv_chk_pwd_again);

        editLicenseNumber = findViewById(R.id.editLicenseNumber);
        editDept = findViewById(R.id.editDept);
        editName = findViewById(R.id.editName);

        //회원가입 버튼
        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_email.getText().toString();
                String password = et_pwd.getText().toString();//비번 6글자 이상이어야함
                String password_again = et_pwd_again.getText().toString();//비밀번호 확인용

                //작성한 이메일, 비밀번호 조건 확인 후 계정 생성 시도
                if (conditionCheck(email, password, password_again)) {
                    createAccount(email, password);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        showDialog();
    }

    //가입 조건 체크
    private boolean conditionCheck(String email, String password, String password_again) {
        //필요시 추가 예전
        if (email.equals("") | password.equals("") | password_again.equals("")) {//빈칸인 경우
            Toast.makeText(DoctorRegisterActivity.this, "모든 정보를 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        } else if (email.contains(" ") | password.contains(" ") | password_again.contains(" ")) {//공백이 포함된 경우
            Toast.makeText(DoctorRegisterActivity.this, "공백은 지원하지 않습니다", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if (password.length() < MINIMUN_PWD_SIZE) {//비밀번호 글자수 제한
                tv_chk_pwd.setText("비밀번호는 6자 이상 필수");
                return false;
            } else {
                tv_chk_pwd.setText("");
            }
            if (!password.equals(password_again)) {//서로 일치하지 않는 비밀번호
                tv_chk_pwd_again.setText("비밀번호 불일치");
                return false;
            } else {
                tv_chk_pwd_again.setText("");
            }
        }

        return true;
    }

    //이메일 계정 생성 및 db 저장
    private void createAccount(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");

                            //유저 객체 생성 후, db에 저장
                            addFireBase(email, password);

                            Toast.makeText(DoctorRegisterActivity.this, "회원가입 성공" + "\n" + "권한이 주어질 때까지 로그인이 제한 됩니다", Toast.LENGTH_SHORT).show();

                            //회원가입 성공 시 직전 화면(UserSegmentActivity) 종료
                            UserSegmentActivity userSegmentActivity = (UserSegmentActivity) UserSegmentActivity.userSegmentActivity;
                            userSegmentActivity.finish();
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(DoctorRegisterActivity.this, "회원가입 실패",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    }

    //파이어베이스에 저장
    private void addFireBase(String email, String password) {
        //파이어베이스에 유저 객체 저장
        FirebaseUser firebaseUser = auth.getCurrentUser();
        DoctorAccount doctorAccount =
                new DoctorAccount(firebaseUser.getUid(),
                        firebaseUser.getEmail(),
                        editName.getText().toString(),
                        editLicenseNumber.getText().toString(),
                        editDept.getText().toString());

        //의사db에 추가
        databaseReference.child("DoctorAccount").child(firebaseUser.getUid()).setValue(doctorAccount);

        //해당 아이디가 의사라는 것 추가
        databaseReference.child("UserOrDoctor").child(firebaseUser.getUid()).setValue("doctor");
    }

    //의사 회원정보 작성 취소 대화창
    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DoctorRegisterActivity.this);
        builder.setMessage("작성 중인 내용은 저장되지 않습니다.\n작성을 종료하시겠습니까?");
        builder.setCancelable(false);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
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
}
