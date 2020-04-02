package com.example.seniorcare;

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

import java.util.Arrays;
import java.util.List;

public class NoticeListAdapter extends RecyclerView.Adapter<NoticeListAdapter.NoticeListViewHolder> {
    public static class NoticeListViewHolder extends RecyclerView.ViewHolder implements ImageButton.OnClickListener{
        public LinearLayout noticeItemView;
        public ImageButton editImageButton;
        public ImageButton deleteImageButton;

        public NoticeListViewHolder(LinearLayout noticeItemView) {
            super(noticeItemView);
            this.noticeItemView = noticeItemView;
            editImageButton = noticeItemView.findViewById(R.id.editImageButton);
            deleteImageButton = noticeItemView.findViewById(R.id.deleteImageButton);
            editImageButton.setOnClickListener(this);
            deleteImageButton.setOnClickListener(this);
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
    public NoticeListAdapter(Context context) {
        this.noticeList = Arrays.asList("sup1", "sup2", "sup3");
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public NoticeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout noticeItemView = (LinearLayout) LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.notice_item_view, parent, false);

        return new NoticeListViewHolder(noticeItemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull NoticeListViewHolder holder, int position) {
        TextView noticeTV = holder.noticeItemView.findViewById(R.id.noticeTextView);
        noticeTV.setText(noticeList.get(position));
    }

    @Override
    public int getItemCount() {
        return noticeList.size();
    }
}