package com.halifapps.listviewtest;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import static com.halifapps.listviewtest.ChecklistActivity.adapter_uncheckedItems;
import static com.halifapps.listviewtest.ChecklistActivity.checkedItems;
import static com.halifapps.listviewtest.ChecklistActivity.uncheckedItems;

public class AdapterCheckedItems extends ArrayAdapter<String> {

    public AdapterCheckedItems(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.checked_row, parent, false);
        }

        final ImageButton delete_button = convertView.findViewById(R.id.delete_button);
        final CheckBox checkBox = convertView.findViewById(R.id.checkbox_checked);
        EditText editText = convertView.findViewById(R.id.edit_text_checked);

        checkBox.setChecked(true);
        editText.setText(getItem(position));
        editText.setPaintFlags(editText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        editText.setTextColor(Color.GRAY);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_DONE){
                    String input = textView.getText().toString();
                    if (input.length() > 0 && input.matches(".*[A-Za-z0-9].*")){
                        checkedItems.set(position,input);
                        notifyDataSetChanged();
                    }
                    return true;
                }
                return false;
            }
        });

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    delete_button.setVisibility(View.VISIBLE);
                }else{
                    delete_button.setVisibility(View.INVISIBLE);
                }
            }
        });

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remove(getItem(position));
                notifyDataSetChanged();
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b){
                    uncheckedItems.add(getItem(position));
                    remove(getItem(position));
                    notifyDataSetChanged();
                    adapter_uncheckedItems.notifyDataSetChanged();
//                    checkBox.setChecked(true);
                }
            }
        });

        return convertView;
    }
}

