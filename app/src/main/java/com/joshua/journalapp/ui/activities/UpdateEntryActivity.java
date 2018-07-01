package com.joshua.journalapp.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.joshua.journalapp.data.NoteDbHelper;
import com.joshua.journalapp.R;

public class UpdateEntryActivity extends AppCompatActivity {
    SQLiteDatabase sqLiteDatabase;
    NoteDbHelper noteDbHelper = new NoteDbHelper(UpdateEntryActivity.this);
    SharedPreferences sharedPreferences;
    long flag;
    EditText updateEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_entry);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        sqLiteDatabase = noteDbHelper.getWritableDatabase();
        sharedPreferences = getSharedPreferences("flag", Context.MODE_PRIVATE);

        flag = sharedPreferences.getLong("flag", 0);

        String strSQL = "SELECT  details FROM Notelist WHERE  _ID  =" + flag;
        Cursor cursor = sqLiteDatabase.rawQuery(strSQL, null);
        if (cursor.moveToFirst()) { // data?
            String data = cursor.getString(cursor.getColumnIndex("details"));
            cursor.close();
            updateEdit = (EditText) findViewById(R.id.update_details);
            updateEdit.setText(data);


        }
    }

    public void saveUpdate(View view) {
        String s = updateEdit.getText().toString();
        String strSQL = "UPDATE Notelist SET details = '" + s + "'WHERE  _ID   =" + flag + "";

        sqLiteDatabase.execSQL(strSQL);
        startActivity(new Intent(UpdateEntryActivity.this, BasicJournalActivity.class));
    }


    public void closeUpdate(View view) {
        startActivity(new Intent(UpdateEntryActivity.this, BasicJournalActivity.class));

    }


}
