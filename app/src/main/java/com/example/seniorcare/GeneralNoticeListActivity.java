package com.example.seniorcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

public class GeneralNoticeListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_general_notice_list);

            RecyclerView noticeListView = findViewById(R.id.noticeRecycleView);

            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            noticeListView.setLayoutManager(layoutManager);

            NoticeListAdapter sensorListAdapter = new NoticeListAdapter(this.getApplicationContext());
            noticeListView.setAdapter(sensorListAdapter);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
}
