package org.techtown.Jindani;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class QnaActivity extends AppCompatActivity {

    RecyclerView qnalist;
    private AdapterQnaList adapterQnaList;

    static final int REQUSET_CODE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qna_list);

        //리사이클러뷰
        qnalist = findViewById(R.id.qnalist);
        qnalist.setLayoutManager(new LinearLayoutManager(this));
        adapterQnaList = new AdapterQnaList(qnalist);
        qnalist.setAdapter(adapterQnaList);

        Button button = findViewById(R.id.qna_list_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText e = findViewById(R.id.search_q);
                Intent intent = new Intent(getApplicationContext(), WriteQActivity.class);
                startActivityForResult(intent, REQUSET_CODE);
                e.getText().clear();
            }
        });
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



//    /**카페 검색**/
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu, menu);
//        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
//
//        //입력창 레이아웃
//        searchView.setMaxWidth(Integer.MAX_VALUE); //검색 버튼 클릭 시 검색창을 가로너비에 맞게 설정
//        searchView.setQueryHint("카페명 검색");
//
//        //검색했을 때 동작
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                if (newText != null) {
//                    adapter.getFilter().filter(newText);
//                }
//                return true;
//            }
//        });
//        return true;
//    }
}
