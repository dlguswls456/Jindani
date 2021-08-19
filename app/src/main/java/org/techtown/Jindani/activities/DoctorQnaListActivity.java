package org.techtown.Jindani.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.techtown.Jindani.R;
import org.techtown.Jindani.adapter.AdapterQnaList;
import org.techtown.Jindani.adapter.AdapterUpdateAnswerList;
import org.techtown.Jindani.listeners.OnQuestionItemClickListener;
import org.techtown.Jindani.models.QuestionModel;

import java.util.ArrayList;

public class DoctorQnaListActivity extends AppCompatActivity{

    RecyclerView qnalist;
    private AdapterQnaList adapterQnaList;
    private ArrayList<QuestionModel> initList = new ArrayList<>();

    private EditText search_question;
    private DatabaseReference databaseReference;

    static final int REQUSET_CODE = 1;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_qna_list);

        readFirebase();

        //리사이클러뷰
        qnalist = findViewById(R.id.qnalist);
        qnalist.setLayoutManager(new LinearLayoutManager(DoctorQnaListActivity.this));
        adapterQnaList = new AdapterQnaList(qnalist);
        qnalist.setAdapter(adapterQnaList);

        //질문 클릭
        adapterQnaList.setOnItemClickListener(new OnQuestionItemClickListener() {
            @Override
            public void onItemClick(AdapterQnaList.ViewHolder holder, View view, int position) {
                QuestionModel item = adapterQnaList.getQnaItem(position);

                Intent intent = new Intent(DoctorQnaListActivity.this, DoctorQnaDetailActivity.class);
                intent.putExtra("q_id", item.getQuestionId());
                intent.putExtra("q_title", item.getQuestion_title());
                intent.putExtra("q_content", item.getQuestion_content());
                intent.putExtra("q_date", item.getQuestion_date());

                startActivityForResult(intent, REQUSET_CODE);
            }
        });

        //질문 검색
        search_question = findViewById(R.id.search_question);
        search_question.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("SEARCH", "beforeTextChanged");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapterQnaList.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("SEARCH", "afterTextChanged");
//
            }
        });
    }

    //firebase에서 데이터 읽어오기, 변화가 있으면 클라이언트에 알려줌
    private void readFirebase(){
        databaseReference = FirebaseDatabase.getInstance().getReference().child("JindaniApp").child("Question");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { //변화된 값이 snapshot으로 넘어옴
                initList.clear();
                adapterQnaList.list.clear(); //매번 모든 데이터를 가져오므로 리스트를 비워주기
                adapterQnaList.copyList.clear();
                for(DataSnapshot ds : snapshot.getChildren()){ //Question아래에 있는 데이터 모두 가져오기
                    QuestionModel q = ds.getValue(QuestionModel.class);
                    adapterQnaList.addQToList(q);
                    initList.add(q);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("firebase", "질문 데이터 읽어오기 실패");
            }
        });
    }

}
