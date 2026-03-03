package com.example.mydiary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class ViewEntryActivity extends AppCompatActivity {

    private EditText editTextTitle;
    private EditText editTextContent;
    private Button buttonSave;

    private int entryPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_entry);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextContent = findViewById(R.id.edit_text_content);
        buttonSave = findViewById(R.id.button_save);

        Intent intent = getIntent();
        String title = intent.getStringExtra("entry_title");
        String content = intent.getStringExtra("entry_content");
        entryPosition = intent.getIntExtra("entry_position", -1);
        boolean editMode = intent.getBooleanExtra("edit_mode", false);

        editTextTitle.setText(title);
        editTextContent.setText(content);

        if (!editMode) {
            editTextTitle.setEnabled(false);
            editTextContent.setEnabled(false);
            buttonSave.setVisibility(View.GONE);
        }

        buttonSave.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("entry_title", editTextTitle.getText().toString());
            resultIntent.putExtra("entry_content", editTextContent.getText().toString());
            resultIntent.putExtra("entry_position", entryPosition);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}
