package com.developers.notes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import static com.developers.notes.DBHelper.TABLE_NAME;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static SparseIntArray ids = new SparseIntArray();
    FloatingActionButton fab;
    ListView noteList;
    NoteAdapter noteAdapter;
    Cursor cursor;
    DBHelper dbHelper;
    SQLiteDatabase notesDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        noteList = (ListView) findViewById(R.id.noteList);
        dbHelper = new DBHelper(this);
        notesDB = dbHelper.getReadableDatabase();
        cursor = notesDB.query(TABLE_NAME, null, null, null, null, null, null);
        noteAdapter = new NoteAdapter(MainActivity.this, cursor, 0);
        noteList.setAdapter(noteAdapter);
        noteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor = notesDB.query(DBHelper.TABLE_NAME, null, null, null, null, null, null);
                cursor.moveToPosition(position);
                String fileName = cursor.getString(cursor.getColumnIndex(DBHelper.FILE_NAME_COLUMN));
                String noteName = cursor.getString(cursor.getColumnIndex(DBHelper.NOTE_NAME_COLUMN));
                Intent editNote = new Intent(MainActivity.this, EditNoteActivity.class);
                editNote.putExtra("filename", fileName);
                editNote.putExtra("notename", noteName);
                int unicId = (int) id;
                editNote.putExtra("kolvo", unicId);
                startActivity(editNote);
            }
        });

        View.OnClickListener fabOnClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createNote = new Intent(MainActivity.this, CreateNoteActivity.class);
                startActivity(createNote);
            }
        };
        fab.setOnClickListener(fabOnClick);

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        noteAdapter.notifyDataSetChanged();
        noteList.setAdapter(noteAdapter);
    }

    protected void onRestart() {
        super.onRestart();
        noteAdapter.notifyDataSetChanged();
        noteList.setAdapter(noteAdapter);
    }

    protected void onStart() {
        super.onStart();
        noteAdapter.notifyDataSetChanged();
        noteList.setAdapter(noteAdapter);
    }

    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
            super.onBackPressed();
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
