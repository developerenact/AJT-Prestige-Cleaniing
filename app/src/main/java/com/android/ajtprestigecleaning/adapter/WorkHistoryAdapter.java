package com.android.ajtprestigecleaning.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.ajtprestigecleaning.R;
import com.android.ajtprestigecleaning.activities.DashboardActivity;
import com.android.ajtprestigecleaning.activities.JobDetailActivity;
import com.android.ajtprestigecleaning.activities.WorkHistoryActivity;
import com.android.ajtprestigecleaning.apiServices.ApiInterface;
import com.android.ajtprestigecleaning.apiServices.BaseUrl;
import com.android.ajtprestigecleaning.model.JobsPojo.Datum;
import com.android.ajtprestigecleaning.model.UpdateJobStatusPojo.UpdateJobStatusPojo;
import com.android.ajtprestigecleaning.util.Constants;
import com.android.ajtprestigecleaning.util.GpsTracker;

import java.io.Serializable;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Datum> results;
    private final Context context;


    public WorkHistoryAdapter(List<Datum> results, Context context) {
        this.results = results;
        this.context = context;


    }


    @Override
    public int getItemViewType(int position) {

        if (!results.get(position).getHeader()) {
            return 1;
        } else {
            return 2;
        }


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == 1) {
            return new WorkHistoryAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.job_recycle_item, parent, false));
        } else {
            return new WorkHistoryAdapter.HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.header_recycle_item, parent, false));
        }


    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {


        if (holder instanceof WorkHistoryAdapter.HeaderViewHolder) {
            ((WorkHistoryAdapter.HeaderViewHolder) holder).headername.setText(results.get(position).getHeaderName());
            Typeface custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-Medium.ttf");
            ((WorkHistoryAdapter.HeaderViewHolder) holder).headername.setTypeface(custom_font);
        } else {


            ((WorkHistoryAdapter.ViewHolder) holder).first_btn.setVisibility(View.GONE);
            ((WorkHistoryAdapter.ViewHolder) holder).second_btn.setVisibility(View.GONE);

            ((WorkHistoryAdapter.ViewHolder) holder).id.setText("Id number / #" + results.get(position).getId() + " /" + " " + results.get(position).getJobType());
            ((WorkHistoryAdapter.ViewHolder) holder).address.setText(results.get(position).getAddress());
            ((WorkHistoryAdapter.ViewHolder) holder).work.setText(results.get(position).getName());
            if (!results.get(position).getPrice().isEmpty()) {
                ((WorkHistoryAdapter.ViewHolder) holder).jobprice.setText("$" + results.get(position).getPrice());

            } else {
                ((WorkHistoryAdapter.ViewHolder) holder).jobprice.setText("N/A");

            }
            ((WorkHistoryAdapter.ViewHolder) holder).time.setText(convertDate(results.get(position).getCreatedAt(), "dd-MM-yyyy | hh.mm aa"));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, JobDetailActivity.class);
                    intent.putExtra("sampleObject", (Serializable) results.get(position));
                    //  intent.putExtra("sampleObject", (Serializable) results.get(position).getCheckList().get(position));
                    intent.putExtra("jobId", results.get(position).getId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView id, address, work, time, jobprice, first_btn, second_btn;

        public ViewHolder(View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id_number);
            address = itemView.findViewById(R.id.location);
            work = itemView.findViewById(R.id.desc);
            time = itemView.findViewById(R.id.date_time);
            jobprice = itemView.findViewById(R.id.tv_job_price);
            first_btn = itemView.findViewById(R.id.first_btn);
            second_btn = itemView.findViewById(R.id.second_btn);


        }
    }


    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView headername;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            headername = itemView.findViewById(R.id.headername);


        }
    }


    public static String convertDate(String dateInMilliseconds, String dateFormat) {
        return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
    }


}
