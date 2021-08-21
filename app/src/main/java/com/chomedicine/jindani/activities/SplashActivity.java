package com.chomedicine.jindani.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import org.chomedicine.jindani.R;

//사진 추가 및 첫 화면 등록 필요
public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 3000;

//    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        imageView = findViewById(R.id.imgSplash);

        Handler hand = new Handler();
        hand.postDelayed(new Runnable() {//로고 화면 3초 보여주기
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN);
    }
}
