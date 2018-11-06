package com.halifapps.listviewtest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.halifapps.listviewtest.data.ChecklistDBHelper;

import java.util.ArrayList;

import static com.halifapps.listviewtest.data.ChecklistNotesContract.Checklist.COLUMN_CHECKED;
import static com.halifapps.listviewtest.data.ChecklistNotesContract.Checklist.COLUMN_TITLE;
import static com.halifapps.listviewtest.data.ChecklistNotesContract.Checklist.COLUMN_UNCHECKED;
import static com.halifapps.listviewtest.data.ChecklistNotesContract.Checklist.TABLE_NAME;
import static com.halifapps.listviewtest.data.ChecklistNotesContract.Checklist._ID;


public class ChecklistActivity extends AppCompatActivity {
    public static final String CHECKED = "checked";
    public static final String UNCHECKED = "unchecked";
    public static final String ID = "id";
    ListView itemsChecked;
    ListView itemsUnchecked;
    static AdapterUncheckedItems adapter_uncheckedItems;
    static AdapterCheckedItems adapter_checkedItems;
    EditText listItem;
    EditText title;
    TextView title_text;
    static ArrayList<String> checkedItems;
    static ArrayList<String> uncheckedItems;
    int itemID = -2;
    private ChecklistDBHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checklist_activity);
        setTitle("Checklist");
        initialise();
        setListeners();

        if (getIntent().hasExtra(ID)) {

            Cursor cursor = db.rawQuery(MainActivity.QUERY, null);
            itemID = getIntent().getIntExtra(ID, -2);
            cursor.moveToPosition(itemID);
            setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
            checkedItems.addAll(stringToArrayList(cursor.getString(cursor.getColumnIndex(COLUMN_CHECKED))));
            uncheckedItems.addAll(stringToArrayList(cursor.getString(cursor.getColumnIndex(COLUMN_UNCHECKED))));
            adapter_checkedItems.notifyDataSetChanged();
            adapter_uncheckedItems.notifyDataSetChanged();
            title_text.setText(getTitle());
            title_text.setVisibility(View.VISIBLE);

            title.setText(getTitle().toString().trim());
            listItem.setVisibility(View.VISIBLE);
            listItem.requestFocus();
            cursor.close();
        }
    }

    private void initialise() {
        listItem = findViewById(R.id.list_item);
        itemsUnchecked = findViewById(R.id.unchecked);
        itemsChecked = findViewById(R.id.checked);
        title = findViewById(R.id.title);
//        title_text = findViewById(R.id.note_title);
        checkedItems = new ArrayList<>();
        uncheckedItems = new ArrayList<>();
        adapter_checkedItems = new AdapterCheckedItems(this, R.layout.checked_row, checkedItems);
        adapter_uncheckedItems = new AdapterUncheckedItems(this, R.layout.unchecked_row, uncheckedItems);
        itemsUnchecked.setAdapter(adapter_uncheckedItems);
        itemsChecked.setAdapter(adapter_checkedItems);
        dbHelper = new ChecklistDBHelper(this);
        db = dbHelper.getWritableDatabase();
    }

    private void setListeners() {

        title.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    String text = textView.getText().toString().trim();
                    if (text.length() > 0) {
                        setTitle(text);
                        title.setText(text);
                        listItem.setVisibility(View.VISIBLE);
                        listItem.requestFocus();
                    } else {
                        title.setText("");
                        Toast.makeText(getApplicationContext(), "We really need this Title :-)",
                                Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }
        });

        listItem.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    String input = textView.getText().toString().trim();
                    if (input.length() > 0) {
                        uncheckedItems.add(input);
                        adapter_uncheckedItems.notifyDataSetChanged();
                        listItem.setText("");
                        listItem.requestFocus();
                    } else {
                        listItem.setText("");
                        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(listItem.getWindowToken(), 0);
                    }
                    return true;
                }

                return false;
            }
        });

//        title_text.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                title.setText(((TextView)view).getText().toString().trim());
//                title.setVisibility(View.VISIBLE);
//                title_text.setVisibility(View.GONE);
//                title.requestFocus();
//
//            }
//        });

//        itemsChecked.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                String text = adapterView.getItemAtPosition(i).toString();
//                uncheckedItems.add(0, text);
//                checkedItems.remove(text);
//                adapter_checkedItems.notifyDataSetChanged();
//                adapter_uncheckedItems.notifyDataSetChanged();
//            }
//        });
//
//        itemsUnchecked.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                String text = adapterView.getItemAtPosition(i).toString();
//                checkedItems.add(0, text);
//                uncheckedItems.remove(text);
//                adapter_uncheckedItems.notifyDataSetChanged();
//                adapter_checkedItems.notifyDataSetChanged();
//            }
//        });
//
//        itemsChecked.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                checkedItems.remove(i);
//                adapter_checkedItems.notifyDataSetChanged();
//                return true;
//            }
//        });
//
//        itemsUnchecked.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                uncheckedItems.remove(i);
//                adapter_uncheckedItems.notifyDataSetChanged();
//                return true;
//            }
//        });
    }


    @Override
    public void onBackPressed() {

        ContentValues values = new ContentValues();
        values.put(COLUMN_UNCHECKED, arraylistToString(uncheckedItems));
        values.put(COLUMN_CHECKED, arraylistToString(checkedItems));
        Cursor cursor = db.rawQuery(MainActivity.QUERY, null);

        if (itemID != -2) {
            cursor.moveToPosition(itemID);
            db.update(TABLE_NAME, values, _ID + " =" + cursor.getInt(cursor.getColumnIndex(_ID)), null);
        } else {
            String title = getTitle().toString();
            if (!title.equals("Checklist")) {
                values.put(COLUMN_TITLE, getTitle().toString());
                itemID = (int) db.insert(TABLE_NAME, null, values);
                System.out.println("AFTER INSERT | title = " + getTitle());
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


    private ArrayList<String> stringToArrayList(String str) {
        ArrayList<String> list = new ArrayList<>();
        for (String s : str.split(",")) {
            if (s.trim().length() > 0) {
                list.add(s.trim());
            }
        }
        return list;
    }

    private String arraylistToString(ArrayList<String> list) {
        StringBuilder stringBuilder = new StringBuilder();
        if (!list.isEmpty()) {
            for (String s : list) {
                stringBuilder.append(s + ",");
            }
        }
        return stringBuilder.toString();
    }
}