package com.chomedicine.jindani.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chomedicine.jindani.adapter.AdapterQnaList;
import com.chomedicine.jindani.adapter.AdapterUpdateAnswerList;
import com.chomedicine.jindani.listeners.OnQuestionItemClickListener;
import com.chomedicine.jindani.models.AnswerModel;
import com.chomedicine.jindani.models.QuestionModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.chomedicine.jindani.R;

import java.util.ArrayList;

public class DoctorUpdateQnaActivity extends AppCompatActivity {

    private RecyclerView updateList;
    private AdapterQnaList adapterQnaList;

    private ArrayList<AnswerModel> list = new ArrayList<>();
    private ArrayList<QuestionModel> copyList = new ArrayList<>();

    private DatabaseReference databaseReference; // 실시간 데이터베이스


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_update_qna);

        databaseReference = FirebaseDatabase.getInstance().getReference("JindaniApp");

        //리사이클러뷰
        updateList = findViewById(R.id.doc_updateList);
        updateList.setLayoutManager(new LinearLayoutManager(DoctorUpdateQnaActivity.this));
        adapterQnaList = new AdapterQnaList(updateList);
        updateList.setAdapter(adapterQnaList);

        //이전 액티비티에서 의사 아이디 받아오기
        Intent befo_intent = getIntent();
        String docId = befo_intent.getStringExtra("docId");

        readAnswer(docId);

        //질문 클릭
        adapterQnaList.setOnItemClickListener(new OnQuestionItemClickListener() {
            @Override
            public void onItemClick(AdapterQnaList.ViewHolder holder, View view, int position) {
                QuestionModel item = adapterQnaList.getQnaItem(position);

                Intent intent = new Intent(DoctorUpdateQnaActivity.this, DoctorQnaDetailActivity.class);
                intent.putExtra("questionModel", item);
                intent.putExtra("from_update", true); //답변 관리 페이지에서 넘어감

                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        readQuestion();
    }

    //firebase에서 해당 의사 답변 읽어와서 리스트에 추가
    private void readAnswer(String docId) {
        databaseReference.child("Answer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear(); //매번 모든 데이터를 가져오므로 리스트를 비워주기
                for (DataSnapshot ds : snapshot.getChildren()) {
                    AnswerModel ans = ds.getValue(AnswerModel.class);
                    if (ans.getDocId().equals(docId)) { //의사 아이디에 해당하는 답변 가져오기
                        list.add(ans);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "답변 데이터 읽어오기 실패");
            }
        });
    }

    //firebase에서 리스트와 비교하며 답변이 달린 질문 읽어오기
    private void readQuestion() {
        databaseReference.child("Question").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adapterQnaList.list.clear();
                adapterQnaList.check(); //리스트 데이터 변경 체크
                for (AnswerModel ans : list) { //질문 아이디에 해당하는 질문 가져오기
                    int i = 0;
                    QuestionModel Q = snapshot.child(ans.getQuestionId()).getValue(QuestionModel.class);
                    for (QuestionModel q : adapterQnaList.list) { //중복값 넣지 않음
                        if (Q.getQuestionId().equals(q.getQuestionId())) {
                            i++;
                        }
                    }
                    if (i == 0) {
                        adapterQnaList.addQToList(Q);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "질문 데이터 읽어오기 실패");
            }
        });
    }
}
