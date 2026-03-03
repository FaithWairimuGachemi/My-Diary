
package com.example.mydiary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements EntryAdapter.OnEntryMenuItemClickListener {

    private static final int ADD_ENTRY_REQUEST = 1;
    public static final int VIEW_ENTRY_REQUEST = 2;
    private List<DiaryEntry> entries = new ArrayList<>();
    private EntryAdapter adapter;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        loadEntries();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EntryAdapter(entries, this);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddEntryActivity.class);
                startActivityForResult(intent, ADD_ENTRY_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_ENTRY_REQUEST && resultCode == RESULT_OK && data != null) {
            String title = data.getStringExtra("title");
            String content = data.getStringExtra("content");
            String date = data.getStringExtra("date");
            entries.add(0, new DiaryEntry(title, content, date));
            adapter.notifyItemInserted(0);
            saveEntries();
        } else if (requestCode == VIEW_ENTRY_REQUEST && resultCode == RESULT_OK && data != null) {
            int position = data.getIntExtra("entry_position", -1);
            if (position != -1) {
                String title = data.getStringExtra("entry_title");
                String content = data.getStringExtra("entry_content");
                entries.get(position).setTitle(title);
                entries.get(position).setContent(content);
                adapter.notifyItemChanged(position);
                saveEntries();
            }
        }
    }

    private void saveEntries() {
        SharedPreferences sharedPreferences = getSharedPreferences("diary_entries", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(entries);
        editor.putString("entries", json);
        editor.apply();
    }

    private void loadEntries() {
        SharedPreferences sharedPreferences = getSharedPreferences("diary_entries", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("entries", null);
        Type type = new TypeToken<ArrayList<DiaryEntry>>() {}.getType();
        entries = gson.fromJson(json, type);

        if (entries == null) {
            entries = new ArrayList<>();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            signOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, task -> {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                });
    }

    @Override
    public void onDeleteClick(int position) {
        entries.remove(position);
        adapter.notifyItemRemoved(position);
        saveEntries();
    }
}
