package com.sgevf.superui;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.sgevf.ui.BoxDialog;
import com.sgevf.ui.RefreshLayout.RefreshLayout;
import com.sgevf.ui.RefreshLayout.RefreshListener;
import com.sgevf.ui.WebLoadingActivity;
import com.sgevf.ui.utils.DialogHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RefreshListener {
    ListView listView;
    ArrayAdapter<String> adapter;
    List<String> names;
    RefreshLayout refresh;
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
        new  TestAsync(0).execute();
    }

    @Override
    public void onMore() {
        new  TestAsync(1).execute();
    }

    public void showDialog(View view) {
        DialogHelper.showDialog(this,TestDialog.class);
//        startActivity(new Intent(this,WebLoadingActivity.class).putExtra("url","https://www.baidu.com/"));
    }

    public class TestAsync extends AsyncTask<Void,Integer,Integer>{
        int type;
        public TestAsync(int type){
            this.type=type;
        }
        @Override
        protected void onPostExecute(Integer integer) {
            if(type==0) {
                refresh.finishHeadRefreshing();
            }else if(type==1){
                refresh.finishFootRefreshing();
            }
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return type;
        }
    }
}
