package com.halifapps.listviewtest;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.halifapps.listviewtest.data.ChecklistDBHelper;

import java.util.ArrayList;

import static com.halifapps.listviewtest.ChecklistActivity.ID;
import static com.halifapps.listviewtest.data.ChecklistNotesContract.Checklist.COLUMN_NOTE;
import static com.halifapps.listviewtest.data.ChecklistNotesContract.Checklist.COLUMN_TITLE;
import static com.halifapps.listviewtest.data.ChecklistNotesContract.Checklist.TABLE_NAME;
import static com.halifapps.listviewtest.data.ChecklistNotesContract.Checklist._ID;


public class MainActivity extends AppCompatActivity {
    Button newChecklist;
    Button newNote;
    public static final String QUERY = "SELECT * FROM " + TABLE_NAME;
    private ArrayList<String> titles;
    private AdapterGridView adapter;
    private ChecklistDBHelper dbHelper;
    private SQLiteDatabase db;
    private ListView listView;
    Button new_note;
    private Cursor cursor;
    private ArrayList<Integer> listItem_ids;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("My Notes & Lists");
        initialise();
        setListeners();
        if (savedInstanceState == null) {
            restoreData();
        }

    }

    private void initialise() {
        newChecklist = findViewById(R.id.new_checklist);
        newNote = findViewById(R.id.new_note);
        listView = findViewById(R.id.list_view);
        titles = new ArrayList<>();
        adapter = new AdapterGridView(this, R.layout.textview_main, titles);
        listView.setAdapter(adapter);
        dbHelper = new ChecklistDBHelper(this);
        listItem_ids = new ArrayList<>();
        new_note = findViewById(R.id.new_item);

    }

    private void setListeners() {
        new_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                   @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.new_checklist:
                                startActivity(new Intent(getApplicationContext(), ChecklistActivity.class));
                                return true;
                            case R.id.new_note:
                                startActivity(new Intent(getApplicationContext(), NoteActivity.class));
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.inflate(R.menu.new_note);
                popupMenu.show();
            }
        });

//        newChecklist.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), ChecklistActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        newNote.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), NoteActivity.class);
//                startActivity(intent);
//            }
//        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                cursor = db.rawQuery(QUERY, null);
                System.out.println("INSIDE on click listener. Position (i) = " + i + "\n cursor count = " + cursor.getCount());
                cursor.moveToPosition(i);

                String note = cursor.getString(cursor.getColumnIndex(COLUMN_NOTE));
                if (note != null) {
                    Intent note_intent = new Intent(getApplicationContext(), NoteActivity.class);
                    note_intent.putExtra(ID, i);
                    startActivity(note_intent);
                } else {
                    Intent checklist_intent = new Intent(MainActivity.this, ChecklistActivity.class);
                    checklist_intent.putExtra(ID, i);
                    startActivity(checklist_intent);
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), "Item deleted!", Toast.LENGTH_SHORT).show();
                db.delete(TABLE_NAME, _ID + " = " + listItem_ids.get(i), null);
                listItem_ids.remove(i);
                titles.remove(i);
                adapter.notifyDataSetChanged();
                return true;
            }
        });

    }
    

    private void restoreData() {
        db = dbHelper.getWritableDatabase();
        cursor = db.rawQuery(QUERY, null);
        while (cursor.moveToNext()) {
            listItem_ids.add(cursor.getInt(cursor.getColumnIndex(_ID)));
            titles.add(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
        }
        cursor.close();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        cursor = db.rawQuery(QUERY, null);
        int cursorCount = cursor.getCount();
        if (cursorCount > titles.size()) {
            cursor.moveToPosition(cursorCount - 1);
            titles.add(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
            listItem_ids.add(cursorCount);
            adapter.notifyDataSetChanged();
            System.out.println("ON RESTART NEW ITEM ADDED!!!,  titles = " + titles + " \nlistItems_Ids = " + listItem_ids);
        }
//        System.out.println("ON RESTART| titles = " + titles + " \nlistItems_Ids = " + listItem_ids);
    }
}
