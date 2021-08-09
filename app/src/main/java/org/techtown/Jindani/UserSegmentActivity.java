package org.techtown.Jindani;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserSegmentActivity extends AppCompatActivity {

    //회원가입 성공 시 현재 화면(UserSegmentActivity) 종료하기 위해 선언
    public static Activity userSegmentActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_segment);

        userSegmentActivity = UserSegmentActivity.this;

        //일반 사용자 회원가입
        Button user_signin = findViewById(R.id.user_signin);
        user_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserSegmentActivity.this, UserRegisterActivity.class);
                startActivity(intent);
            }
        });

        //의사 회원가입
        Button doc_signin = findViewById(R.id.doc_signin);
        doc_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}