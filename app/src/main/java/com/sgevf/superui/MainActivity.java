package com.sgevf.superui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.sgevf.ui.pickerView.MyPickerView;
import com.sgevf.ui.pickerView.PickerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MyPickerView myPickerView;
    private PickerView pickerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myPickerView =findViewById(R.id.myPickerView);
        List<TestData> list=new ArrayList<>();
        list.add(new TestData());
        list.add(new TestData());
        list.add(new TestData());
        list.add(new TestData());
        list.add(new TestData());
        list.add(new TestData());
        list.add(new TestData());
        list.add(new TestData());
        list.add(new TestData());
        myPickerView.setData(list);

        pickerView=findViewById(R.id.pickerView);
        pickerView.setLoop(false);
        List<String> list1=new ArrayList<>();
        list1.add("new TestData()");
        list1.add("new TestData()");
        list1.add("new TestData()");
        list1.add("new TestData()");
        list1.add("new TestData()");
        list1.add("new TestData()");
        list1.add("new TestData()");
        list1.add("new TestData()");
        list1.add("new TestData()");
        pickerView.setData(list1);
    }
}
