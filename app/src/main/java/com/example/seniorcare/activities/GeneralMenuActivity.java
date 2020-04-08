package com.example.seniorcare.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.seniorcare.R;
import com.example.seniorcare.models.User;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.seniorcare.activities.Constant.SENIOR_NAME;
import static com.example.seniorcare.activities.Constant.SENIOR_USER_ID;

public class GeneralMenuActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    ImageButton addSenior;
    Button logoutButton;
    ListView featureList;
    String[] featureListItem;
    ArrayAdapter<String> adapter;
    Spinner seniorSpinner;
    private HashMap<String, User> seniorUserDropDownListHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_menu);

        addSenior = findViewById(R.id.addUserImageButton);
        addSenior.setOnClickListener(this);

        logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(this);

        featureList = findViewById(R.id.featureListView);

        featureListItem = getResources().getStringArray(R.array.general_menu_list_items);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, featureListItem);
        featureList.setAdapter(adapter);
        featureList.setOnItemClickListener(this);

        seniorSpinner = findViewById(R.id.seniorSpinner);
        setSeniorSpinner();
    }

    protected void onRestart() {
        super.onRestart();
        setContentView(R.layout.activity_general_menu);

        addSenior = findViewById(R.id.addUserImageButton);
        addSenior.setOnClickListener(this);

        logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(this);

        featureList = findViewById(R.id.featureListView);

        featureListItem = getResources().getStringArray(R.array.general_menu_list_items);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, featureListItem);
        featureList.setAdapter(adapter);
        featureList.setOnItemClickListener(this);

        seniorSpinner = findViewById(R.id.seniorSpinner);
        setSeniorSpinner();
    }

    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        if (position == 0) {
            Intent mapIntent = new Intent(this, GoogleMapsActivity.class);
            startActivity(mapIntent);
        } else if (position == 1) {
            Intent generalNoticeListActivityIntent = new Intent(this, GeneralNoticeListActivity.class);
            String seniorName = seniorSpinner.getSelectedItem().toString();
            generalNoticeListActivityIntent.putExtra(SENIOR_NAME, seniorName);
            generalNoticeListActivityIntent.putExtra(SENIOR_USER_ID, seniorUserDropDownListHashMap.get(seniorName).getUserId());
            startActivity(generalNoticeListActivityIntent);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.addUserImageButton) {
            Intent addSeniorIntent = new Intent(this, SetSeniorActivity.class);
            startActivity(addSeniorIntent);
        } else if (v.getId() == R.id.logoutButton) {
            accountService.logout();
            finish();
        }
    }

    protected void setSeniorSpinner() {
        List<String> seniorIdList = managedUserService.getManagedUserIds(accountService.getCurrentUser());
        seniorUserDropDownListHashMap = new HashMap<String, User>();

        seniorIdList.forEach(seniorId -> {
            User user = accountService.getUserById(seniorId);
            seniorUserDropDownListHashMap.put(
                    user.getName(),
                    user
            );
        });


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                seniorUserDropDownListHashMap.keySet().stream().collect(Collectors.toList())
        );
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        seniorSpinner.setAdapter(dataAdapter);
    }
}
