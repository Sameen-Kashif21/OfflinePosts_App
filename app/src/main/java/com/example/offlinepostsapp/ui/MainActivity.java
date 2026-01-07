package com.example.offlinepostsapp.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.offlinepostsapp.R;
import com.example.offlinepostsapp.data.AppPrefs;
import com.example.offlinepostsapp.data.PostDbHelper;
import com.example.offlinepostsapp.model.Post;
import com.example.offlinepostsapp.network.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private SimpleCursorAdapter adapter;
    private PostDbHelper db;
    private Cursor cursor;

    private Switch switchOffline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        applySavedTheme();
        super.onCreate(savedInstanceState);

        if (!AppPrefs.isLoggedIn(this)) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        db = new PostDbHelper(this);
        listView = findViewById(R.id.listView);
        switchOffline = findViewById(R.id.switchOffline);
        Spinner spinnerFilter = findViewById(R.id.spinnerFilter);

        // Spinner example (requirement)
        ArrayAdapter<String> spAd = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"All Posts"});
        spinnerFilter.setAdapter(spAd);

        // Default adapter: SimpleCursorAdapter
        String[] from = {"title", "body"};
        int[] to = {R.id.tvTitle, R.id.tvBody};
        cursor = db.getAllCursor();
        adapter = new SimpleCursorAdapter(this, R.layout.row_post, cursor, from, to, 0);
        listView.setAdapter(adapter);

        // Context menu for items
        registerForContextMenu(listView);

        // Click opens detail
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Cursor c = (Cursor) adapter.getItem(position);
            int postId = c.getInt(c.getColumnIndexOrThrow("post_id"));
            String title = c.getString(c.getColumnIndexOrThrow("title"));
            String body = c.getString(c.getColumnIndexOrThrow("body"));

            Intent i = new Intent(this, PostDetailActivity.class);
            i.putExtra("postId", postId);
            i.putExtra("title", title);
            i.putExtra("body", body);
            startActivity(i);
        });

        // Offline switch
        switchOffline.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) fetchAndStorePosts();
            else Toast.makeText(this, "Offline mode enabled (SQLite)", Toast.LENGTH_SHORT).show();
        });

        // Restore state on rotation
        if (savedInstanceState != null) {
            switchOffline.setChecked(savedInstanceState.getBoolean("offline", false));
        }

        // Fetch once if DB empty and not offline
        if (!switchOffline.isChecked() && adapter.getCount() == 0) {
            fetchAndStorePosts();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("offline", switchOffline.isChecked());
        super.onSaveInstanceState(outState);
    }

    private void fetchAndStorePosts() {
        ApiClient.create().getPosts().enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                List<Post> posts = response.body();
                if (!response.isSuccessful() || posts == null || posts.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Empty API response. Showing offline data.", Toast.LENGTH_SHORT).show();
                    refreshCursor();
                    return;
                }

                for (Post p : posts) {
                    db.insertOrReplace(p.id, p.userId, p.title, p.body);
                }
                Toast.makeText(MainActivity.this, "Synced " + posts.size() + " posts", Toast.LENGTH_SHORT).show();
                refreshCursor();
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Network failed. Showing offline data.", Toast.LENGTH_SHORT).show();
                refreshCursor();
            }
        });
    }

    private void refreshCursor() {
        Cursor newCursor = db.getAllCursor();
        adapter.changeCursor(newCursor);
        if (cursor != null) cursor.close();
        cursor = newCursor;
    }

    // Options Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_theme_light) {
            AppPrefs.setTheme(this, AppPrefs.THEME_LIGHT);
            recreate();
            return true;
        } else if (id == R.id.action_theme_dark) {
            AppPrefs.setTheme(this, AppPrefs.THEME_DARK);
            recreate();
            return true;
        } else if (id == R.id.action_theme_blue) {
            AppPrefs.setTheme(this, AppPrefs.THEME_BLUE);
            recreate();
            return true;
        } else if (id == R.id.action_open_recycler) {
            startActivity(new Intent(this, RecyclerActivity.class));
            return true;
        } else if (id == R.id.action_logout) {
            AppPrefs.setLoggedIn(this, false);
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Context menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_context_post, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Cursor c = (Cursor) adapter.getItem(info.position);
        int postId = c.getInt(c.getColumnIndexOrThrow("post_id"));

        if (item.getItemId() == R.id.ctx_delete) {
            db.deleteByPostId(postId);
            refreshCursor();
            return true;
        } else if (item.getItemId() == R.id.ctx_edit) {
            Intent i = new Intent(this, PostDetailActivity.class);
            i.putExtra("postId", postId);
            i.putExtra("title", c.getString(c.getColumnIndexOrThrow("title")));
            i.putExtra("body", c.getString(c.getColumnIndexOrThrow("body")));
            i.putExtra("edit", true);
            startActivity(i);
            return true;
        }
        return super.onContextItemSelected(item);
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
