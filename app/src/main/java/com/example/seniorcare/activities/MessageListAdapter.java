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

import java.util.Arrays;
import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageListViewHolder> {
    public static class MessageListViewHolder extends RecyclerView.ViewHolder implements ImageButton.OnClickListener{
        public LinearLayout noticeItemView;
        public ImageButton stopImageButton;

        public MessageListViewHolder(LinearLayout noticeItemView) {
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
                } else if (v.getId() == R.id.deleteImageButton) {

                } else {
                    throw new Exception("Button not found");
                }
            } catch (Exception e) {
                Log.e("MAIN_BTN_CLICK_ERR ", e.getMessage());
            }
        }
    }

    private List<String> noticeList;
    public MessageListAdapter(Context context) {
        this.noticeList = Arrays.asList("sup1", "sup2", "sup3");
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public MessageListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout noticeItemView = (LinearLayout) LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.senior_notice_item_view, parent, false);

        return new MessageListViewHolder(noticeItemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull MessageListViewHolder holder, int position) {
        TextView noticeTV = holder.noticeItemView.findViewById(R.id.noticeTextView);
        noticeTV.setText(noticeList.get(position));
    }

    @Override
    public int getItemCount() {
        return noticeList.size();
    }
}