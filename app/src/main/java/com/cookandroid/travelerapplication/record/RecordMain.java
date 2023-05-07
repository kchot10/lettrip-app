package com.cookandroid.travelerapplication.record;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.cookandroid.travelerapplication.R;

public class RecordMain extends AppCompatActivity {
    private EditText edittext_title;
    Spinner spinner; //지역 구분1
    Spinner spinner2; //지역 구분2
    ArrayAdapter<CharSequence> adapter;
    ArrayAdapter<CharSequence> adapter2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_main);

        edittext_title = findViewById(R.id.edittext_title);
        String title = String.valueOf(edittext_title.getText()); //제목 받아오기 - db 저장 필요

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


    }

    private void setCitySpinnerAdapterItem(int array_resource) {

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, (String[])getResources().getStringArray(array_resource));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter);
    }


}
