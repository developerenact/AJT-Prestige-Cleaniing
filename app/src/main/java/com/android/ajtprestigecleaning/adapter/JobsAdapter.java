package com.android.ajtprestigecleaning.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.android.ajtprestigecleaning.R;
import com.android.ajtprestigecleaning.activities.JobDetailActivity;
import com.android.ajtprestigecleaning.model.JobModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import io.paperdb.Paper;



public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.ViewHolder> {

    ArrayList<JobModel> joblist;
    private final Context context;

    @Override
    public int getItemViewType(int position) {

      /*  if()
        {
            return 1;
        }

            else {
                return 2;
            }*/

      return 1;

    }

    public JobsAdapter( ArrayList<JobModel> joblist,Context context) {
        this.joblist=joblist;
        this.context = context;


    }


    @Override
    public JobsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == 1) {
            return new JobsAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.job_recycle_item, parent, false));
        }

        else {
            return new JobsAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.job_recycle_item, parent, false));
        }


    }

    @Override
    public void onBindViewHolder(final JobsAdapter.ViewHolder holder, final int position) {
        holder.id.setText(joblist.get(position).getId());
        holder.address.setText(joblist.get(position).getAddress());
        holder.work.setText(joblist.get(position).getWork());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, JobDetailActivity.class);
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return joblist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView header,id, address, work, time;

        public ViewHolder(View itemView) {
            super(itemView);
            id=itemView.findViewById(R.id.id_number);
            address=itemView.findViewById(R.id.location);
            work=itemView.findViewById(R.id.desc);


        }
    }

    private String TimeStumpToDate(Long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh.mm aa");
        String dateString = formatter.format(new Date((time)));
        return (dateString);

    }




}