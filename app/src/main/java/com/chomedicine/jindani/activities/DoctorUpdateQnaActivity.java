package com.chomedicine.jindani.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chomedicine.jindani.adapter.AdapterUpdateAnswerList;
import com.chomedicine.jindani.models.AnswerModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.chomedicine.jindani.R;

public class DoctorUpdateQnaActivity extends AppCompatActivity {

    private RecyclerView updateList;
    private AdapterUpdateAnswerList adapterUpdateAnswerList;

    private DatabaseReference databaseReference; // 실시간 데이터베이스


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_update_qna);

        databaseReference = FirebaseDatabase.getInstance().getReference("JindaniApp");

        //리사이클러뷰
        updateList = findViewById(R.id.doc_updateList);
        updateList.setLayoutManager(new LinearLayoutManager(DoctorUpdateQnaActivity.this));
        adapterUpdateAnswerList = new AdapterUpdateAnswerList(updateList);
        updateList.setAdapter(adapterUpdateAnswerList);

        //이전 액티비티에서 의사 아이디 받아오기
        Intent intent = getIntent();
        String docId = intent.getStringExtra("docId");

        readFirebase(docId);

//        //답변 클릭
//        adapterUpdateAnswerList.setOnItemClickListener(new OnAnswerItemClickListener() {
//            @Override
//            public void onItemClick(AdapterUpdateAnswerList.ViewHolder holder, View view, int position) {
//                AnswerModel item = adapterUpdateAnswerList.getAnswerItem(position);
//
//                Intent intent = new Intent(DoctorUpdateQnaActivity.this, DoctorUpdateQnaDetailActivity.class);
//                intent.putExtra("q_id", item.getQuestionId());
//
//                startActivity(intent);
//
//            }
//        });

    }

    //firebase에서 데이터 읽어와서 리사이클러뷰에 추가
    private void readFirebase(String docId) {
        databaseReference.child("Answer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adapterUpdateAnswerList.list.clear(); //매번 모든 데이터를 가져오므로 리스트를 비워주기
                for (DataSnapshot ds : snapshot.getChildren()) {
                    AnswerModel ans = ds.getValue(AnswerModel.class);
                    if (ans.getDocId().equals(docId)) { //의사 아이디에 해당하는 답변 가져오기
                        adapterUpdateAnswerList.addAnsToList(ans);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "답변 데이터 읽어오기 실패");
            }
        });
    }
}
