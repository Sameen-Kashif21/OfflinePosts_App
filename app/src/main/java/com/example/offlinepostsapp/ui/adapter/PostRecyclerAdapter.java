package com.example.offlinepostsapp.ui.adapter;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.offlinepostsapp.R;

public class PostRecyclerAdapter extends RecyclerView.Adapter<PostRecyclerAdapter.VH> {

    public interface OnPostClick {
        void onClick(int postId, String title, String body);
    }

    private Cursor cursor;
    private final OnPostClick listener;

    public PostRecyclerAdapter(Cursor cursor, OnPostClick listener) {
        this.cursor = cursor;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_recycler, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        if (cursor == null || !cursor.moveToPosition(position)) return;

        int postId = cursor.getInt(cursor.getColumnIndexOrThrow("post_id"));
        String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
        String body = cursor.getString(cursor.getColumnIndexOrThrow("body"));

        h.tvTitle.setText(title);
        h.tvBody.setText(body);

        h.itemView.setOnClickListener(v -> listener.onClick(postId, title, body));
    }

    @Override
    public int getItemCount() {
        return (cursor == null) ? 0 : cursor.getCount();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvTitle, tvBody;
        VH(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvBody = itemView.findViewById(R.id.tvBody);
        }
    }
}
