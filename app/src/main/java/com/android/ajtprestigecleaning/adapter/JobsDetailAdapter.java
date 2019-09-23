package com.android.ajtprestigecleaning.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.ajtprestigecleaning.R;
import com.android.ajtprestigecleaning.activities.JobDetailActivity;
import com.android.ajtprestigecleaning.model.JobDetailPojo.Task;
import com.android.ajtprestigecleaning.model.JobModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class JobsDetailAdapter extends RecyclerView.Adapter<JobsDetailAdapter.ViewHolder> {

    List<Task> results;
    private final Context context;


    public JobsDetailAdapter(List<Task> results, Context context) {
        this.results = results;
        this.context = context;


    }


    @Override
    public JobsDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new JobsDetailAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.timeline_itemview, parent, false));

    }

    @Override
    public void onBindViewHolder(final JobsDetailAdapter.ViewHolder holder, final int position) {
        holder.task.setText(results.get(position).getName());



    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView task;

        public ViewHolder(View itemView) {
            super(itemView);
            task = itemView.findViewById(R.id.task);



        }
    }




}
