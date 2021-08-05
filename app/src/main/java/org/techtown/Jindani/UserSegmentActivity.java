package org.techtown.Jindani;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class UserSegmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_segment);

        Button user_signin = findViewById(R.id.user_signin);
        Button doc_signin = findViewById(R.id.doc_signin);

    }
}