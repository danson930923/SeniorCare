package com.example.seniorcare.activities;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.seniorcare.R;
import com.example.seniorcare.models.Reminder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SeniorNoticeListAdapter extends RecyclerView.Adapter<SeniorNoticeListAdapter.SeniorNoticeListViewHolder> {
    private HashMap<String, Reminder> noticeMap;

    public static class SeniorNoticeListViewHolder extends RecyclerView.ViewHolder implements ImageButton.OnClickListener{
        public LinearLayout noticeItemView;
        public ImageButton stopImageButton;

        public SeniorNoticeListViewHolder(LinearLayout noticeItemView) {
            super(noticeItemView);
            this.noticeItemView = noticeItemView;
            stopImageButton = noticeItemView.findViewById(R.id.stopImageButton);
            stopImageButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            try {
                if (v.getId() == R.id.editImageButton) {
                    Context context = v.getContext();
                    Intent setNotificationIntent = new Intent(context, SetNotificationActivity.class);
                    context.startActivity(setNotificationIntent);
                } else if (v.getId() == R.id.stopImageButton) {

                } else {
                    throw new Exception("Button not found");
                }
            } catch (Exception e) {
                Log.e("MAIN_BTN_CLICK_ERR ", e.getMessage());
            }
        }
    }

    private List<String> noticeList;
    public SeniorNoticeListAdapter(Context context, List<Reminder> reminderList) {
        this.noticeList = new ArrayList<>();
        noticeMap = new HashMap<>();

        reminderList.stream().forEach(reminder -> {
            String noticeMsg = reminder.getTitle()
                    + " : "
                    + (new SimpleDateFormat("HH:mm")).format(reminder.getTime());
            noticeList.add(noticeMsg);
            noticeMap.put(noticeMsg, reminder);
        });
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public SeniorNoticeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout noticeItemView = (LinearLayout) LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.senior_notice_item_view, parent, false);

        return new SeniorNoticeListViewHolder(noticeItemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull SeniorNoticeListViewHolder holder, int position) {
        TextView noticeTV = holder.noticeItemView.findViewById(R.id.noticeTextView);
        noticeTV.setText(noticeList.get(position));
    }

    @Override
    public int getItemCount() {
        return noticeList.size();
    }
}