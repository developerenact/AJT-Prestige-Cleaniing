package com.android.ajtprestigecleaning.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.ajtprestigecleaning.R;

import java.util.List;

public class CustomAdapter extends BaseAdapter {
    Context context;
    List<String> tasks;
    List<String> ids;
    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, List<String> tasks, List<String> ids) {
        this.context = applicationContext;
        this.tasks = tasks;
        this.ids = ids;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return ids.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.spinner_custom_layout, null);
        TextView names = (TextView) view.findViewById(R.id.spinner_textView);
        names.setText(tasks.get(i));
        return view;
    }
}
