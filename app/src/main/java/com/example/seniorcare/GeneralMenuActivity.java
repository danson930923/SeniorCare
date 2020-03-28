package com.example.seniorcare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class GeneralMenuActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView featureList;
    String[] featureListItem;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_menu);

        featureList = findViewById(R.id.featureListView);

        featureListItem = getResources().getStringArray(R.array.general_menu_list_items);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, featureListItem);
        featureList.setAdapter(adapter);
        featureList.setOnItemClickListener(this);
    }

    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        String value=adapter.getItem(position);
        Toast.makeText(getApplicationContext(),value,Toast.LENGTH_SHORT).show();
    }
}