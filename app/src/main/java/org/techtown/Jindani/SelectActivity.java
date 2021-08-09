package org.techtown.Jindani;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SelectActivity extends AppCompatActivity {

    Button chat_button, qna_button, btn_logout;

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference databaseReference; // 실시간 데이터베이스

    UserAccount userAccount;
    int age;
    private String ageCategory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        //버튼 레이아웃 변경
        chat_button = findViewById(R.id.chat_button);
        qna_button = findViewById(R.id.qna_button);
        btn_logout = findViewById(R.id.btn_logout);
        setLayout();

        //현재 사용자 데이터 가져옴
        databaseReference = FirebaseDatabase.getInstance().getReference("JindaniApp");
        readFirebase(new FirebaseCallback<UserAccount>() {
            @Override
            public void onCallback(UserAccount value) {
                Log.d("TAG", "onCallback 성공");

                //가져온 생년월일로 나이 카테고리 구하기
                //생년월일로 Calendar객체 만들기
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date birthDate = null;
                try {
                    birthDate = sdf.parse(userAccount.getBirthDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
//                System.out.println(sdf.format(birthDate));
                Calendar birthCal = Calendar.getInstance();
                birthCal.setTime(birthDate);

                //나이계산 및 나이 카테고리 계산
                age = getAge(birthCal.get(Calendar.YEAR), birthCal.get(Calendar.MONTH), birthCal.get(Calendar.DATE));
                ageCategory = getAgeCategory(age, birthCal);

                //확인용 text
                String birthStr = userAccount.getBirthDate() + " 만 " + age + "세 " + ageCategory;
                Toast.makeText(SelectActivity.this, birthStr, Toast.LENGTH_SHORT).show();
            }

        });

        //채팅 버튼 -> 정보 담아서 보내줘야함
        chat_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //바로 채팅으로 넘어가게(ChatActivity), 정보들 들고가야함
                Intent intent = new Intent(getApplicationContext(), PersonInfoActivity.class);
                startActivity(intent);
            }
        });

        //qna 버튼
        qna_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), QnaListActivity.class);
                startActivity(intent);
            }
        });

        //로그아웃 버튼
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();

                Intent intent = new Intent(SelectActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    //버튼 레이아웃 변경
    private void setLayout() {
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

    //파이어베이스에서 데이터 가져오기
    public void readFirebase(FirebaseCallback firebaseCallback) {
        databaseReference.child("UserAccount").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {//사용자 데이터 성공적으로 가져오면
                userAccount = snapshot.getValue(UserAccount.class);
                firebaseCallback.onCallback(userAccount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {//데이터 가져오기 실패
                Log.e("SelectActivity", String.valueOf(error.toException()));
            }
        });
    }

    //만나이 계산기
    public int getAge(int birthYear, int birthMonth, int birthDay) {
        //현재날짜 가져옴
        Calendar current = Calendar.getInstance();
        int currentYear = current.get(Calendar.YEAR);
        int currentMonth = current.get(Calendar.MONTH);
        int currentDay = current.get(Calendar.DAY_OF_MONTH);

        int age = currentYear - birthYear;
        // 생일 안 지난 경우 -1
        if (birthMonth * 100 + birthDay > currentMonth * 100 + currentDay) {
            age--;
        }

        return age;
    }

    //나이 카테고리 구하기
    public String getAgeCategory(int age, Calendar birthCal) {
        //현재 날짜 가져옴
        Calendar current = Calendar.getInstance();

        if (age / 10 >= 9) {//나이가 90대 이상
            return "90s";
        } else if (age / 10 >= 1) {//10대 이상, 90대 미만
            return (age / 10) * 10 + "s";
        } else {//0살~9살
            if (age >= 1 & age <= 6) {//1세~6세 유아
                return "유아";
            } else if (age >= 7) {//7세~9세
                return "0s";
            } else {//0세
                current.add(Calendar.MONTH, -1);//현 날짜보다 1달 전으로 설정
                if (birthCal.after(current)) {//생후 1달 미만 신생아
                    return "신생아";
                } else {//생후 1개월~1년 미만 영아
                    return "영아";
                }
            }
        }
    }

    //로그아웃
    private void signOut() {
        // Firebase sign out
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();

        //구글 로그인 사용자에게만 필요.. 나중에 수정 해야함
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);
        // Google sign out
        googleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Complete", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
