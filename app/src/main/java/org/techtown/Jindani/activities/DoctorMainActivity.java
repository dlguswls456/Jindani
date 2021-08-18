package org.techtown.Jindani.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class DoctorMainActivity extends AppCompatActivity {

    private Button qna_button, btn_logout;

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference databaseReference; // 실시간 데이터베이스

    private final static String TAG = "DoctorMainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main);

        //버튼 레이아웃 변경
        qna_button = findViewById(R.id.qna_button);
        btn_logout = findViewById(R.id.btn_logout);
        setLayout();

        //각 버튼 클릭
        qna_button.setOnClickListener(buttonClickListener);
        btn_logout.setOnClickListener(buttonClickListener);

        databaseReference = FirebaseDatabase.getInstance().getReference("JindaniApp");

    }

    //버튼 레이아웃 변경
    private void setLayout() {
        //버튼 레이아웃 변경
        String qna = qna_button.getText().toString();
        SpannableStringBuilder q_spannable = new SpannableStringBuilder(qna);

        String c_word = "질환 예측";
        String q_word = "의사에게 질문";
        int q_start = qna.indexOf(q_word);
        int q_end = q_start + q_word.length();

//        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        q_spannable.setSpan(new StyleSpan(Typeface.BOLD), q_start, q_end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        q_spannable.setSpan(new RelativeSizeSpan(1.3f), q_start, q_end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //상대적 크기

        qna_button.setText(q_spannable);
    }

    View.OnClickListener buttonClickListener = view -> {
        switch (view.getId()) {
            case R.id.qna_button: { //qna 버튼
                gotoQnaActivity();
                break;
            }
            case R.id.btn_logout: { //로그아웃 버튼
                signOutAndFinish();
                break;
            }
        }
    };

    private void gotoQnaActivity() {
        Intent intent = new Intent(getApplicationContext(), DoctorQnaListActivity.class);
        startActivity(intent);
    }

    private void signOutAndFinish() {
        signOut();
        Intent intent = new Intent(DoctorMainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    //로그아웃
    private void signOut() {
        // Firebase sign out
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();

    }
}