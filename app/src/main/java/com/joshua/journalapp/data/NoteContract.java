package com.joshua.journalapp.data;

import android.provider.BaseColumns;

public class NoteContract{
    public static final class NotelistEntry implements BaseColumns {

        //table name and each of the db columns
        public static final String TABLE_NAME = "Notelist";
        public static final String COLUMN_DETAILS = "details";
    }

}
