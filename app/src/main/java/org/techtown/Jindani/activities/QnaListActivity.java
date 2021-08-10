package org.techtown.Jindani.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import org.techtown.Jindani.models.QnaModel;
import org.techtown.Jindani.R;

public class QnaListActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView qnalist;
    private AdapterQnaList adapterQnaList;

//    static final int REQUSET_CODE = 1;

    private FirebaseDatabase database;
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

        Button button = findViewById(R.id.write_q_button); //질문 작성 버튼
        button.setOnClickListener(QnaListActivity.this);

//        initDatabase();
        readFirebase();

    }

    @Override
    public void onClick(View v) {
        EditText e = findViewById(R.id.search_question);
        Intent intent = new Intent(QnaListActivity.this, WriteQuestionActivity.class);
        startActivity(intent);
        e.getText().clear();
    }

//    private void initDatabase(){
//        databaseReference = FirebaseDatabase.getInstance().getReference();
//    }

    private void readFirebase(){ //firebase에서 데이터 읽어오기, 변화가 있으면 클라이언트에 알려줌
        databaseReference = FirebaseDatabase.getInstance().getReference().child("JindaniApp").child("Question");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { //변화된 값이 snapshot으로 넘어옴
                adapterQnaList.list.clear(); //매번 모든 데이터를 가져오므로 리스트를 비워주기
                for(DataSnapshot ds : snapshot.getChildren()){ //Question아래에 있는 데이터 모두 가져오기
                    QnaModel q = ds.getValue(QnaModel.class);
                    adapterQnaList.addQToList(q);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QnaListActivity.this, "데이터 읽어오기 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }



//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) { //현재 사용 xx
//        super.onActivityResult(requestCode, resultCode, data);
//
//        //요청 코드와 응답 코드가 모두 정상이면
//        if (requestCode == REQUSET_CODE) {
//            if (resultCode == RESULT_OK) {
//
//                //WriteQActivity에서 값 받아오기
//                String t = data.getStringExtra("제목");
//                String c = data.getStringExtra("내용");
//
//
//            }
//        }
//    }

}
