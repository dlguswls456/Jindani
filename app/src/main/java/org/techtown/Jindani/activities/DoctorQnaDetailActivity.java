package org.techtown.Jindani.activities;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.techtown.Jindani.R;
import org.techtown.Jindani.adapter.AdapterAnswerList;
import org.techtown.Jindani.models.AnswerModel;
import org.techtown.Jindani.models.QuestionModel;

import java.util.ArrayList;

public class DoctorQnaDetailActivity extends AppCompatActivity {

    RecyclerView ansList;
    private AdapterAnswerList adapterAnswerList;
    private ArrayList<AnswerModel> initList = new ArrayList<>(); //listSize 위해

    TextView detail_title;
    TextView detail_content;
    TextView detail_date;
    String q_id;
    Button write_ans_button;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_qna_detail);

        detail_title = findViewById(R.id.detail_title);
        detail_content = findViewById(R.id.detail_content);
        detail_date = findViewById(R.id.detail_date);

        //리사이클러뷰
        ansList = findViewById(R.id.doc_ansList);
        ansList.setLayoutManager(new LinearLayoutManager(DoctorQnaDetailActivity.this));
        adapterAnswerList = new AdapterAnswerList(ansList);
        ansList.setAdapter(adapterAnswerList);

        Intent intent = getIntent();
        getQuestionData(intent);

        //이전 액티비티에서 q_id 받아오기
        q_id = intent.getStringExtra("q_id");
        //파이어베이스에서 답변리스트 데이터 읽어오기
        readFirebase(q_id);

        //답변작성 버튼 클릭 시 작동
        write_ans_button = findViewById(R.id.write_ans_button);
        write_ans_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int list_size = initList.size();

                Intent intent = new Intent(DoctorQnaDetailActivity.this, WriteAnswerActivity.class);
                intent.putExtra("q_id", q_id);
                intent.putExtra("listSize", list_size);

                startActivity(intent);
            }
        });

    }

    //질문 제목, 내용, 시점 받아와서 띄우기
    private void getQuestionData(Intent intent){

        String t = intent.getStringExtra("q_title");
        String c = intent.getStringExtra("q_content");
        String d = intent.getStringExtra("q_date");

        detail_title.setText(t);
        detail_content.setText(c);
        detail_date.setText(d);
    }

    //firebase에서 데이터 읽어와서 리사이클러뷰에 추가
    private void readFirebase(String qid){
        databaseReference = FirebaseDatabase.getInstance().getReference().child("JindaniApp").child("Answer");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { //변화된 값이 snapshot으로 넘어옴
                initList.clear();
                adapterAnswerList.list.clear(); //매번 모든 데이터를 가져오므로 리스트를 비워주기
                for(DataSnapshot ds : snapshot.getChildren()){
                    AnswerModel ans = ds.getValue(AnswerModel.class);
                    if(ans.getQuestionId().equals(qid)) { //질문 번호에 해당하는 답변 가져오기
                        adapterAnswerList.addAnsToList(ans);
                        initList.add(ans);
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