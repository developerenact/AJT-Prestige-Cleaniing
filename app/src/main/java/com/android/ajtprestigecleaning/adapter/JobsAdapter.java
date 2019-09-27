package com.android.ajtprestigecleaning.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.ajtprestigecleaning.R;
import com.android.ajtprestigecleaning.activities.JobDetailActivity;
import com.android.ajtprestigecleaning.model.JobDetailPojo.Task;
import com.android.ajtprestigecleaning.model.JobListPojo.Datum;
import com.android.ajtprestigecleaning.model.JobModel;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.paperdb.Paper;


public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.ViewHolder> {

    List<Datum> results;
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

    public JobsAdapter(List<Datum> results, Context context) {
        this.results = results;
        this.context = context;


    }


    @Override
    public JobsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == 1) {
            return new JobsAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.job_recycle_item, parent, false));
        } else {
            return new JobsAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.job_recycle_item, parent, false));
        }


    }

    @Override
    public void onBindViewHolder(final JobsAdapter.ViewHolder holder, final int position) {
        holder.id.setText("Id number / #"+results.get(position).getId());
        holder.address.setText(results.get(position).getAddress());
        holder.work.setText(results.get(position).getName());
        holder.time.setText(convertDate(results.get(position).getCreatedAt(),"dd-MM-yyyy | hh.mm aa"));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, JobDetailActivity.class);
                intent.putExtra("sampleObject", (Serializable) results.get(position));
                intent.putExtra("jobId",results.get(position).getId());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView header, id, address, work, time;

        public ViewHolder(View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id_number);
            address = itemView.findViewById(R.id.location);
            work = itemView.findViewById(R.id.desc);
            time = itemView.findViewById(R.id.date_time);


        }
    }

    public static String convertDate(String dateInMilliseconds, String dateFormat) {
        return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
    }



}
