package org.techtown.Jindani;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class QnaListActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView qnalist;
    private AdapterQnaList adapterQnaList;

    static final int REQUSET_CODE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qna_list);

        //리사이클러뷰
        qnalist = findViewById(R.id.qnalist);
        qnalist.setLayoutManager(new LinearLayoutManager(QnaListActivity.this));
        adapterQnaList = new AdapterQnaList(qnalist);
        qnalist.setAdapter(adapterQnaList);

        Button button = findViewById(R.id.write_q_button); //질문 작성 버튼
        button.setOnClickListener(QnaListActivity.this);
    }

    @Override
    public void onClick(View v) {
        EditText e = findViewById(R.id.search_q);
        Intent intent = new Intent(QnaListActivity.this, WriteQActivity.class);
        startActivityForResult(intent, REQUSET_CODE);
        e.getText().clear();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //요청 코드와 응답 코드가 모두 정상이면
        if (requestCode == REQUSET_CODE) {
            if (resultCode == RESULT_OK) {

                //WriteQActivity에서 값 받아오기
                String t = data.getStringExtra("제목");
                String c = data.getStringExtra("내용");

                //질문 목록에 추가
                adapterQnaList.addQToList(new QnaModel(t, c, "", false));
            }
        }
    }

}
