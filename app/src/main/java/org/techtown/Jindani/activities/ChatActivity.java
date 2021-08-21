package org.techtown.Jindani.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.techtown.Jindani.adapter.AdapterChatBot;
import org.techtown.Jindani.models.ChatModel;
import org.techtown.Jindani.models.Disease;
import org.techtown.Jindani.models.DiseaseInfo;
import org.techtown.Jindani.models.Level2;
import org.techtown.Jindani.models.Questions;
import org.techtown.Jindani.R;
import org.techtown.Jindani.network.RetrofitAPI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatActivity extends AppCompatActivity {

    //Flask 메인 주소
//    final String URL = "http://18.117.235.156:5000/";
    final String URL = "http://172.30.1.35:5000/";

    RecyclerView rvChatList;//채팅
    Button btnSend;//전송 버튼
    EditText etChat;//입력창

    //post할 것들
    TreeMap<Integer, Questions> QTree = new TreeMap<>();//질문목록 저장 <질문 순서, 질문>
    HashMap<String, String> answer;//답변 목록 저장 <질문 태그, 유저 답변>
    HashMap<String, String> for_info = new HashMap<>();//예측된 질병 top3이름 저장 <disease + n, 질병이름>

    private AdapterChatBot adapterChatBot;//리사이클러뷰 어댑터
    int i = 0;//질문목록 인덱스 접근용

    //예측 결과 저장
    List<Level2> level2_top2;
    List<Disease> diseases_top3;
    //질병정보 저장
    List<DiseaseInfo> top3_diseaseInfo;

    RetrofitAPI retrofitAPI;

    String level2_question_file_name = null;

    private static final int MAX_PREDICTED_LEVEL2 = 2;
    private static final int BASIC_PERSON_INFO_SIZE = 7;
    private static final String SELECT_FIRST_LEVEL2 = "1";
    private static final String SELECT_SECOND_LEVEL2 = "2";
    private static final String SELECT_NOTHING = "0";

    //level2가져올 때 사용
    Callback<List<Level2>> level2Callback = new Callback<List<Level2>>() {
        @Override
        public void onResponse(Call<List<Level2>> call, Response<List<Level2>> response) {
            if (response.isSuccessful()) {
                Log.d("TEST", "post level2 성공성공");

                //반환값 받아서 저장
                level2_top2 = response.body();

                i++;
                if (level2_top2.size() == MAX_PREDICTED_LEVEL2) {//level2_top2의 차이가 10%이하여서 top2모두 반환됐을 경우
                    adapterChatBot.addChatToList(new ChatModel("27가지 중 아래 2가지 범위의 질병이 예상됩니다.", true));
                    QTree.put(i, new Questions("select level2", "해당된다고 생각하면 숫자(1 or 2)를 선택해주시고, 해당되지 않는다고 생각하면 숫자 (0)을 선택해주세요."));
                } else {//차이가 10%이상
                    adapterChatBot.addChatToList(new ChatModel("27가지 중 아래 범위의 질병이 예상됩니다.", true));
                    QTree.put(i, new Questions("select level2", "해당된다고 생각하면 숫자(1)를 선택해주시고, 해당되지 않는다고 생각하면 숫자 (0)을 선택해주세요."));
                }
                adapterChatBot.addChatToList(new ChatModel(QTree.get(i).getQuestion(), true));//i를 키캆으로 이용

                //예측 결과 출력
                for (Level2 level2 : level2_top2) {
                    adapterChatBot.addChatToList(new ChatModel(level2.getLevel2_name(), true));
                }
            }
        }

        @Override
        public void onFailure(Call<List<Level2>> call, Throwable t) {
            Log.d("TEST", "post level2 실패실패");
            t.printStackTrace();
            Log.d("TEST", "post level2 실패실패");
        }
    };

    //질병top3 List로 가져올 때 사용
    Callback<List<Disease>> top3Callback = new Callback<List<Disease>>() {
        @Override
        public void onResponse(Call<List<Disease>> call, Response<List<Disease>> response) {
            if (response.isSuccessful()) {
                Log.d("TEST", "post top3 성공성공");

                //반환값 받아서 저장
                diseases_top3 = response.body();

                int idx = 0;
                //예측 결과 출력
                for (Disease disease : diseases_top3) {
                    for_info.put("disease" + idx, disease.getDisease_name());
                    idx++;
                }
                retrofitAPI.postDisease_for_Disease_info(for_info).enqueue(diseaseInfoCallback);
            }
        }

        @Override
        public void onFailure(Call<List<Disease>> call, Throwable t) {
            Log.d("TEST", "post top3 실패실패");
            t.printStackTrace();
            Log.d("TEST", "post top3 실패실패");
        }
    };

    Callback<List<DiseaseInfo>> diseaseInfoCallback = new Callback<List<DiseaseInfo>>() {
        @Override
        public void onResponse(Call<List<DiseaseInfo>> call, Response<List<DiseaseInfo>> response) {
            Log.d("TEST", "post info 성공성공");

            top3_diseaseInfo = response.body();

            for (int i = 0; i < top3_diseaseInfo.size(); i++) {
                Disease disease = diseases_top3.get(i);
                DiseaseInfo diseaseInfo = top3_diseaseInfo.get(i);
                adapterChatBot.addChatToList(new ChatModel(
                        diseaseInfo.getOName() + " " + disease.getPercentage() + "%\n" +
                                "동의어: " + diseaseInfo.getSyn() + "\n" +
                                "진료과: " + diseaseInfo.getDept() + "\n" +
                                "정의: " + diseaseInfo.getDef() + "\n", true));

            }
        }

        @Override
        public void onFailure(Call<List<DiseaseInfo>> call, Throwable t) {
            Log.d("TEST", "post info 실패실패");
            t.printStackTrace();
            Log.d("TEST", "post info 실패실패");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent getIntent = getIntent();
        answer = (HashMap<String, String>) getIntent.getSerializableExtra("personInfo");


        //level2를 위한 질문 목록 가져오기(일단은 로컬에서)
        loadQuestion("questions_for_level2", 0);

        //모델 예측하는데 시간 걸려서 timeout안되게 시간 늘림
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)
                .build();

        //Flask 연동
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitAPI = retrofit.create(RetrofitAPI.class);

        //채팅 형식으로 주고받기
        rvChatList = findViewById(R.id.rvChatList);
        rvChatList.setLayoutManager(new LinearLayoutManager(this));
        adapterChatBot = new AdapterChatBot(rvChatList);
        rvChatList.setAdapter(adapterChatBot);

        etChat = findViewById(R.id.etChat);
        btnSend = findViewById(R.id.btnSend);

        //먼저 질문던지기
        adapterChatBot.addChatToList(new ChatModel(QTree.get(i).getQuestion(), true));//i를 키캆으로 이용
        btnSend.setOnClickListener(new View.OnClickListener() {//전송 버튼 클릭이벤트
            @Override
            public void onClick(View v) {
                //입력창 비어있을 경우
                if (etChat.getText() == null || etChat.getText().length() == 0) {
                    Toast.makeText(ChatActivity.this, "Please enter a text", Toast.LENGTH_LONG).show();
                } else {//제대로 입력했을 경우
                    answer.put(QTree.get(i).getTag(), etChat.getText().toString());//답변 목록에 저장
                    adapterChatBot.addChatToList(new ChatModel(etChat.getText().toString(), false));//채팅창에 입력값 출력
                    etChat.getText().clear();//입력창 초기화

                    //모든 질문에 대답한 경우
                    if (answer.size() - BASIC_PERSON_INFO_SIZE == QTree.size()) {
                        //level2예측 전
                        if (level2_top2 == null) {
                            //지금까지의 답변 모아서 보내고 예측한 level2받아오기
                            adapterChatBot.addChatToList(new ChatModel("질병 예측 범위를 좁히고 있습니다! 잠시만 기다려주세요.", true));
                            retrofitAPI.postAnswer_for_Level2(answer).enqueue(level2Callback);
                        } else if (level2_question_file_name == null) {//level2 예측 후, 선택
                            //level2 2개중에 선택
                            if (answer.get("select level2").equals(SELECT_FIRST_LEVEL2)) {
                                //level2 한글 -> 영어 찾는 코드 필요
                                level2_question_file_name = level2_top2.get(0).getEng_name();
                            } else if (answer.get("select level2").equals(SELECT_SECOND_LEVEL2)) {
                                level2_question_file_name = level2_top2.get(1).getEng_name();
                            } else if (answer.get("select level2").equals(SELECT_NOTHING)) {
                                level2_question_file_name = "whole";
                            }

                            //다음 질문 목록 가져오기
                            loadQuestion(level2_question_file_name, QTree.size());

                            i++;
                            adapterChatBot.addChatToList(new ChatModel(QTree.get(i).getQuestion(), true));//i를 키캆으로 이용
                        } else {//top3예측 직전
                            //답변 모아서 보내고 예측한 질병 받아오기
                            adapterChatBot.addChatToList(new ChatModel("확률이 높은 질병 3개를 예측 중입니다.", true));
                            retrofitAPI.postAnswer_for_Disease(answer).enqueue(top3Callback);
                        }
                    } else {//아직 대답중인 경우
                        i++;
                        adapterChatBot.addChatToList(new ChatModel(QTree.get(i).getQuestion(), true));//다음 질문 던짐
                    }
                }
            }
        });
    }

    //질문 목록 가져오기
    public void loadQuestion(String filename, int index) {
        //파일 경로? 찾기
        int file = getApplicationContext().getResources().getIdentifier(filename, "raw", getApplicationContext().getPackageName());

        InputStream inputData = getResources().openRawResource(file);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputData));

        //txt파일을 한줄씩 읽어옴
        while (true) {
            String line = null;
            try {
                line = bufferedReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (line == null) {
                break;
            } else {
                String splited[] = line.split("\t");//탭을 기준으로 쪼갬
                //탭 기준 왼쪽은 질문 태그, 오른쪽은 질문
                Questions questions = new Questions(splited[0], splited[1]);
                QTree.put(index, questions);//TreeMap에 저장
                index++;
            }
        }
    }

    //뒤로가기 버튼 눌렀을 때 동작
    @Override
    public void onBackPressed() {
        showDialog();
    }

    //채팅 종료 대화창
    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
        builder.setMessage("채팅 내용은 저장되지 않습니다.\n채팅을 종료하시겠습니까?");
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
}
