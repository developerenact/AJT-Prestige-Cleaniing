package com.android.ajtprestigecleaning.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ajtprestigecleaning.R;
import com.android.ajtprestigecleaning.activities.DashboardActivity;
import com.android.ajtprestigecleaning.adapter.JobsAdapter;
import com.android.ajtprestigecleaning.apiServices.ApiInterface;
import com.android.ajtprestigecleaning.apiServices.BaseUrl;

import com.android.ajtprestigecleaning.model.JobsPojo.Datum;
import com.android.ajtprestigecleaning.model.JobsPojo.JobsPojo;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.android.ajtprestigecleaning.activities.BaseActivityk.customDialog;
import static com.android.ajtprestigecleaning.activities.BaseActivityk.hideLoader;
import static com.android.ajtprestigecleaning.activities.BaseActivityk.isNetworkConnected;
import static com.android.ajtprestigecleaning.activities.BaseActivityk.showLoader;

/**
 * A simple {@link Fragment} subclass.
 */
public class JobsFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    JobsAdapter adapter;
    TextView header;
    List<Datum> joblist;
    List<String> type;
    ArrayList<Datum> jobsWithHeader;
    Context context;
    AVLoadingIndicatorView progressBar;

    public JobsFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_jobs, container, false);
        recyclerView = view.findViewById(R.id.job_recyclerview);
        // progressBar = (AVLoadingIndicatorView) view.findViewById(R.id.loader);
        // progressBar.show();
        type = new ArrayList<>();
        joblist = new ArrayList<>();
        jobsWithHeader = new ArrayList<>();
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        // getjobs(0);

        return view;
    }


    public void getjobs(int state, final Context context) {
        ((DashboardActivity) context).showProgress();
        if (((DashboardActivity) context).isNetworkAvailable()) {
            ApiInterface service = BaseUrl.CreateService(ApiInterface.class);
            Call<JobsPojo> call = service.getjobs(2, state);
            call.enqueue(new Callback<JobsPojo>() {
                @Override
                public void onResponse(Call<JobsPojo> call, Response<JobsPojo> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 0) {

                            ((DashboardActivity) context).hideProgress();
                            if (response.body().getData().size() > 0) {

                            } else {
                                ((DashboardActivity) context).customDialog(getActivity().getString(R.string.no_jobs_available), getContext());
                            }
                            joblist = response.body().getData();


                            Collections.sort(joblist, new Comparator<Datum>() {
                                @Override
                                public int compare(Datum o1, Datum o2) {
                                    return o1.getJobStatus().compareTo(o2.getJobStatus());
                                }

                            });

                            List<Datum> In_progress = new ArrayList<>();
                            List<Datum> Upcoming_Jobs = new ArrayList<>();
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

                                    if (joblist.get(i).getJobStatus().equalsIgnoreCase("1")) {
                                        In_progress.add(joblist.get(i));
                                    } else if (joblist.get(i).getJobStatus().equalsIgnoreCase("2")) {
                                        Upcoming_Jobs.add(joblist.get(i));
                                    } else if (joblist.get(i).getJobStatus().equalsIgnoreCase("3")) {
                                        Past_Jobs.add(joblist.get(i));
                                    } else if (joblist.get(i).getJobStatus().equalsIgnoreCase("4")) {
                                        Rejected_Jobs.add(joblist.get(i));
                                    } else if (joblist.get(i).getJobStatus().equalsIgnoreCase("5")) {
                                        Completed.add(joblist.get(i));
                                    }
                                }
                                joblist.clear();
                            }


                            if (In_progress.size() > 0) {
                                Datum datum = new Datum();
                                datum.setHeader(true);
                                datum.setHeaderName("In Progress");
                                joblist.add(datum);
                                joblist.addAll(In_progress);

                            }

                            if (Upcoming_Jobs.size() > 0) {
                                Datum datum = new Datum();
                                datum.setHeader(true);
                                datum.setHeaderName("Upcomming");
                                joblist.add(datum);
                                joblist.addAll(Upcoming_Jobs);

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


                            adapter = new JobsAdapter(joblist, getContext());
                            recyclerView.setAdapter(adapter);

                        } else {
                            ((DashboardActivity) context).hideProgress();

                        }

                    } else {

                        ((DashboardActivity) context).hideProgress();
                        Toast.makeText(getContext(), getActivity().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();


                    }
                }

                @Override
                public void onFailure(Call<JobsPojo> call, Throwable t) {

                    ((DashboardActivity) context).hideProgress();
                    Log.d("otp", t.getMessage());
                    Toast.makeText(getContext(), getActivity().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
                }
            });
        } else {

            ((DashboardActivity) context).hideProgress();
            ((DashboardActivity) context).customDialog(getActivity().getString(R.string.no_internet), getContext());

        }

    }

    public void update(int state) {
        getjobs(state, context);
    }




}
