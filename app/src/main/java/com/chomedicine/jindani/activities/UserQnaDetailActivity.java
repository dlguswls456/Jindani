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

import com.chomedicine.jindani.R;

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

        //???????????????????????? ??????????????? ????????? ????????????
        readFirebase(qId);

        //??????????????????
        ansList = findViewById(R.id.user_ansList);
        ansList.setLayoutManager(new LinearLayoutManager(UserQnaDetailActivity.this));
        adapterAnswerList = new AdapterAnswerList(ansList, UserQnaDetailActivity.this);
        ansList.setAdapter(adapterAnswerList);

    }

    private void getQuestionModel(String qId) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("JindaniApp").child("Question").child(qId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { //????????? ?????? snapshot?????? ?????????
                questionModel = snapshot.getValue(QuestionModel.class);
                setQuestionData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("firebase", "?????? ????????? ???????????? ??????");
            }
        });

    }

    View.OnClickListener buttonClickListener = view -> {
        switch (view.getId()) {
            case R.id.btn_edit: { //?????? ??????
                Intent intent = new Intent(UserQnaDetailActivity.this, WriteQuestionActivity.class);
                intent.putExtra("questionModel", questionModel);
                startActivity(intent);
                break;
            }
            case R.id.btn_delete: { //?????? ??????
                showDeleteDialog();
                break;
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        getQuestionModel(qId);

        //?????? ?????????
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //????????? ????????? ?????? ???????????????
        if(!firebaseUser.getUid().equals(userId)){
            btn_edit.setVisibility(View.GONE);
            btn_delete.setVisibility(View.GONE);
        }
    }

    //?????? ??????, ??????, ?????? ???????????? ?????????
    private void setQuestionData(){
        detail_title.setText(questionModel.getQuestion_title());
        detail_content.setText(questionModel.getQuestion_content());
        detail_date.setText(questionModel.getQuestion_date());
    }

    //?????? ?????? ?????????
    public void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserQnaDetailActivity.this);
        builder.setMessage("????????? ?????????????????????????");
        builder.setCancelable(false);
        builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //?????? ?????? ??????
                databaseReference = FirebaseDatabase.getInstance().getReference().child("JindaniApp").child("Question").child(qId);
                databaseReference.removeValue();

                //????????? ?????? ?????? ??????
                DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child("JindaniApp").child("Answer");
                databaseReference2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) { //????????? ?????? snapshot?????? ?????????
                        for(DataSnapshot ds : snapshot.getChildren()){
                            AnswerModel ans = ds.getValue(AnswerModel.class);
                            if(ans.getQuestionId().equals(qId)) { //?????? ????????? ???????????? ?????? ????????????
                                databaseReference2.child(ans.getAnswerId()).removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("firebase", "?????? ????????? ???????????? ??????");
                    }
                });
                finish();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    //firebase?????? ????????? ????????????, ????????? ????????? ?????????????????? ?????????
    private void readFirebase(String qid){
        databaseReference = FirebaseDatabase.getInstance().getReference().child("JindaniApp").child("Answer");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { //????????? ?????? snapshot?????? ?????????
                adapterAnswerList.list.clear(); //?????? ?????? ???????????? ??????????????? ???????????? ????????????
                for(DataSnapshot ds : snapshot.getChildren()){
                    AnswerModel ans = ds.getValue(AnswerModel.class);
                    if(ans.getQuestionId().equals(qid)) { //?????? ????????? ???????????? ?????? ????????????
                        adapterAnswerList.addAnsToList(ans);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("firebase", "?????? ????????? ???????????? ??????");
            }
        });
    }
}