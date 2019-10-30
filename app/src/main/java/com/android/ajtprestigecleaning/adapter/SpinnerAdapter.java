package com.android.ajtprestigecleaning.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.ajtprestigecleaning.R;
import com.android.ajtprestigecleaning.model.JobsPojo.Task;

import java.util.List;

public class SpinnerAdapter extends BaseAdapter {
    Context context;
    List<Task> task;
    List<String> checklistId;
    LayoutInflater inflter;

    public SpinnerAdapter(Context applicationContext,List<Task> task,List<String> checklistId) {
        this.context = applicationContext;
        this.task=task;
        this.checklistId=checklistId;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return task.size();
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
        names.setText(task.get(i).getName());
        view.setPadding(0, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
        return view;
    }
}
