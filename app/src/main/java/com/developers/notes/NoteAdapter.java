package com.developers.notes;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.developers.notes.CreateNoteActivity.FILE_EXTENSION;

/**
 * Created by avispa on 17.10.2016.
 */

public class NoteAdapter extends CursorAdapter {
    Context ctx;
    LayoutInflater lInflater;
    static int count = 0;
    final String LOG_TAG = "myLogs";

    public NoteAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        Log.d(LOG_TAG, "note adapter created");
        lInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        Log.d(LOG_TAG, "inflater created");
    }

    public void bindView(View view, Context context, Cursor cursor) {
        TextView itemNoteName = (TextView) view.findViewById(R.id.itemNoteName);
        TextView itemNoteContent = (TextView) view.findViewById(R.id.itemNoteContent);
        long id = cursor.getLong(cursor.getColumnIndex("_id"));
        MainActivity.ids.put(count, (int) id);
        ++count;
        String noteName = cursor.getString(cursor.getColumnIndex(DBHelper.NOTE_NAME_COLUMN));
        String fileName = cursor.getString(cursor.getColumnIndex(DBHelper.FILE_NAME_COLUMN));
        try {
            openNote(fileName, context, itemNoteContent);
        } catch (Exception e) {}
        itemNoteName.setText(noteName);
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return lInflater.inflate(R.layout.item, parent, false);
    }

    private void openNote(String fileName, Context context, TextView itemNoteContent) {
        try {
            InputStream inputStream = context.openFileInput(fileName + "." + FILE_EXTENSION);

            if (inputStream != null) {
                InputStreamReader isr = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(isr);
                String line;
                StringBuilder builder = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
//                builder.append("\b");
                inputStream.close();
                itemNoteContent.setText(builder.toString());
            }
        } catch (Throwable t) {
            // Don`t do anything
        }
    }
}
