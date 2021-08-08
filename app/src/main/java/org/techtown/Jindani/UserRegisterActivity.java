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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UserRegisterActivity extends AppCompatActivity {

    //일반 사용자용 회원가입
    private static final String TAG = "UserRegisterActivity";

    private FirebaseAuth mAuth; //파이어베이스 인증 처리
    private DatabaseReference mDatabaseReference; // 실시간 데이터베이스

    private Button btn_register;
    private RadioGroup radioGroup;
    private EditText editHeight, editWeight, editPast, editSocial, editFamily, et_email, et_pwd, et_pwd_again;
    private TextView txtDate, tv_chk_pwd, tv_chk_pwd_again;

    private DatePicker datePicker;
    int age;
    private String ageCategory;


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
                //생년월일로 Calendar객체 만들기
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date birthDate = null;
                try {
                    //monthOfYear은 0부터 시작하기때문에 1 더해줘야함
                    birthDate = sdf.parse(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
//                System.out.println(sdf.format(birthDate));
                Calendar birthCal = Calendar.getInstance();
                birthCal.setTime(birthDate);

                //나이계산 및 나이 카테고리 계산
                age = getAge(year, monthOfYear, dayOfMonth);
                ageCategory = getAgeCategory(age, birthCal);

                //확인용 text
                String birthStr = year + "년 " + (monthOfYear + 1) + "월 " + dayOfMonth + "일" + " 만 " + age + "세 " + ageCategory;
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
                                            ageCategory,
                                            editHeight.getText().toString(),
                                            editWeight.getText().toString(),
                                            editPast.getText().toString(),
                                            editSocial.getText().toString(),
                                            editFamily.getText().toString());

                            //setValue: db insert
                            mDatabaseReference.child("UserAccount").child(firebaseUser.getUid()).setValue(userAccount);
                            //updateUI(user);

                            Toast.makeText(UserRegisterActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
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

    //만나이 계산기
    public int getAge(int birthYear, int birthMonth, int birthDay) {
        //현재날짜 가져옴
        Calendar current = Calendar.getInstance();
        int currentYear = current.get(Calendar.YEAR);
        int currentMonth = current.get(Calendar.MONTH);
        int currentDay = current.get(Calendar.DAY_OF_MONTH);

        int age = currentYear - birthYear;
        // 생일 안 지난 경우 -1
        if (birthMonth * 100 + birthDay > currentMonth * 100 + currentDay) {
            age--;
        }

        return age;
    }

    //나이 카테고리 구하기
    public String getAgeCategory(int age, Calendar birthCal) {
        //현재 날짜 가져옴
        Calendar current = Calendar.getInstance();

        if (age / 10 >= 9) {//나이가 90대 이상
            return "90s";
        } else if (age / 10 >= 1) {//10대 이상, 90대 미만
            return (age / 10) * 10 + "s";
        } else {//0살~9살
            if (age >= 1 & age <= 6) {//1세~6세 유아
                return "유아";
            } else if (age >= 7) {//7세~9세
                return "0s";
            } else {//0세
                current.add(Calendar.MONTH, -1);//현 날짜보다 1달 전으로 설정
                if (birthCal.after(current)) {//생후 1달 미만 신생아
                    return "신생아";
                } else {//생후 1개월~1년 미만 영아
                    return "영아";
                }
            }
        }
    }
}
