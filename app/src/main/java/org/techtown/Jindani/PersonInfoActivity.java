package org.techtown.Jindani;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class PersonInfoActivity extends AppCompatActivity {

    Button btnRegister;
    RadioGroup radioGroup;
    EditText editHeight, editWeight, editPast, editSocial, editFamily;
    TextView txtDate;

    DatePicker datePicker;
    int age;
    String ageCategory;

    HashMap<String, String> personInfo = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);

        radioGroup = findViewById(R.id.radioGroup);
        btnRegister = findViewById(R.id.btnRegister);
        editHeight = findViewById(R.id.editHeight);
        editWeight = findViewById(R.id.editWeight);
        txtDate = findViewById(R.id.txtDate);
        editPast = findViewById(R.id.editPast);
        editSocial = findViewById(R.id.editSocial);
        editFamily = findViewById(R.id.editFamily);

        datePicker = findViewById(R.id.datePicker);

        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //생년월일로 Calendar객체 만들기
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date birthDate = null;
                try {
                    //monthOfYear은 0부터 시작하기때문에 1 더해줘야함
                    birthDate = sdf.parse(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
//                System.out.println(sdf.format(birthDate));
                Calendar birthCal = Calendar.getInstance();
                birthCal.setTime(birthDate);

                //나이계산 및 나이 카테고리 계산
                age = getAge(year, monthOfYear, dayOfMonth);
                ageCategory = getAgeCategory(age, birthCal);

                //확인용 text
                String birthStr = year + "년 " + (monthOfYear + 1) + "월 " + dayOfMonth + "일" + " 만 " + age + "세 " + ageCategory;
                txtDate.setText(birthStr);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioButtonId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = findViewById(radioButtonId);

                personInfo.put("Sex", radioButton.getText().toString());
                personInfo.put("Age", ageCategory);
                personInfo.put("Height", editHeight.getText().toString());
                personInfo.put("Weight", editWeight.getText().toString());
                personInfo.put("과거력", editPast.getText().toString());
                personInfo.put("사회력", editSocial.getText().toString());
                personInfo.put("가족력", editFamily.getText().toString());
                //질문목록에서 과거력 사회력 가족력 빼야됨!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

                Intent intent = new Intent(PersonInfoActivity.this, MainActivity.class);
                intent.putExtra("personInfo", personInfo);
                startActivity(intent);
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

}
