package com.android.ajtprestigecleaning.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ajtprestigecleaning.R;
import com.android.ajtprestigecleaning.activities.DashboardActivity;
import com.android.ajtprestigecleaning.activities.JobDetailActivity;
import com.android.ajtprestigecleaning.adapter.JobsAdapter;
import com.android.ajtprestigecleaning.adapter.JobsDetailAdapter;
import com.android.ajtprestigecleaning.apiServices.ApiInterface;
import com.android.ajtprestigecleaning.apiServices.BaseUrl;
import com.android.ajtprestigecleaning.model.JobDetailPojo.JobDetailPojo;
import com.android.ajtprestigecleaning.model.JobListPojo.JobListPojo;
import com.android.ajtprestigecleaning.model.JobModel;
import com.android.ajtprestigecleaning.model.LoginPojo.LoginPojo;
import com.android.ajtprestigecleaning.util.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import io.paperdb.Paper;
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
    ArrayList<JobModel> joblist;
    JobListPojo jobListPojo;
    TextView header;

    public JobsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_jobs, container, false);

        recyclerView = view.findViewById(R.id.job_recyclerview);
        header = view.findViewById(R.id.header);


      /*  Collections.sort(joblist, new Comparator<JobModel>() {
            @Override
            public int compare(JobModel o1, JobModel o2) {
                return o1.getHeader().compareTo(o2.getHeader());
            }

        });*/


        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        // getjobs(0);

        return view;
    }


    public void getjobs(final int state, final Activity activity) {
        showLoader(activity);
        if (isNetworkConnected(activity)) {
            ApiInterface service = BaseUrl.CreateService(ApiInterface.class);
            Call<JobListPojo> call = service.getjobs(2, state);
            call.enqueue(new Callback<JobListPojo>() {
                @Override
                public void onResponse(Call<JobListPojo> call, Response<JobListPojo> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 0) {
                            hideLoader();
                            if(response.body().getData().size()>0) {
                                header.setVisibility(View.VISIBLE);
                                if (state == 0) {
                                    header.setVisibility(View.GONE);
                                } else if (state == 1) {
                                    header.setText(getActivity().getString(R.string.in_progress));
                                } else if (state == 2) {
                                    header.setText(getActivity().getString(R.string.upcoming));
                                } else if (state == 3) {
                                    header.setText(getActivity().getString(R.string.past_jobs));
                                } else if (state == 4) {
                                    header.setText(getActivity().getString(R.string.rejected_jobs));

                                } else {
                                    header.setText(getActivity().getString(R.string.completed_jobs));

                                }
                            }
                            else{
                                customDialog(activity, getActivity().getString(R.string.no_jobs_available));
                                header.setVisibility(View.GONE);
                            }
                            adapter = new JobsAdapter(response.body().getData(), getContext());
                            recyclerView.setAdapter(adapter);

                        } else {
                            hideLoader();
                        }

                    } else {
                        hideLoader();
                        Toast.makeText(getContext(),getActivity().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();


                    }
                }

                @Override
                public void onFailure(Call<JobListPojo> call, Throwable t) {
                    hideLoader();
                    Log.d("otp", t.getMessage());
                    Toast.makeText(getContext(), getActivity().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            hideLoader();
            customDialog(activity, getActivity().getString(R.string.no_internet));

        }

    }


}
