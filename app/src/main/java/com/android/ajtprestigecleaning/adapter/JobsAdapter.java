package com.android.ajtprestigecleaning.adapter;

import android.app.Activity;
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
import com.android.ajtprestigecleaning.apiServices.ApiInterface;
import com.android.ajtprestigecleaning.apiServices.BaseUrl;
import com.android.ajtprestigecleaning.model.JobsPojo.Datum;
import com.android.ajtprestigecleaning.model.UpdateJobStatusPojo.UpdateJobStatusPojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class JobsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Datum> results;
    private final Context context;


    public JobsAdapter(List<Datum> results, Context context) {
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
            return new JobsAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.job_recycle_item, parent, false));
        } else {
            return new JobsAdapter.HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.header_recycle_item, parent, false));
        }


    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {


        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).headername.setText(results.get(position).getHeaderName());
            Typeface custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-Medium.ttf");
            ((HeaderViewHolder) holder).headername.setTypeface(custom_font);
        }
        else {

            if (results.get(position).getJobStatus().equals("1")) {
                ((ViewHolder) holder).first_btn.setVisibility(View.VISIBLE);
                ((ViewHolder) holder).second_btn.setVisibility(View.VISIBLE);
                ((ViewHolder) holder).first_btn.setText("End Job");
                ((ViewHolder) holder).second_btn.setText("Log Hours");
                ((ViewHolder) holder).first_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updateJobStatus(results.get(position).getId(), "5", ((ViewHolder) holder).first_btn, ((ViewHolder) holder).second_btn, holder.getAdapterPosition());
                    }
                });
            } else if (results.get(position).getJobStatus().equals("2")) {
                ((ViewHolder) holder).first_btn.setVisibility(View.VISIBLE);
                ((ViewHolder) holder).second_btn.setVisibility(View.VISIBLE);
                ((ViewHolder) holder).first_btn.setText("Decline");
                ((ViewHolder) holder).second_btn.setText("Un Available");
                ((ViewHolder) holder).first_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updateJobStatus(results.get(position).getId(), "4", ((ViewHolder) holder).first_btn, ((ViewHolder) holder).second_btn, holder.getAdapterPosition());
                    }
                });
            } else {
                ((ViewHolder) holder).first_btn.setVisibility(View.GONE);
                ((ViewHolder) holder).second_btn.setVisibility(View.GONE);
            }
            ((ViewHolder) holder).id.setText("Id number / #" + results.get(position).getId() + " /" + " " + results.get(position).getJobType());
            ((ViewHolder) holder).address.setText(results.get(position).getAddress());
            ((ViewHolder) holder).work.setText(results.get(position).getName());
            if(!results.get(position).getPrice().isEmpty()){
                ((ViewHolder) holder).jobprice.setText(results.get(position).getPrice());

            }
            else {
                ((ViewHolder) holder).jobprice.setText("N/A");

            }
            ((ViewHolder) holder).time.setText(convertDate(results.get(position).getCreatedAt(), "dd-MM-yyyy | hh.mm aa"));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, JobDetailActivity.class);
                    intent.putExtra("sampleObject", (Serializable) results.get(position));
                    //  intent.putExtra("sampleObject", (Serializable) results.get(position).getCheckList().get(position));
                    intent.putExtra("jobId", results.get(position).getId());
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


    public void updateJobStatus(String jobid, final String status, final TextView firstbtn, final TextView secondbtn, final int pos) {
        ((DashboardActivity)context).showProgress();
        if (((DashboardActivity) context).isNetworkAvailable()) {
            ApiInterface service = BaseUrl.CreateService(ApiInterface.class);
            Call<UpdateJobStatusPojo> call = service.updateJobStatus(jobid, "2", status);
            call.enqueue(new Callback<UpdateJobStatusPojo>() {
                @Override
                public void onResponse(Call<UpdateJobStatusPojo> call, Response<UpdateJobStatusPojo> response) {
                    if (response.isSuccessful()) {
                        ((DashboardActivity) context).hideProgress();
                        if (response.body().getStatus() == 0) {
                            firstbtn.setVisibility(View.GONE);
                            secondbtn.setVisibility(View.GONE);
                            // notifyItemRemoved(pos);
                            results.remove(pos);
                            notifyDataSetChanged();
                            if (context instanceof DashboardActivity) {
                                ((DashboardActivity)context).updateadapter();
                            }


                        } else {
                            Toast.makeText(context, context.getString(R.string.something_wrong), Toast.LENGTH_LONG).show();

                        }


                    } else {
                        ((DashboardActivity) context).hideProgress();
                        Toast.makeText(context, context.getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<UpdateJobStatusPojo> call, Throwable t) {
                    ((DashboardActivity) context).hideProgress();
                    Log.d("otp", t.getMessage());
                    Toast.makeText(context, context.getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            ((DashboardActivity) context).hideProgress();
            ((DashboardActivity) context).customDialog(context.getString(R.string.no_internet),context);

        }

    }



}
