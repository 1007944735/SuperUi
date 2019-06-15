package com.sgevf.superui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.sgevf.ui.RefreshLayout.RefreshLayout;
import com.sgevf.ui.RefreshLayout.RefreshListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RefreshListener {
    ListView listView;
    ArrayAdapter<String> adapter;
    List<String> names;
    RefreshLayout refresh;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            refresh.finishRefreshing();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=findViewById(R.id.listView);
        refresh=findViewById(R.id.refresh);
        names=new ArrayList<>();
        for(int i=0;i<20;i++){
            names.add("数据"+i);
        }
        adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,names);
        listView.setAdapter(adapter);
        refresh.setRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        new  TestAsync().execute();
    }

    public class TestAsync extends AsyncTask<Void,Integer,Integer>{
        @Override
        protected void onPostExecute(Integer integer) {
            refresh.finishRefreshing();
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 1;
        }
    }
}
