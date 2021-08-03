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

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_page);

        //채팅 버튼
        Button chat_button = findViewById(R.id.chat_button);
        chat_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PersonInfoActivity.class);
                startActivity(intent);
            }
        });

        //qna 버튼
        Button qna_button = findViewById(R.id.qna_button);
        qna_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //버튼 레이아웃 변경
        String chat = chat_button.getText().toString();
        String qna = qna_button.getText().toString();
        SpannableStringBuilder c_spannable = new SpannableStringBuilder(chat);
        SpannableStringBuilder q_spannable = new SpannableStringBuilder(qna);

        String c_word = "질환 예측";
        String q_word = "의사에게 질문";
        int c_start = chat.indexOf(c_word);
        int c_end = c_start + c_word.length();
        int q_start = qna.indexOf(q_word);
        int q_end = q_start + q_word.length();

//        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        c_spannable.setSpan(new StyleSpan(Typeface.BOLD), c_start, c_end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        c_spannable.setSpan(new RelativeSizeSpan(1.3f), c_start, c_end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //상대적 크기
//        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        q_spannable.setSpan(new StyleSpan(Typeface.BOLD), q_start, q_end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        q_spannable.setSpan(new RelativeSizeSpan(1.3f), q_start, q_end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //상대적 크기

        chat_button.setText(c_spannable);
        qna_button.setText(q_spannable);


    }
}
