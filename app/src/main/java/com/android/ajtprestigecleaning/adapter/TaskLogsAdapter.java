package com.android.ajtprestigecleaning.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.ajtprestigecleaning.R;
import com.android.ajtprestigecleaning.activities.LogsActivity;
import com.android.ajtprestigecleaning.model.AllJobsPojo.CheckList;
import com.android.ajtprestigecleaning.model.AllJobsPojo.Log;
import com.squareup.picasso.Picasso;

import java.util.List;


public class TaskLogsAdapter extends RecyclerView.Adapter<TaskLogsAdapter.ViewHolder> {

    List<Log> results;
    private final Context context;


    public TaskLogsAdapter(List<Log> results, Context context) {
        this.results = results;
        this.context = context;

    }


    @Override
    public TaskLogsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TaskLogsAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.logs_item, parent, false));

    }

    @Override
    public void onBindViewHolder(final TaskLogsAdapter.ViewHolder holder, final int position) {
        holder.note_text.setText(results.get(position).getText());

        if (!results.get(position).getImage().isEmpty()) {
            Picasso.with(context).load(results.get(position).getImage()).placeholder(R.drawable.demoprofile).error(R.drawable.demoprofile).into(holder.note_img);
        } else {
            holder.note_img.setVisibility(View.GONE);

        }

    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView note_text;
        ImageView note_img;

        public ViewHolder(View itemView) {
            super(itemView);
            note_text = itemView.findViewById(R.id.note_text);
            note_img = itemView.findViewById(R.id.log_img);


        }


    }


}
