package com.chomedicine.jindani.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chomedicine.jindani.adapter.AdapterAnswerList;
import com.chomedicine.jindani.models.AnswerModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.chomedicine.jindani.R;

import com.chomedicine.jindani.models.QuestionModel;

public class UserQnaDetailActivity extends AppCompatActivity {

    RecyclerView ansList;
    private AdapterAnswerList adapterAnswerList;

    TextView detail_title;
    TextView detail_content;
    TextView detail_date;

    Button btn_edit, btn_delete;

    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;

    private QuestionModel questionModel;
    private String qId, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_qna_detail);

        detail_title = findViewById(R.id.detail_title);
        detail_content = findViewById(R.id.detail_content);
        detail_date = findViewById(R.id.detail_date);

        btn_edit = findViewById(R.id.btn_edit);
        btn_delete = findViewById(R.id.btn_delete);

        btn_edit.setOnClickListener(buttonClickListener);
        btn_delete.setOnClickListener(buttonClickListener);

        Intent befo_intent = getIntent();
        qId = befo_intent.getStringExtra("qId");
        userId = befo_intent.getStringExtra("userId");

        //파이어베이스에서 답변리스트 데이터 읽어오기
        readFirebase(qId);

        //리사이클러뷰
        ansList = findViewById(R.id.user_ansList);
        ansList.setLayoutManager(new LinearLayoutManager(UserQnaDetailActivity.this));
        adapterAnswerList = new AdapterAnswerList(ansList);
        ansList.setAdapter(adapterAnswerList);

    }

    private void getQuestionModel(String qId) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("JindaniApp").child("Question").child(qId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { //변화된 값이 snapshot으로 넘어옴
                questionModel = snapshot.getValue(QuestionModel.class);
                setQuestionData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("firebase", "답변 데이터 읽어오기 실패");
            }
        });

    }

    View.OnClickListener buttonClickListener = view -> {
        switch (view.getId()) {
            case R.id.btn_edit: { //수정 버튼
                Intent intent = new Intent(UserQnaDetailActivity.this, WriteQuestionActivity.class);
                intent.putExtra("questionModel", questionModel);
                startActivity(intent);
                break;
            }
            case R.id.btn_delete: { //삭제 버튼
                showDeleteDialog();
                break;
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        getQuestionModel(qId);

        //지금 사용자
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //작성자 아니면 버튼 안보여주기
        if(!firebaseUser.getUid().equals(userId)){
            btn_edit.setVisibility(View.GONE);
            btn_delete.setVisibility(View.GONE);
        }
    }

    //질문 제목, 내용, 시점 받아와서 띄우기
    private void setQuestionData(){
        detail_title.setText(questionModel.getQuestion_title());
        detail_content.setText(questionModel.getQuestion_content());
        detail_date.setText(questionModel.getQuestion_date());
    }

    //질문 삭제 대화창
    public void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserQnaDetailActivity.this);
        builder.setMessage("질문을 삭제하시겠습니까?");
        builder.setCancelable(false);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //해당 질문 삭제
                databaseReference = FirebaseDatabase.getInstance().getReference().child("JindaniApp").child("Question").child(qId);
                databaseReference.removeValue();

                //질문에 달린 답변 삭제
                DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child("JindaniApp").child("Answer");
                databaseReference2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) { //변화된 값이 snapshot으로 넘어옴
                        for(DataSnapshot ds : snapshot.getChildren()){
                            AnswerModel ans = ds.getValue(AnswerModel.class);
                            if(ans.getQuestionId().equals(qId)) { //질문 번호에 해당하는 답변 가져오기
                                databaseReference2.child(ans.getAnswerId()).removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("firebase", "답변 데이터 읽어오기 실패");
                    }
                });
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

    //firebase에서 데이터 읽어오기, 변화가 있으면 클라이언트에 알려줌
    private void readFirebase(String qid){
        databaseReference = FirebaseDatabase.getInstance().getReference().child("JindaniApp").child("Answer");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { //변화된 값이 snapshot으로 넘어옴
                adapterAnswerList.list.clear(); //매번 모든 데이터를 가져오므로 리스트를 비워주기
                for(DataSnapshot ds : snapshot.getChildren()){
                    AnswerModel ans = ds.getValue(AnswerModel.class);
                    if(ans.getQuestionId().equals(qid)) { //질문 번호에 해당하는 답변 가져오기
                        adapterAnswerList.addAnsToList(ans);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("firebase", "답변 데이터 읽어오기 실패");
            }
        });
    }
}