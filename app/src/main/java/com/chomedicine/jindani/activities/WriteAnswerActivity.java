package com.chomedicine.jindani.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.chomedicine.jindani.models.AnswerModel;
import com.chomedicine.jindani.models.DoctorAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.chomedicine.jindani.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WriteAnswerActivity extends AppCompatActivity implements View.OnClickListener {

    // 파이어베이스 데이터베이스 연동
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();

    EditText ans_content; //답변 내용
    Button write_ans_button; //등록 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_answer);

        write_ans_button = findViewById(R.id.write_ans_button);
        write_ans_button.setOnClickListener(WriteAnswerActivity.this);
    }

    //등록 버튼 눌렀을 때 동작
    @Override
    public void onClick(View v) {

        hideKeyboard();

        ans_content = findViewById(R.id.ans_content);

        //edittext에서 문자열 받아오기
        String ans = ans_content.getText().toString();

        //입력 안됐을 때
        if (ans.equals("")) {
            Toast.makeText(WriteAnswerActivity.this, "답변을 입력해주세요!", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(WriteAnswerActivity.this, "답변 완료", Toast.LENGTH_SHORT).show();


        Intent intent = getIntent();

        //질문 id
        String qId = intent.getStringExtra("q_id");

        //답변 id 설정
        int list_size = intent.getIntExtra("listSize", 0);
        String rand = String.valueOf((int)(Math.random()*100));
        String aId = qId + "_A" + (list_size + 1) + "_" + rand;

        //db에서 의사이름 받아오기
        databaseReference.child("JindaniApp").child("DoctorAccount").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name; //의사 이름
                String dept; //의사 진료과

                DoctorAccount doc = snapshot.getValue(DoctorAccount.class);
                name = doc.getName();
                dept = doc.getDept();
                storeAnswer(firebaseUser.getUid(), name, dept, aId, ans, qId); //listener에서 나오면 전역변수에도 저장x
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("firebase", "의사 이름 데이터 읽기 실패");
            }
        });

        finish();

    }

    //키보드 숨기기
    public void hideKeyboard() {
        InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    //값을 파이어베이스 Realtime database에 저장
    public void storeAnswer(String docId, String docName, String docDept, String aId, String content, String qId) {
        String date = getTime();
        AnswerModel ans = new AnswerModel(docId, docName, docDept, aId, content, date, qId);

        //child는 해당 키 위치로 이동
        databaseReference.child("JindaniApp").child("Answer").child(aId).setValue(ans);
    }

    //현재 시점을 특정 형식으로 리턴
    private String getTime() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd a hh:mm"); //오전 오후 구분
        String getTime = dateFormat.format(date);
        return getTime;
    }
}