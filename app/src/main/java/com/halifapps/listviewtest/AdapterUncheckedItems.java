package com.halifapps.listviewtest;

import android.content.Context;
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

import static com.halifapps.listviewtest.ChecklistActivity.adapter_checkedItems;
import static com.halifapps.listviewtest.ChecklistActivity.checkedItems;
import static com.halifapps.listviewtest.ChecklistActivity.uncheckedItems;

public class AdapterUncheckedItems extends ArrayAdapter<String> {
    public AdapterUncheckedItems(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.unchecked_row,parent,false);
        }

        final EditText editText = convertView.findViewById(R.id.edit_text_unchecked);
        final ImageButton delete_button = convertView.findViewById(R.id.delete_button);
        final CheckBox checkBox = convertView.findViewById(R.id.checkbox_unchecked);

        checkBox.setChecked(false);
        editText.setText(getItem(position));

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_DONE){
                    String input = textView.getText().toString();
                    if (input.length() > 0 && input.matches(".*[A-Za-z0-9].*")){
                        uncheckedItems.set(position,input);
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
                if(b){
                    checkedItems.add(getItem(position));
                    remove(getItem(position));
                    notifyDataSetChanged();
                    adapter_checkedItems.notifyDataSetChanged();
//                    checkBox.setChecked(false);
                }
            }
        });

        return convertView;
    }
}
