package com.halifapps.listviewtest.data;

import android.provider.BaseColumns;

public class ChecklistNotesContract {

    public static abstract class Checklist implements BaseColumns {
        public final static String TABLE_NAME = "checklist";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_CHECKED = "checked";
        public final static String COLUMN_UNCHECKED = "unchecked";
        public final static String COLUMN_TITLE = "title";
        public final static String COLUMN_NOTE = "note";
        public final static String COLUMN_TEXT = "text";

    }


}
