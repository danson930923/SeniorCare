package com.example.seniorcare.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.seniorcare.R;
import com.example.seniorcare.models.Reminder;

import java.util.List;

public class MessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        RecyclerView noticeListView = findViewById(R.id.noticeRecycleView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        noticeListView.setLayoutManager(layoutManager);

        List<Reminder> reminderList = null;
        NoticeListAdapter sensorListAdapter = new NoticeListAdapter(this.getApplicationContext(), reminderList);
        noticeListView.setAdapter(sensorListAdapter);
    }
}
