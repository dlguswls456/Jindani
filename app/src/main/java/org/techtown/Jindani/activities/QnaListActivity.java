package org.techtown.Jindani.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import org.techtown.Jindani.adapter.AdapterQnaList;
import org.techtown.Jindani.listeners.OnQuestionItemClickListener;
import org.techtown.Jindani.models.QuestionModel;
import org.techtown.Jindani.R;

import java.util.ArrayList;

public class QnaListActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView qnalist;
    private AdapterQnaList adapterQnaList;
    private ArrayList<QuestionModel> initList = new ArrayList<>(); //listSize 위해

    private EditText search_question;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qna_list);

        //리사이클러뷰
        qnalist = findViewById(R.id.qnalist);
        qnalist.setLayoutManager(new LinearLayoutManager(QnaListActivity.this));
        adapterQnaList = new AdapterQnaList(qnalist);
        qnalist.setAdapter(adapterQnaList);

        adapterQnaList.setOnItemClickListener(new OnQuestionItemClickListener() {
            @Override
            public void onItemClick(AdapterQnaList.ViewHolder holder, View view, int position) {
                QuestionModel item = adapterQnaList.getQnaItem(position);

                Intent intent = new Intent(QnaListActivity.this, UserQnaDetailActivity.class);
                intent.putExtra("qId", item.getQuestionId());
                intent.putExtra("userId", item.getUserId());

                startActivity(intent);

            }
        });

        Button button = findViewById(R.id.write_q_button); //질문 작성 버튼
        button.setOnClickListener(QnaListActivity.this);

        //질문 검색
        search_question = findViewById(R.id.search_question);
        search_question.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("TAG", "beforeTextChanged");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapterQnaList.getFilter().filter(s.toString());
            }

            @Override
             public void afterTextChanged(Editable s) {
                Log.d("TAG", "afterTextChanged");
//
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        readFirebase();
    }

    @Override
    public void onClick(View v) {
        int list_size = initList.size();

        Intent intent = new Intent(QnaListActivity.this, WriteQuestionActivity.class);
        intent.putExtra("listSize", list_size);
        startActivity(intent);
        search_question.getText().clear();
    }

    //firebase에서 데이터 읽어오기, 변화가 있으면 클라이언트에 알려줌
    private void readFirebase(){
        databaseReference = FirebaseDatabase.getInstance().getReference().child("JindaniApp").child("Question");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
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
                Log.d("TAG", "데이터 읽어오기 실패");
            }
        });
    }

}
