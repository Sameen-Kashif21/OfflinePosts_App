package com.example.offlinepostsapp.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.offlinepostsapp.R;
import com.example.offlinepostsapp.data.AppPrefs;
import com.example.offlinepostsapp.data.PostDbHelper;
import com.example.offlinepostsapp.ui.adapter.PostRecyclerAdapter;

public class RecyclerActivity extends AppCompatActivity {

    private PostDbHelper db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        applySavedTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        db = new PostDbHelper(this);

        RecyclerView rv = findViewById(R.id.recycler);
        rv.setLayoutManager(new LinearLayoutManager(this));

        cursor = db.getAllCursor();
        PostRecyclerAdapter adapter = new PostRecyclerAdapter(cursor, (postId, title, body) -> {
            Intent i = new Intent(this, PostDetailActivity.class);
            i.putExtra("postId", postId);
            i.putExtra("title", title);
            i.putExtra("body", body);
            startActivity(i);
        });

        rv.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        if (cursor != null) cursor.close();
        super.onDestroy();
    }

    private void applySavedTheme() {
        String t = AppPrefs.getTheme(this);
        if (AppPrefs.THEME_DARK.equals(t)) setTheme(R.style.Theme_OfflinePosts_Dark);
        else if (AppPrefs.THEME_BLUE.equals(t)) setTheme(R.style.Theme_OfflinePosts_Blue);
        else setTheme(R.style.Theme_OfflinePosts_Light);
    }
}
