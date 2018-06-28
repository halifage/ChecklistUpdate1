package com.halifapps.listviewtest;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.halifapps.listviewtest.data.ChecklistDBHelper;

import static com.halifapps.listviewtest.ChecklistActivity.ID;
import static com.halifapps.listviewtest.data.ChecklistNotesContract.Checklist.COLUMN_NOTE;
import static com.halifapps.listviewtest.data.ChecklistNotesContract.Checklist.COLUMN_TITLE;
import static com.halifapps.listviewtest.data.ChecklistNotesContract.Checklist.TABLE_NAME;
import static com.halifapps.listviewtest.data.ChecklistNotesContract.Checklist._ID;

public class NoteActivity extends AppCompatActivity {

    EditText note_title;
    EditText note_body;
    String title;
    int itemID = -2;
    private ChecklistDBHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_activity);
        setTitle("Note");
        initialise();
        setListeners();

        if (getIntent().hasExtra(ID)) {

            Cursor cursor = db.rawQuery(MainActivity.QUERY, null);
            itemID = getIntent().getIntExtra(ID, -2);
            cursor.moveToPosition(itemID);
            setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)).trim());
            note_title.setText(getTitle().toString().trim());
            note_body.setText(cursor.getString(cursor.getColumnIndex(COLUMN_NOTE)).trim());
            cursor.close();
        }


    }

    private void initialise() {
        dbHelper = new ChecklistDBHelper(this);
        db = dbHelper.getWritableDatabase();
        note_title = findViewById(R.id.note_title);
        note_body = findViewById(R.id.note_body);
    }

    private void setListeners() {

        note_title.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    String text = textView.getText().toString().trim();
                    if (text.length() > 0 && text.matches(".*[A-Za-z0-9].*")) {
                        setTitle(text);
                        note_title.setText(text);
                        note_body.requestFocus();
                    } else {
                        note_title.setText("");
                        Toast.makeText(getApplicationContext(), "We really need this Title :-)",
                                Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public void onBackPressed() {

        ContentValues values = new ContentValues();
        String note = note_body.getText().toString();

        values.put(COLUMN_NOTE, note_body.getText().toString());

        Cursor cursor = db.rawQuery(MainActivity.QUERY, null);

        if (!values.get(COLUMN_NOTE).toString().isEmpty()) {
            if (itemID != -2) {
                cursor.moveToPosition(itemID);
                db.update(TABLE_NAME, values, _ID + " =" + cursor.getInt(cursor.getColumnIndex(_ID)), null);
            } else {
                title = note_title.getText().toString();
                if (!title.equals("Checklist")) {
                    values.put(COLUMN_TITLE, title);
                    itemID = (int) db.insert(TABLE_NAME, null, values);
                    System.out.println("AFTER INSERT | itemID " + itemID);
                }
            }
        }
        cursor.close();
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


}
