package com.chomedicine.jindani.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.chomedicine.jindani.R;
import com.chomedicine.jindani.models.UserAccount;

public class PersonInfoActivity extends AppCompatActivity {

    //구글 로그인 사용자용 기본정보 등록
    private static final String TAG = "PersonInfoActivity";

    private FirebaseAuth auth; //파이어베이스 인증 처리
    private DatabaseReference databaseReference; // 실시간 데이터베이스

    private Button btnRegister;
    private RadioGroup radioGroup;
    private EditText editHeight, editWeight, editPast, editSocial, editFamily;
    private TextView txtDate;
    private DatePicker datePicker;
    private String birthDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);

        auth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference("JindaniApp");

        radioGroup = findViewById(R.id.radioGroup);
        editHeight = findViewById(R.id.editHeight);
        editWeight = findViewById(R.id.editWeight);
        txtDate = findViewById(R.id.txtDate);
        editPast = findViewById(R.id.editPast);
        editSocial = findViewById(R.id.editSocial);
        editFamily = findViewById(R.id.editFamily);
        datePicker = findViewById(R.id.datePicker);

        //생년월일 선택
        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                birthDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                //확인용 text
                String birthStr = year + "년 " + (monthOfYear + 1) + "월 " + dayOfMonth + "일";
                txtDate.setText(birthStr);

            }
        });

        //등록 버튼
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //유저 객체 생성 후, db에 저장
                addFireBase();

                Toast.makeText(PersonInfoActivity.this, "등록 성공", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(PersonInfoActivity.this, UserMainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    //파이어베이스에 저장
    private void addFireBase() {
        //성별 정보 가져오기
        int radioButtonId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(radioButtonId);

        //파이어베이스에 유저 객체 저장
        FirebaseUser firebaseUser = auth.getCurrentUser();
        UserAccount userAccount =
                new UserAccount(firebaseUser.getUid(),
                        firebaseUser.getEmail(),
                        radioButton.getText().toString(),
                        birthDate,
                        editHeight.getText().toString(),
                        editWeight.getText().toString(),
                        editPast.getText().toString(),
                        editSocial.getText().toString(),
                        editFamily.getText().toString());

        //사용자db에 추가
        databaseReference.child("UserAccount").child(firebaseUser.getUid()).setValue(userAccount);
        //해당 아이디가 일반 사용자라는 것 추가
        databaseReference.child("UserOrDoctor").child(firebaseUser.getUid()).setValue("user");
    }

}
