package com.sgevf.superui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sgevf.ui.RefreshRecyclerView;
import com.sgevf.ui.refreshRecyclerView.RefreshLoadListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RefreshLoadListener {
    private RefreshRecyclerView recyclerView;
    private TestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        List<String> list = new ArrayList<>();
        adapter = new TestAdapter(list);
        recyclerView.setAdapter(adapter);
        recyclerView.setRefreshLoadListener(this);
        recyclerView.firstLoad();
    }

    @Override
    public void loadMore() {
        TestAsync testAsync = new TestAsync(adapter, recyclerView);
        testAsync.execute(1);
    }

    @Override
    public void loadRefresh() {
        TestAsync testAsync = new TestAsync(adapter, recyclerView);
        testAsync.execute(0);
    }

    class TestAsync extends AsyncTask<Integer, Integer, String> {
        private Integer flag;
        private TestAdapter adapter;
        private RefreshRecyclerView recyclerView;

        public TestAsync(TestAdapter adapter, RefreshRecyclerView recyclerView) {
            this.adapter = adapter;
            this.recyclerView = recyclerView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Integer... integers) {
            flag = integers[0];
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            List<String> list1 = new ArrayList<>();
            for (int i = 50; i < 100; i++) {
                list1.add(i + "");
            }
            adapter.addData(flag, list1);
            recyclerView.stopRefresh(!list1.isEmpty());
        }
    }


}
