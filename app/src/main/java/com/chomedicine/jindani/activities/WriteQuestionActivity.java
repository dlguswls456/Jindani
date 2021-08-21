package com.chomedicine.jindani.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.chomedicine.jindani.models.QuestionModel;
import org.chomedicine.jindani.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WriteQuestionActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText question_title, question_content;

    // 파이어베이스 데이터베이스 연동
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference(); //DatabaseReference는 데이터베이스의 특정 위치로 연결

    private QuestionModel questionModel;

    private int list_size;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_q);

        Button button = findViewById(R.id.write_q_button); //등록 버튼
        button.setOnClickListener(WriteQuestionActivity.this);

        question_title = findViewById(R.id.q_title); //질문 제목
        question_content = findViewById(R.id.q_content); //질문 내용

        Intent intent = getIntent();
        questionModel = (QuestionModel) intent.getSerializableExtra("questionModel");
        list_size = intent.getIntExtra("listSize", -1);

        //질문 수정일 때
        if(questionModel != null){
            question_title.setText(questionModel.getQuestion_title());
            question_content.setText(questionModel.getQuestion_content());
        }

    }

    //뒤로가기 버튼 눌렀을 때 동작
    @Override
    public void onBackPressed() {
        showDialog();
    }

    //등록 버튼 눌렀을 때 동작
    @Override
    public void onClick(View v) {
        String qId;

        hideKeyboard();

        //edittext에서 문자열 받아오기
        String title = question_title.getText().toString();
        String content = question_content.getText().toString();

        //입력 안됐을 때
        if (title.equals("")) {
            Toast.makeText(WriteQuestionActivity.this, "제목을 입력해주세요!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (content.equals("")) {
            Toast.makeText(WriteQuestionActivity.this, "내용을 입력해주세요!", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(WriteQuestionActivity.this, "질문 완료", Toast.LENGTH_SHORT).show();

        //질문 id 설정
        if(list_size != -1){//질문 작성
            String rand = String.valueOf((int)(Math.random()*100));
            qId = "Q" + (list_size + 1) + "_" + rand ;
        }else{//질문 수정
            qId = questionModel.getQuestionId();
        }
        storeQuestion(firebaseUser.getUid(), qId, title, content);

        finish();
    }

    //키보드 숨기기
    public void hideKeyboard() {
        InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    //질문 작성 취소 대화창
    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(WriteQuestionActivity.this);
        builder.setMessage("작성 중인 내용은 저장되지 않습니다.\n질문 작성을 종료하시겠습니까?");
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

    //값을 파이어베이스 Realtime database에 저장
    public void storeQuestion(String userId, String qId, String title, String content) {
        String date = getTime();
        QuestionModel q = new QuestionModel(userId, qId, title, content, date);

        //child는 해당 키 위치로 이동
        databaseReference.child("JindaniApp").child("Question").child(qId).setValue(q);
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
