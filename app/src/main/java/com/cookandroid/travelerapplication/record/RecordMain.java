package com.cookandroid.travelerapplication.record;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.cookandroid.travelerapplication.MainActivity;
import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.travel.CourseActivity;

import java.util.Calendar;

public class RecordMain extends AppCompatActivity {
    private EditText edittext_title;
    Spinner spinner;
    Spinner spinner2;
    ArrayAdapter<CharSequence> adapter;
    ArrayAdapter<CharSequence> adapter2;
    DatePickerDialog datePickerDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_main);

        //제목
        edittext_title = findViewById(R.id.edittext_title);
        String title = String.valueOf(edittext_title.getText()); //제목 받아오기 - db 저장 필요


        //도시 선택
        adapter = ArrayAdapter.createFromResource(this, R.array.my_array_state, R.layout.spinner_layout);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner = findViewById(R.id.cityDropdown);
        spinner.setAdapter(adapter);

        adapter2 = ArrayAdapter.createFromResource(this, R.array.city_array_default, R.layout.spinner_layout);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2 = findViewById(R.id.cityDropdown_detail);
        spinner2.setAdapter(adapter2);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                String selectedCity;
                switch(position){
                    case 0:
                        setCitySpinnerAdapterItem(R.array.city_array_default);
                        selectedCity = spinner2.getSelectedItem().toString(); //도시명 받아오기 - db 저장 필요
                        break;
                    case 1:
                        setCitySpinnerAdapterItem(R.array.서울특별시);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast toast = Toast.makeText(getApplicationContext(), "도시를 선택하세요.", Toast.LENGTH_LONG);
            }
        });


        //날짜 선택
        Button dateBtn_start = findViewById(R.id.dateBtn_start);
        dateBtn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(RecordMain.this);
                dialog.setContentView(R.layout.activity_record_date);

                Button close = dialog.findViewById(R.id.cancelBtn);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });


                CalendarView calendarView = dialog.findViewById(R.id.calendarView);
                calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                        String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                        dateBtn_start.setText(selectedDate);
                        //날짜 db 저장하기
                    }
                });


                Button ok = dialog.findViewById(R.id.okBtn);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //받아온 데이터를 db에 저장
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        Button dateBtn_end = findViewById(R.id.dateBtn_end);
        dateBtn_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(RecordMain.this);
                dialog.setContentView(R.layout.activity_record_date);

                Button close = dialog.findViewById(R.id.cancelBtn);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });


                CalendarView calendarView = dialog.findViewById(R.id.calendarView);
                calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                        String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                        dateBtn_end.setText(selectedDate);
                        //날짜 db 저장하기
                    }
                });


                Button ok = dialog.findViewById(R.id.okBtn);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //받아온 데이터를 db에 저장
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });


        //총 비용 입력
        EditText costEdittext = findViewById(R.id.costEditText);
        String cost = costEdittext.getText().toString();
        //받아 온 비용 db 저장하기


        //장소 추가 버튼 눌러서 리사이클러뷰 추가하기
        Button addPlaceBtn = findViewById(R.id.addPlaceBtn);
        addPlaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void setCitySpinnerAdapterItem(int array_resource) {
        adapter = new ArrayAdapter<>(this, R.layout.spinner_layout, (String[])getResources().getStringArray(array_resource));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter);
    }

}
