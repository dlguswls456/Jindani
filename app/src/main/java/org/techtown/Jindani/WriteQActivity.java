package org.techtown.Jindani;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WriteQActivity extends AppCompatActivity implements View.OnClickListener {

    // 파이어베이스 데이터베이스 연동
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference(); //DatabaseReference는 데이터베이스의 특정 위치로 연결

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_q);

        Button button = findViewById(R.id.write_q_button); //완료 버튼
        button.setOnClickListener(WriteQActivity.this);
    }

    @Override
    public void onBackPressed() { //뒤로가기 버튼 눌렀을 때 동작
        showDialog();
    }

    @Override
    public void onClick(View v) { //완료 버튼 눌렀을 때 동작

        hideKeyboard();

        EditText q_title = findViewById(R.id.q_title); //질문 제목
        EditText q_content = findViewById(R.id.q_content); //질문 내용

        //edittext에서 문자열 받아오기
        String t = q_title.getText().toString();
        String c = q_content.getText().toString();

        //입력 안됐을 때
        if (t.equals("")) {
            Toast.makeText(WriteQActivity.this, "제목을 입력해주세요!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (c.equals("")) {
            Toast.makeText(WriteQActivity.this, "내용을 입력해주세요!", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(WriteQActivity.this, "질문 완료", Toast.LENGTH_SHORT).show();

        //변경 필요 - 사용자 id 받아오기, 질문 어떻게 넣을지
        storeQ("n번사용자", "4번질문", t, c);

        finish();

    }

    public void hideKeyboard() { //키보드 숨기기
        InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void showDialog() { //질문 작성 취소 대화창
        AlertDialog.Builder builder = new AlertDialog.Builder(WriteQActivity.this);
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

    public void storeQ(String userid, String qid, String title, String content) { //값을 파이어베이스 Realtime database에 저장
        String date = getTime();
        QnaModel q = new QnaModel(userid, qid, title, content, date);

        //child는 해당 키 위치로 이동
        databaseReference.child("JindaniApp").child("Question").child(qid).setValue(q);
    }

    private String getTime() { //현재 시점을 특정 형식으로 리턴
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd a hh:mm"); //오전 오후 구분
        String getTime = dateFormat.format(date);
        return getTime;
    }
}
