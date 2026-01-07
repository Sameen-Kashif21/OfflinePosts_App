package com.example.offlinepostsapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import com.example.offlinepostsapp.R;
import com.example.offlinepostsapp.data.AppPrefs;
import com.example.offlinepostsapp.data.PostDbHelper;

public class PostDetailActivity extends AppCompatActivity {

    private EditText etTitle, etBody;
    private int postId;
    private PostDbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        applySavedTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        db = new PostDbHelper(this);

        etTitle = findViewById(R.id.etTitle);
        etBody = findViewById(R.id.etBody);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnMore = findViewById(R.id.btnMore);

        Intent i = getIntent();
        postId = i.getIntExtra("postId", -1);
        etTitle.setText(i.getStringExtra("title"));
        etBody.setText(i.getStringExtra("body"));

        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String body = etBody.getText().toString().trim();

            if (title.isEmpty()) { etTitle.setError("Title required"); return; }
            if (body.isEmpty()) { etBody.setError("Body required"); return; }

            if (postId != -1) {
                db.updateTitleBody(postId, title, body);
                Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btnMore.setOnClickListener(v -> showPopup(btnMore));
    }

    private void showPopup(Button anchor) {
        PopupMenu pm = new PopupMenu(this, anchor);
        pm.getMenuInflater().inflate(R.menu.menu_popup_post, pm.getMenu());
        pm.setOnMenuItemClickListener(this::onPopupItem);
        pm.show();
    }

    private boolean onPopupItem(MenuItem item) {
        if (item.getItemId() == R.id.pop_open_web) {
            Intent w = new Intent(this, WebViewActivity.class);
            w.putExtra("url", "https://jsonplaceholder.typicode.com/posts/" + postId);
            startActivity(w);
            return true;
        } else if (item.getItemId() == R.id.pop_share) {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, "Post #" + postId + "\n" + etTitle.getText().toString());
            startActivity(Intent.createChooser(share, "Share via"));
            return true;
        }
        return false;
    }

    private void applySavedTheme() {
        String t = AppPrefs.getTheme(this);
        if (AppPrefs.THEME_DARK.equals(t)) setTheme(R.style.Theme_OfflinePosts_Dark);
        else if (AppPrefs.THEME_BLUE.equals(t)) setTheme(R.style.Theme_OfflinePosts_Blue);
        else setTheme(R.style.Theme_OfflinePosts_Light);
    }
}
