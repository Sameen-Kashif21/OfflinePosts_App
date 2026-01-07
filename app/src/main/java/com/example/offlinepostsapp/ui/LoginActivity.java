package com.example.offlinepostsapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.example.offlinepostsapp.R;
import com.example.offlinepostsapp.data.AppPrefs;

public class LoginActivity extends AppCompatActivity {
    private EditText etName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        applySavedTheme();
        super.onCreate(savedInstanceState);

        if (AppPrefs.isLoggedIn(this)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

        etName = findViewById(R.id.etName);
        Button btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            if (name.isEmpty()) {
                etName.setError("Enter your name");
                return;
            }
            AppPrefs.setLoggedIn(this, true);
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        if (savedInstanceState != null) {
            etName.setText(savedInstanceState.getString("name", ""));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("name", etName.getText().toString());
        super.onSaveInstanceState(outState);
    }

    private void applySavedTheme() {
        String t = AppPrefs.getTheme(this);
        if (AppPrefs.THEME_DARK.equals(t)) setTheme(R.style.Theme_OfflinePosts_Dark);
        else if (AppPrefs.THEME_BLUE.equals(t)) setTheme(R.style.Theme_OfflinePosts_Blue);
        else setTheme(R.style.Theme_OfflinePosts_Light);
    }
}
