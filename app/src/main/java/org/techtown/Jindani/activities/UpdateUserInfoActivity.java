package org.techtown.Jindani.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.techtown.Jindani.R;
import org.techtown.Jindani.models.UserAccount;

public class UpdateUserInfoActivity extends AppCompatActivity {

    //구글 로그인 사용자용 기본정보 등록
    private static final String TAG = "UpdateUserInfoActivity";

    private FirebaseAuth auth; //파이어베이스 인증 처리
    private DatabaseReference databaseReference; // 실시간 데이터베이스

    private Button btnRegister;
    private EditText editHeight, editWeight, editPast, editSocial, editFamily;

    private UserAccount userAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);

        Intent intent = getIntent();
        userAccount = (UserAccount) intent.getSerializableExtra("userAccount");

        auth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference("JindaniApp");

        editHeight = findViewById(R.id.editHeight);
        editWeight = findViewById(R.id.editWeight);
        editPast = findViewById(R.id.editPast);
        editSocial = findViewById(R.id.editSocial);
        editFamily = findViewById(R.id.editFamily);

        //각 항목에 기존 데이터 표시
        setData();

        //등록 버튼
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //유저 객체 생성 후, db에 저장
                addFireBase();

                Toast.makeText(UpdateUserInfoActivity.this, "수정 성공", Toast.LENGTH_SHORT).show();

                finish();
            }
        });
    }

    //뒤로가기 버튼 눌렀을 때 동작
    @Override
    public void onBackPressed() {
        showDialog();
    }
    
    //작성 취소 대화창
    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateUserInfoActivity.this);
        builder.setMessage("작성 중인 내용은 저장되지 않습니다.\n작성을 종료하시겠습니까?");
        builder.setCancelable(false);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    private void setData() {
        editHeight.setText(userAccount.getHeight());
        editWeight.setText(userAccount.getWeight());
        editPast.setText(userAccount.getPast());
        editFamily.setText(userAccount.getFamily());
        editSocial.setText(userAccount.getSocial());
    }

    //파이어베이스에 저장
    private void addFireBase() {
        //파이어베이스에 유저 객체 저장
        FirebaseUser firebaseUser = auth.getCurrentUser();

        userAccount.setHeight(editHeight.getText().toString());
        userAccount.setWeight(editWeight.getText().toString());
        userAccount.setPast(editPast.getText().toString());
        userAccount.setFamily(editFamily.getText().toString());
        userAccount.setSocial(editSocial.getText().toString());

        //사용자db에 추가
        databaseReference.child("UserAccount").child(firebaseUser.getUid()).setValue(userAccount);
    }
}
