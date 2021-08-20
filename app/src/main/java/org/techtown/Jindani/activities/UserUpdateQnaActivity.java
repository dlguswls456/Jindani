package org.techtown.Jindani.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.techtown.Jindani.R;
import org.techtown.Jindani.adapter.AdapterQnaList;
import org.techtown.Jindani.listeners.OnQuestionItemClickListener;
import org.techtown.Jindani.models.QuestionModel;

public class UserUpdateQnaActivity extends AppCompatActivity {

    private RecyclerView updateList;
    private AdapterQnaList adapterQnaList;

    private DatabaseReference databaseReference; // 실시간 데이터베이스

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_update_qna);

        databaseReference = FirebaseDatabase.getInstance().getReference("JindaniApp");

        //리사이클러뷰
        updateList = findViewById(R.id.user_updateList);
        updateList.setLayoutManager(new LinearLayoutManager(UserUpdateQnaActivity.this));
        adapterQnaList = new AdapterQnaList(updateList);
        updateList.setAdapter(adapterQnaList);

        //이전 액티비티에서 사용자 아이디 받아오기
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");

        adapterQnaList.setOnItemClickListener(new OnQuestionItemClickListener() {
            @Override
            public void onItemClick(AdapterQnaList.ViewHolder holder, View view, int position) {
                QuestionModel item = adapterQnaList.getQnaItem(position);

                Intent intent = new Intent(UserUpdateQnaActivity.this, UserQnaDetailActivity.class);
                intent.putExtra("qId", item.getQuestionId());
                intent.putExtra("userId", item.getUserId());

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        readFirebase(userId);
    }

    //firebase에서 데이터 읽어와서 리사이클러뷰에 추가
    private void readFirebase(String userId) {
        databaseReference.child("Question").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adapterQnaList.list.clear(); //매번 모든 데이터를 가져오므로 리스트를 비워주기
                for(DataSnapshot ds : snapshot.getChildren()){
                    QuestionModel q = ds.getValue(QuestionModel.class);
                    if(q.getUserId().equals(userId)) { //사용자 아이디에 해당하는 답변 가져오기
                        adapterQnaList.addQToList(q);
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