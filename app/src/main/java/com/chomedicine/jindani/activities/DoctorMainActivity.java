package com.chomedicine.jindani.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.chomedicine.jindani.R;

public class DoctorMainActivity extends AppCompatActivity {

    private Button qna_button, mypage_button;

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference databaseReference; // 실시간 데이터베이스

    private final static String TAG = "DoctorMainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main);

        //버튼 레이아웃 변경
        qna_button = findViewById(R.id.qna_button);
        mypage_button = findViewById(R.id.mypage_button);
        setLayout();

        //각 버튼 클릭
        qna_button.setOnClickListener(buttonClickListener);
        mypage_button.setOnClickListener(buttonClickListener);

        databaseReference = FirebaseDatabase.getInstance().getReference("JindaniApp");

    }

    //버튼 레이아웃 변경
    private void setLayout() {
        //버튼 레이아웃 변경
        String qna = qna_button.getText().toString();
        String mypage = mypage_button.getText().toString();

        SpannableStringBuilder q_spannable = new SpannableStringBuilder(qna);
        SpannableStringBuilder m_spannable = new SpannableStringBuilder(mypage);

        String q_word = "환자에게 답변";
        String m_word = "마이페이지";
        int q_start = qna.indexOf(q_word);
        int q_end = q_start + q_word.length();
        int m_start = mypage.indexOf(m_word);
        int m_end = m_start + m_word.length();

//        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        q_spannable.setSpan(new StyleSpan(Typeface.BOLD), q_start, q_end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        q_spannable.setSpan(new RelativeSizeSpan(1.3f), q_start, q_end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //상대적 크기

        m_spannable.setSpan(new StyleSpan(Typeface.BOLD), m_start, m_end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        m_spannable.setSpan(new RelativeSizeSpan(1.3f), m_start, m_end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //상대적 크기

        qna_button.setText(q_spannable);
        mypage_button.setText(m_spannable);
    }

    View.OnClickListener buttonClickListener = view -> {
        switch (view.getId()) {
            case R.id.qna_button: { //qna 버튼
                gotoQnaActivity();
                break;
            }
            case R.id.mypage_button: { //로그아웃 버튼
                gotoMyPageActivity();
                break;
            }
        }
    };

    private void gotoQnaActivity() {
        Intent intent = new Intent(getApplicationContext(), DoctorQnaListActivity.class);
        startActivity(intent);
    }

    private void gotoMyPageActivity() {
        Intent intent = new Intent(DoctorMainActivity.this, DoctorMypageActivity.class);
        startActivity(intent);
    }

}