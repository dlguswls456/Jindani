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
import android.widget.Toast;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;

public class PersonInfoActivity extends AppCompatActivity {

    Button btnRegister;
    RadioGroup radioGroup;
    EditText editHeight, editWeight, editDate, editPast, editSocial, editFamily;

    DatePicker datePicker;
    int age;
    final int DIALOG_DATE = 1;

//    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
//        @Override
//        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//            Toast.makeText(getApplicationContext(), year + "년" + monthOfYear + "월" + dayOfMonth +"일", Toast.LENGTH_SHORT).show();
//        }
//    };

    HashMap<String, String> personInfo = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);

        radioGroup = findViewById(R.id.radioGroup);
        btnRegister = findViewById(R.id.btnRegister);
        editHeight = findViewById(R.id.editHeight);
        editWeight = findViewById(R.id.editWeight);
        editDate = findViewById(R.id.editDate);
        editPast = findViewById(R.id.editPast);
        editSocial = findViewById(R.id.editSocial);
        editFamily = findViewById(R.id.editFamily);

        datePicker = findViewById(R.id.datePicker);

        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String birthDate = year + "년 " + (monthOfYear + 1) + "월 " + dayOfMonth + "일";
                editDate.setText(birthDate);
                age = getAge(year, monthOfYear, dayOfMonth);
            }
        });


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioButtonId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = findViewById(radioButtonId);

                personInfo.put("Sex", radioButton.getText().toString());
                personInfo.put("Age", age + "");
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
        Calendar current = Calendar.getInstance();
        int currentYear = current.get(Calendar.YEAR);
        int currentMonth = current.get(Calendar.MONTH) + 1;
        int currentDay = current.get(Calendar.DAY_OF_MONTH);

        int age = currentYear - birthYear;
        // 생일 안 지난 경우 -1
        if (birthMonth * 100 + birthDay > currentMonth * 100 + currentDay)
            age--;

        return age;
    }

}
