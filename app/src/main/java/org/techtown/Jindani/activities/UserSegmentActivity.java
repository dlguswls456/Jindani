package org.techtown.Jindani.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import org.techtown.Jindani.R;

public class UserSegmentActivity extends AppCompatActivity {

    //회원가입 성공 시 현재 화면(UserSegmentActivity) 종료하기 위해 선언
    public static Activity userSegmentActivity;

    LinearLayout user_signup;
    LinearLayout doc_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_segment);

        //회원가입 성공 시 현재 화면(UserSegmentActivity) 종료하기 위해 초기화
        userSegmentActivity = UserSegmentActivity.this;

        //일반 사용자 회원가입
        user_signup = findViewById(R.id.user_signup);
        user_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserSegmentActivity.this, UserRegisterActivity.class);
                startActivity(intent);
            }
        });

        //의사 회원가입
        doc_signup = findViewById(R.id.doc_signup);
        doc_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}