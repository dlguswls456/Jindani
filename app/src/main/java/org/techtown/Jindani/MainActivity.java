package org.techtown.Jindani;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    //Flask메인 주소
    final String URL = "http://172.30.1.17:5000";

    RecyclerView rvChatList;//채팅
    Button btnSend;//전송 버튼
    EditText etChat;//입력창

    TreeMap<Integer, Questions> QTree = new TreeMap<>();//질문목록 저장 <질문 순서, 질문>
    HashMap<String, String> answer = new HashMap<>();//답변 목록 저장 <질문 태그, 유저 답변>

    private AdapterChatBot adapterChatBot;//리사이클러뷰 어댑터
    int i = 0;//질문목록 인덱스 접근용

    //level2가져올 때 사용
    Callback<Level2> level2Callback = new Callback<Level2>() {
        @Override
        public void onResponse(Call<Level2> call, Response<Level2> response) {
            if (response.isSuccessful()) {
                Log.d("TEST", "post 성공성공");

                //반환값 받아서 저장
                Level2 level2 = response.body();
                //예측 결과 출력
                adapterChatBot.addChatToList(new ChatModel(level2.getFirst(), true));
                adapterChatBot.addChatToList(new ChatModel(level2.getSecond(), true));
            }
        }

        @Override
        public void onFailure(Call<Level2> call, Throwable t) {
            Log.d("TEST", "post 실패실패");
            t.printStackTrace();
            Log.d("TEST", "post 실패실패");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //level2를 위한 질문 목록 가져오기(일단은 로컬에서)
        try {
            loadQuestion("questions_for_level2",0);
//            loadQuestion("neck_back",QTree.size());
//            System.out.println(QTree.get(10).tag + ": " + QTree.get(10).question);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Flask 연동
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        //채팅 형식으로 주고받기
        rvChatList = findViewById(R.id.rvChatList);
        rvChatList.setLayoutManager(new LinearLayoutManager(this));
        adapterChatBot = new AdapterChatBot(rvChatList);
        rvChatList.setAdapter(adapterChatBot);

        etChat = findViewById(R.id.etChat);
        btnSend = findViewById(R.id.btnSend);

        //먼저 질문던지기
        adapterChatBot.addChatToList(new ChatModel(QTree.get(i).question, true));//i를 키캆으로 이용
        btnSend.setOnClickListener(new View.OnClickListener() {//전송 버튼 클릭이벤트
            @Override
            public void onClick(View v) {
                //입력창 비어있을 경우
                if(etChat.getText() == null || etChat.getText().length() == 0 ){
                    Toast.makeText(MainActivity.this, "Please enter a text", Toast.LENGTH_LONG).show();
                }else{//제대로 입력했을 경우
                    answer.put(QTree.get(i).tag, etChat.getText().toString());//답변 목록에 저장
                    adapterChatBot.addChatToList(new ChatModel(etChat.getText().toString(), false));//채팅창에 입력값 출력
                    etChat.getText().clear();//입력창 초기화

                    //모든 질문에 대답한 경우
                    if(answer.size()==QTree.size()){
                        //지금까지의 답변 모아서 보내고 예측한 level2받아오기
                        retrofitAPI.postAnswer_for_Level2(answer).enqueue(level2Callback);
                        adapterChatBot.addChatToList(new ChatModel("질병 예측 범위를 좁히고 있습니다! 잠시만 기다려주세요.", true));
                    }else{//아직 대답중인 경우
                        i++;
                        adapterChatBot.addChatToList(new ChatModel(QTree.get(i).question, true));//다음 질문 던짐
                    }
                }
            }
        });
    }

    //질문 목록 가져오기
    public void loadQuestion(String filename, int index) throws IOException {
        //파일 경로? 찾기
        int file = getApplicationContext().getResources().getIdentifier(filename, "raw", getApplicationContext().getPackageName());

        InputStream inputData = getResources().openRawResource(file);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputData));

        //txt파일을 한줄씩 읽어옴
        while (true){
            String line = bufferedReader.readLine();
            if(line == null)
                break;
            else{
                String splited[] = line.split("\t");//탭을 기준으로 쪼갬
                //탭 기준 왼쪽은 질문 태그, 오른쪽은 질문
                Questions questions = new Questions(splited[0], splited[1]);
                QTree.put(index, questions);//TreeMap에 저장
                index++;
            }
        }
    }
}
