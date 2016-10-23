package com.developers.notes;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class EditNoteActivity extends AppCompatActivity {

    public static final String FILE_EXTENSION = "note";
    EditText noteContentText;
    EditText noteNameText;
    FloatingActionButton saveFab;
    DBHelper dbHelper;
    SQLiteDatabase notesDB;
    private static String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        noteContentText = (EditText) findViewById(R.id.noteContentText);
        noteNameText = (EditText) findViewById(R.id.noteNameText);
        dbHelper = new DBHelper(this);
        notesDB = dbHelper.getReadableDatabase();
        Intent editNote = getIntent();
        noteNameText.setText(editNote.getStringExtra("notename"));
        fileName = editNote.getStringExtra("filename");
        openNote(fileName);
        //TODO get filename from database
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void saveNote(String fileName) {
        try {
            OutputStream outputStream = openFileOutput(fileName + "." + FILE_EXTENSION, 0);
            OutputStreamWriter osw = new OutputStreamWriter(outputStream);
            osw.write(noteContentText.getText().toString());
            osw.close();
        } catch (Throwable t) {
            Log.d("TAG", t.toString());
            Toast.makeText(getApplicationContext(),
                    "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
        }

    }

    private void openNote(String fileName) {
        try {
            InputStream inputStream = openFileInput(fileName + "." + FILE_EXTENSION);

            if (inputStream != null) {
                InputStreamReader isr = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(isr);
                String line;
                StringBuilder builder = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }

                inputStream.close();
                noteContentText.setText(builder.toString());
            }
        } catch (Throwable t) {
            Toast.makeText(getApplicationContext(),
                    "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void onBackPressed() {
        if (!noteContentText.getText().toString().isEmpty()) {
            saveNote(fileName);
        }
        super.onBackPressed();
    }
}
