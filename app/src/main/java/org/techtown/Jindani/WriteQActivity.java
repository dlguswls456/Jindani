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

public class WriteQActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_q);

        Button button = findViewById(R.id.write_q_button); //완료 버튼
        button.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        showDialog();
    }

    @Override
    public void onClick(View v) {

        hideKeyboard();

        EditText q_title = findViewById(R.id.q_title); //질문 제목
        EditText q_content = findViewById(R.id.q_content); //질문 내용

        q_content.setOnTouchListener(new View.OnTouchListener() { //nestedscrollview 안에서 edittext 스크롤 가능하게
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.q_content) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
            }
        });

        String t = q_title.getText().toString();
        String c = q_content.getText().toString();

        //입력 안됐을 때
        if (t.equals("")) {
            Toast.makeText(getApplicationContext(), "제목을 입력해주세요!", Toast.LENGTH_SHORT).show();
            return; }
        if (c.equals("")) {
            Toast.makeText(getApplicationContext(), "내용을 입력해주세요!", Toast.LENGTH_SHORT).show();
            return; }

        //제목과 내용을 질문 리스트 액티비티로 전달
        Intent intent = new Intent();
        intent.putExtra("제목", t);
        intent.putExtra("내용", c);

        setResult(Activity.RESULT_OK, intent);
        Toast.makeText(getApplicationContext(), "질문 완료", Toast.LENGTH_SHORT).show();

        finish();

    }

    public void hideKeyboard() { //키보드 숨기기
        InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void showDialog() { //질문 작성 취소 대화창
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
}
