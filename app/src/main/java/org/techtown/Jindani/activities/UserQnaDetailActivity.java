package org.techtown.Jindani.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.techtown.Jindani.R;

public class UserQnaDetailActivity extends AppCompatActivity {

    TextView detail_title;
    TextView detail_content;
    TextView detail_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_qna_detail);

        detail_title = findViewById(R.id.detail_title);
        detail_content = findViewById(R.id.detail_content);
        detail_date = findViewById(R.id.detail_date);

        getQuestionData();

    }

    private void getQuestionData(){

        Intent intent = getIntent();

        String t = intent.getStringExtra("q_title");
        String c = intent.getStringExtra("q_content");
        String d = intent.getStringExtra("q_date");

        detail_title.setText(t);
        detail_content.setText(c);
        detail_date.setText(d);
    }
}