package com.joshua.journalapp.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.joshua.journalapp.data.NoteContract;
import com.joshua.journalapp.data.NoteDbHelper;
import com.joshua.journalapp.R;
import com.joshua.journalapp.ui.adapters.MyAdapter;


public class BasicJournalActivity extends AppCompatActivity implements MyAdapter.ListItemClickListener {

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mlistener;

    RecyclerView recyclerView;
    MyAdapter adapter;
    SQLiteDatabase sqLiteDatabase;
    NoteDbHelper noteDbHelper = new NoteDbHelper(BasicJournalActivity.this);
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_journal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                startActivity(new Intent(BasicJournalActivity.this, AddEntryActivity.class));
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mlistener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                }

            }
        };


        recyclerView = findViewById(R.id.list_of_entries);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sqLiteDatabase = noteDbHelper.getWritableDatabase();
        Cursor cursor = getAllNotes();


        adapter = new MyAdapter(cursor, BasicJournalActivity.this, BasicJournalActivity.this);
        recyclerView.setAdapter(adapter);


        sharedPreferences = getSharedPreferences("flag", Context.MODE_PRIVATE);


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {

                long id = (long) viewHolder.itemView.getTag();

                removeNote(id);

                adapter.swapCursor(getAllNotes());
            }

        }).attachToRecyclerView(recyclerView);

    }


    @Override
    public void onListItemClick(int clickedItemIndex, long id) {

        sharedPreferences.edit();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("flag", id);
        editor.apply();

        startActivity(new Intent(BasicJournalActivity.this, UpdateEntryActivity.class));

    }

    //get all notes
    public Cursor getAllNotes() {
        return sqLiteDatabase.query(
                NoteContract.NotelistEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                NoteContract.NotelistEntry._ID
        );
    }


    private boolean removeNote(long id) {
        return sqLiteDatabase.delete(NoteContract.NotelistEntry.TABLE_NAME, NoteContract.NotelistEntry._ID + "=" + id, null) > 0;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.actionLogout) {
            mAuth.signOut();
            startActivity(new Intent(BasicJournalActivity.this, SignInActivity.class));
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mlistener);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mlistener != null) {
            mAuth.removeAuthStateListener(mlistener);
        }
    }

    //override on resume to update
    @Override
    public void onResume() {

        super.onResume();
        adapter.notifyItemRangeChanged(0, adapter.getItemCount());
    }
}
