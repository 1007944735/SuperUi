package com.sgevf.superui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sgevf.ui.PickerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    PickerView pickerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pickerView=findViewById(R.id.pickerView);
        List<String> datas=new ArrayList<>();
        for(int i=0;i<100;i++){
            datas.add(i+"");
        }
        pickerView.setData(datas);
    }
}
