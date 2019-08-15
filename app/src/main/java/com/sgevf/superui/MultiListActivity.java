package com.sgevf.superui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;

import com.sgevf.ui.FixedListView;

import java.util.Arrays;

public class MultiListActivity extends AppCompatActivity {
    private FixedListView listView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_multi_list);
        listView=findViewById(R.id.listView);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,Arrays.asList("Stirng","sasd","asd","asdasded","wwdwdwd","asjkdjlasd","qwxas","awras","ascwqs","asrgdas","sxasesfasc","wwdwdwd","asjkdjlasd","qwxas","awras","ascwqs","asrgdas","sxasesfasc"));
        listView.setAdapter(adapter);
    }
}
