package com.halifapps.listviewtest;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

public class AdapterGridView extends ArrayAdapter<String> {

    int color = 0;
    String[] colors = {"#BBDEFB","#80CBC4","#81D4FA","#1DE9B6","#A5D6A7","#FFCCBC","#D7CCC8","#CFD8DC","#90A4AE"};


    public AdapterGridView(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = super.getView(position, convertView, parent);


        view.setBackgroundColor(Color.parseColor(colors[color]));
        color++;
        if (color == colors.length) {
            color = 0;
        }



        return view;
    }

}
