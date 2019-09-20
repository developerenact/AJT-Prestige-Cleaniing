package com.android.ajtprestigecleaning.fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.ajtprestigecleaning.R;
import com.android.ajtprestigecleaning.adapter.JobsAdapter;
import com.android.ajtprestigecleaning.model.JobModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * A simple {@link Fragment} subclass.
 */
public class JobsFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    JobsAdapter adapter;
    ArrayList<JobModel> joblist;

    public JobsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_jobs, container, false);

        recyclerView = view.findViewById(R.id.job_recyclerview);

         joblist = new ArrayList<>();
         JobModel jobModel=new JobModel();
         jobModel.setHeader("In Progress");
         jobModel.setId("1001");
         jobModel.setAddress("house no. 785,Shivalik city");
         jobModel.setWork("Dusting");
         joblist.add(0,jobModel);



        JobModel jobModel3=new JobModel();
        jobModel3.setHeader("Upcoming");
        jobModel3.setId("1004");
        jobModel3.setAddress("house no. 365,Shivalik city");
        jobModel3.setWork("Cooking");
        joblist.add(1,jobModel3);

        JobModel jobModel2=new JobModel();
        jobModel2.setHeader("In Progress");
        jobModel2.setId("1003");
        jobModel2.setAddress("house no. 657,Chandigarh");
        jobModel2.setWork("Driving");
        joblist.add(2,jobModel2);

        JobModel jobModel4=new JobModel();
        jobModel4.setHeader("Upcoming");
        jobModel4.setId("1005");
        jobModel4.setAddress("house no. 365,Kharar");
        jobModel4.setWork("Baby sitting");
        joblist.add(3,jobModel4);

        JobModel jobModel1=new JobModel();
        jobModel1.setHeader("In Progress");
        jobModel1.setId("1002");
        jobModel1.setAddress("house no. 890,Mohali");
        jobModel1.setWork("cleaning");
        joblist.add(4,jobModel1);

        JobModel jobModel5=new JobModel();
        jobModel5.setHeader("Upcoming");
        jobModel5.setId("1006");
        jobModel5.setAddress("house no. 365,Zirakpur city");
        jobModel5.setWork("teaching");
        joblist.add(5,jobModel5);

        JobModel jobModel6=new JobModel();
        jobModel6.setHeader("Past Jobs");
        jobModel6.setId("1007");
        jobModel6.setAddress("house no. 365,Panchkula");
        jobModel6.setWork("Dusting");
        joblist.add(6,jobModel6);


        Collections.sort(joblist, new Comparator<JobModel>() {
            @Override
            public int compare(JobModel o1, JobModel o2) {
                return o1.getHeader().compareTo(o2.getHeader());
            }

        });


        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new JobsAdapter(joblist, getContext());
        recyclerView.setAdapter(adapter);

        return  view;
    }



}
