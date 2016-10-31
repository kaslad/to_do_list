package com.developers.notes;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Calendar;

import static com.developers.notes.R.id.tvTime;

public class EditNoteActivity extends AppCompatActivity {

    public static final String FILE_EXTENSION = "note";
    EditText noteContentText;
    EditText noteNameText;
    FloatingActionButton saveFab;
    DBHelper dbHelper;
    SQLiteDatabase notesDB;
    private static String fileName;
    Calendar call = Calendar.getInstance();
    int DIALOG_TIME = 1;
    int myHour = call.get(Calendar.HOUR_OF_DAY);
    int myMinute = call.get(Calendar.MINUTE);
    TextView tvTime, tvDate;
    int DIALOG_DATE = 1;
    int myYear = call.get(Calendar.YEAR);
    int myMonth = call.get(Calendar.MONTH);
    int myDay = call.get(Calendar.DAY_OF_MONTH);
    String time, date;
    Intent intent;
    int unicId;
    boolean open = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        noteContentText = (EditText) findViewById(R.id.noteContentText);
        noteNameText = (EditText) findViewById(R.id.noteNameText);
        dbHelper = new DBHelper(this);

        tvTime = (TextView) findViewById(R.id.tvTime);
        tvDate = (TextView) findViewById(R.id.tvDate);
        intent = new Intent(this, AlarmTask.class);

        notesDB = dbHelper.getReadableDatabase();
        Intent editNote = getIntent();
        noteNameText.setText(editNote.getStringExtra("notename"));
        fileName = editNote.getStringExtra("filename");
        unicId = editNote.getIntExtra("kolvo", 0);
        String m = "";
        m = m + unicId;
        String selection ="_id == ?";
        String[] selectionArgs = new String[] {m};
        Log.d("moh", "cursor id ana unic" + selection + unicId);
        Cursor c = notesDB.query(DBHelper.TABLE_NAME, null, selection, selectionArgs, null, null, null);
        if(c.moveToFirst()){
            date = c.getString(c.getColumnIndex(DBHelper.FILE_DATE_COLUMN));
            time = c.getString(c.getColumnIndex(DBHelper.FILE_TIME_COLUMN));
        }
        Log.d("moh", "date   time" + date + time);
        c.close();
        tvTime.setText(time);
        tvDate.setText(date);
        Log.d("moh", "row inserted, ID = " + unicId);
        openNote(fileName);
        //TODO get filename from database
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
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


    public void onclick (View view){
        showDialog(DIALOG_TIME);
    }
    public void onclick2(View view) {showDialog(DIALOG_DATE);}

    protected Dialog onCreateDialog(int id){
        if(id == DIALOG_TIME) {
            TimePickerDialog tpd = new TimePickerDialog(this, myCallBack, myHour, myMinute, true);
            return tpd;
        }
        if (id == DIALOG_DATE) {
            DatePickerDialog tpdDate = new DatePickerDialog(this, myCallBack2, myYear, myMonth, myDay);
            return tpdDate;
        }

        return super.onCreateDialog(id);
    }

    TimePickerDialog.OnTimeSetListener myCallBack = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            myHour = hourOfDay;
            myMinute = minute;
            tvTime.setText("Time is" + myHour + " my minute " + myMinute);
            call.set(Calendar.HOUR_OF_DAY, myHour);
            call.set(Calendar.MINUTE, myMinute);
            call.set(Calendar.SECOND, 0);
            call.set(Calendar.MILLISECOND, 0);
            open = true;
            TimeVisible();
        }

    };

    public void TimeVisible(){
        time = (myHour + " : " + myMinute);
        tvTime.setText(time);
        date = (myYear + "/" + myMonth + "/" + myDay);
        tvDate.setText(date);
    }

    DatePickerDialog.OnDateSetListener myCallBack2 = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myYear = year;
            myMonth = monthOfYear;
            myDay = dayOfMonth;
            call.set(Calendar.YEAR, year);
            call.set(Calendar.MONTH, monthOfYear);
            call.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            tvDate.setText(myDay + "/" + myMonth + "/" + myYear);

            open = true;
            TimeVisible();
        }
    };


    public void kre (){

        if (!open) {
            if (!date.equals("date")) {
                getDate();
                AlarmTask am = new AlarmTask(this, call);
                am.setText(noteContentText.getText().toString(), unicId);
                am.run();
            }
        }
        else {
            AlarmTask am = new AlarmTask(this, call);
            ContentValues cv = new ContentValues();
            notesDB = dbHelper.getWritableDatabase();
            cv.put(DBHelper.FILE_DATE_COLUMN, date);
            cv.put(DBHelper.FILE_TIME_COLUMN, time);
            String z = "";
            z = z + unicId;
            notesDB.update(DBHelper.TABLE_NAME, cv, DBHelper.KEY_ID + "= ?", new String[] {z});
            am.setText(noteContentText.getText().toString(), unicId);
            am.run();
        }
    }

    public void onclickDone (View view) {
        if (!noteContentText.getText().toString().isEmpty()) {
            saveNote(fileName);
        }
        kre();;
        super.onBackPressed();
    }

    public void getDate(){
        String[] parts = time.split(" : ");
        call.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
        call.set(Calendar.MINUTE, Integer.parseInt(parts[1]));
        call.set(Calendar.SECOND, 0);
        call.set(Calendar.MILLISECOND, 0);
        String[] parts2 = date.split("/");
        call.set(Calendar.YEAR, Integer.parseInt(parts2[0]));
        call.set(Calendar.MONTH, Integer.parseInt(parts2[1]));
        call.set(Calendar.DAY_OF_MONTH, Integer.parseInt(parts2[2]));

    }
    public void onclickDel (View view) {

       if (!date.equals("date")) {
           getDate();
           AlarmTask am = new AlarmTask(this, call);
           am.delAl(noteContentText.getText().toString(), unicId);
        }

        notesDB.delete(DBHelper.TABLE_NAME, DBHelper.KEY_ID + "=" + unicId, null);


        super.onBackPressed();

    }
}
