package org.techtown.Jindani;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class QnaActivity extends AppCompatActivity {

    RecyclerView qnalist;

    private AdapterQnaList adapterQnaList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qna_list_page);

        qnalist = findViewById(R.id.qnalist);
        qnalist.setLayoutManager(new LinearLayoutManager(this));
        adapterQnaList = new AdapterQnaList(qnalist);
        qnalist.setAdapter(adapterQnaList);

        adapterQnaList.addChatToList(new QnaModel("배가 아픈데 왜 그러나요?", false));
        adapterQnaList.addChatToList(new QnaModel("배가 아픈데 왜 그러나요?", false));
        adapterQnaList.addChatToList(new QnaModel("배가 아픈데 왜 그러나요?", false));
        adapterQnaList.addChatToList(new QnaModel("배가 아픈데 왜 그러나요?", false));
        adapterQnaList.addChatToList(new QnaModel("배가 아픈데 왜 그러나요?", false));
        adapterQnaList.addChatToList(new QnaModel("배가 아픈데 왜 그러나요?", false));
        adapterQnaList.addChatToList(new QnaModel("배가 아픈데 왜 그러나요?", false));
        adapterQnaList.addChatToList(new QnaModel("배가 아픈데 왜 그러나요?", false));
        adapterQnaList.addChatToList(new QnaModel("배가 아픈데 왜 그러나요?", false));
        adapterQnaList.addChatToList(new QnaModel("배가 아픈데 왜 그러나요?", false));

    }

}
