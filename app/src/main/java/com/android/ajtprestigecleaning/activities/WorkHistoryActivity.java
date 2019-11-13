package com.android.ajtprestigecleaning.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ajtprestigecleaning.R;
import com.android.ajtprestigecleaning.adapter.JobsAdapter;
import com.android.ajtprestigecleaning.adapter.WorkHistoryAdapter;
import com.android.ajtprestigecleaning.apiServices.ApiInterface;
import com.android.ajtprestigecleaning.apiServices.BaseUrl;
import com.android.ajtprestigecleaning.model.JobsPojo.Datum;
import com.android.ajtprestigecleaning.model.JobsPojo.JobsPojo;
import com.android.ajtprestigecleaning.util.Constants;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkHistoryActivity extends BaseActivity {
    ImageView back;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    WorkHistoryAdapter adapter;
    TextView header;
    List<Datum> joblist;
    List<String> type;
    ArrayList<Datum> jobsWithHeader;
    Context context;
    AVLoadingIndicatorView progressBar;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        back=findViewById(R.id.back);
        recyclerView = findViewById(R.id.history_recyclerview);
        type = new ArrayList<>();
        joblist = new ArrayList<>();
        jobsWithHeader = new ArrayList<>();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        getjobs(0,WorkHistoryActivity.this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void getjobs(int state, final Context context) {
        ((WorkHistoryActivity) context).showProgress();
        if (((WorkHistoryActivity) context).isNetworkAvailable()) {
            ApiInterface service = BaseUrl.CreateService(ApiInterface.class);

            String userid=  Paper.book().read(Constants.USERID, "2");
            Call<JobsPojo> call = service.getjobs(userid, state);
            call.enqueue(new Callback<JobsPojo>() {
                @Override
                public void onResponse(Call<JobsPojo> call, Response<JobsPojo> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 0) {

                            ((WorkHistoryActivity) context).hideProgress();
                            if (response.body().getData().size() > 0) {

                            } else {
                                ((WorkHistoryActivity) context).customDialog(context.getString(R.string.no_jobs_available), WorkHistoryActivity.this);
                            }
                            joblist = response.body().getData();


                            Collections.sort(joblist, new Comparator<Datum>() {
                                @Override
                                public int compare(Datum o1, Datum o2) {
                                    return o1.getJobStatus().compareTo(o2.getJobStatus());
                                }

                            });


                            List<Datum> Past_Jobs = new ArrayList<>();
                            List<Datum> Rejected_Jobs = new ArrayList<>();
                            List<Datum> Completed = new ArrayList<>();


// 0 -> All Jobs
// 1 -> In progress
// 2 -> Upcoming Jobs
// 3 -> Past Jobs
// 4 -> Rejected Jobs
// 5 -> Completed


                            if (joblist.size() > 0) {

                                for (int i = 0; i < joblist.size(); i++) {

                                     if (joblist.get(i).getJobStatus().equalsIgnoreCase(String.valueOf(Constants.PAST))) {
                                        Past_Jobs.add(joblist.get(i));
                                    } else if (joblist.get(i).getJobStatus().equalsIgnoreCase(String.valueOf(Constants.REJECTED))) {
                                        Rejected_Jobs.add(joblist.get(i));
                                    } else if (joblist.get(i).getJobStatus().equalsIgnoreCase(String.valueOf(Constants.COMPLETED))) {
                                        Completed.add(joblist.get(i));
                                    }
                                }
                                joblist.clear();
                            }



                            if (Past_Jobs.size() > 0) {
                                Datum datum = new Datum();
                                datum.setHeader(true);
                                datum.setHeaderName("Past");
                                joblist.add(datum);
                                joblist.addAll(Past_Jobs);

                            }
                            if (Rejected_Jobs.size() > 0) {
                                Datum datum = new Datum();
                                datum.setHeader(true);
                                datum.setHeaderName("Rejected");
                                joblist.add(datum);
                                joblist.addAll(Rejected_Jobs);

                            }


                            if (Completed.size() > 0) {
                                Datum datum = new Datum();
                                datum.setHeader(true);
                                datum.setHeaderName("Completed");
                                joblist.add(datum);
                                joblist.addAll(Completed);

                            }


                            adapter = new WorkHistoryAdapter(joblist, WorkHistoryActivity.this);
                            recyclerView.setAdapter(adapter);

                        } else {
                            ((WorkHistoryActivity) context).hideProgress();

                        }

                    } else {

                        ((WorkHistoryActivity) context).hideProgress();
                        Toast.makeText(context, getApplicationContext().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();


                    }
                }

                @Override
                public void onFailure(Call<JobsPojo> call, Throwable t) {

                    ((WorkHistoryActivity) context).hideProgress();
                    Log.d("otp", t.getMessage());
                    Toast.makeText(context, getApplicationContext().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
                }
            });
        } else {

            ((WorkHistoryActivity) context).hideProgress();
            ((WorkHistoryActivity) context).customDialog(context.getString(R.string.no_internet), WorkHistoryActivity.this);

        }

    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_work_history;
    }
}
