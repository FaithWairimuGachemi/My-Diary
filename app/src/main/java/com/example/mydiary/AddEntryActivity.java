package com.example.mydiary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddEntryActivity extends AppCompatActivity {

    private EditText titleEditText;
    private EditText contentEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);

        titleEditText = findViewById(R.id.title_edit_text);
        contentEditText = findViewById(R.id.entry_edit_text);
        Button saveButton = findViewById(R.id.save_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEditText.getText().toString();
                String content = contentEditText.getText().toString();
                String date = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault()).format(new Date());

                Intent resultIntent = new Intent();
                resultIntent.putExtra("title", title);
                resultIntent.putExtra("content", content);
                resultIntent.putExtra("date", date);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
