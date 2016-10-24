package com.developers.notes;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreateNoteActivity extends AppCompatActivity {

    public static final String FILE_EXTENSION = "note";
    EditText noteContentText;
    EditText noteNameText;
    DBHelper dbHelper;
    SQLiteDatabase notesDB;
    Intent intent;
    Calendar call = Calendar.getInstance();
    int DIALOG_TIME = 1;
    int myHour = call.get(Calendar.HOUR_OF_DAY);
    int myMinute = call.get(Calendar.MINUTE);
    TextView tvTime, tvDate;
    int DIALOG_DATE = 2;
    int myYear = call.get(Calendar.YEAR);
    int myMonth = call.get(Calendar.MONTH);
    int myDay = call.get(Calendar.DAY_OF_MONTH);
    int mCount;
    boolean open = false;
    String time = "time", date = "date";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        noteContentText = (EditText) findViewById(R.id.noteContentText);
        noteNameText = (EditText) findViewById(R.id.noteNameText);
        dbHelper = new DBHelper(this);

        tvTime = (TextView) findViewById(R.id.tvTime);
        tvDate = (TextView) findViewById(R.id.tvDate);
        intent = new Intent(this, AlarmTask.class);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void onclick (View view){
        showDialog(DIALOG_TIME);
    }
    public void onclickDate(View view) {
        showDialog(DIALOG_DATE);
    }

    protected Dialog onCreateDialog(int id){
        if(id == DIALOG_TIME) {
            TimePickerDialog tpd = new TimePickerDialog(this, myCallBack, myHour, myMinute, true);
            return tpd;
        }
        if (id == DIALOG_DATE) {
            DatePickerDialog dpd = new DatePickerDialog(this, myCallBack2, myYear, myMonth, myDay);
            return dpd;
        }

        return super.onCreateDialog(id);
    }

    TimePickerDialog.OnTimeSetListener myCallBack = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            myHour = hourOfDay;
            myMinute = minute;

            call.set(Calendar.HOUR_OF_DAY, myHour);
            call.set(Calendar.MINUTE, myMinute);
            call.set(Calendar.SECOND, 0);
            call.set(Calendar.MILLISECOND, 0);

            open = true;

            TimeVisible();;
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

        AlarmTask am = new AlarmTask(this, call);
        am.setText(noteContentText.getText().toString(), mCount, noteNameText.getText().toString());
        am.run();
    }


    private String saveNote() {
        Date date = new Date();
        SimpleDateFormat namePattern = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String fileName = namePattern.format(date);
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
        return fileName;
    }
    final String LOG_TAG = "myLogs";
    public void onBackPressed() {
        if (!noteContentText.getText().toString().isEmpty()) {
            ContentValues cv = new ContentValues();
            notesDB = dbHelper.getWritableDatabase();
            String noteName = noteNameText.getText().toString();
            String fileName = saveNote();
            cv.put(DBHelper.FILE_NAME_COLUMN, fileName);
            cv.put(DBHelper.NOTE_NAME_COLUMN, noteName);
            cv.put(DBHelper.FILE_DATE_COLUMN, date);
            cv.put(DBHelper.FILE_TIME_COLUMN, time);
           // notesDB.insert(DBHelper.TABLE_NAME, null, cv);
            mCount = (int) notesDB.insert(DBHelper.TABLE_NAME, null, cv);
            Log.d(LOG_TAG, "row inserted, ID = " + mCount);
            dbHelper.close();
            notesDB.close();
        }
        if (open) {
            kre();
        }
        super.onBackPressed();
    }
}



