package com.android.ajtprestigecleaning.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ajtprestigecleaning.R;
import com.android.ajtprestigecleaning.adapter.JobsAdapter;
import com.android.ajtprestigecleaning.adapter.JobsDetailAdapter;
import com.android.ajtprestigecleaning.apiServices.ApiInterface;
import com.android.ajtprestigecleaning.apiServices.BaseUrl;
import com.android.ajtprestigecleaning.model.JobDetailPojo.JobDetailPojo;
import com.android.ajtprestigecleaning.model.JobModel;
import com.android.ajtprestigecleaning.model.LoginPojo.LoginPojo;
import com.android.ajtprestigecleaning.util.Constants;

import java.util.ArrayList;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobDetailActivity extends BaseActivityk {
    ImageView back;
    TextView id,location,task,date,desc,approx_hour;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    JobsDetailAdapter adapter;
    JobDetailPojo jobDetailPojo;
    String jobId="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        jobId=intent.getStringExtra("jobId");
        jobDetail();
        back=findViewById(R.id.back);
        id=findViewById(R.id.id_number);
        location=findViewById(R.id.location);
        task=findViewById(R.id.desc);
        approx_hour=findViewById(R.id.approx_hour);
        date=findViewById(R.id.date_time);
        recyclerView = findViewById(R.id.task_recyclerview);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        desc=findViewById(R.id.description);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_job_detail;
    }

    public void jobDetail() {
        showLoader(JobDetailActivity.this);
        if (isNetworkConnected(JobDetailActivity.this)) {
            ApiInterface service = BaseUrl.CreateService(ApiInterface.class);
            Call<JobDetailPojo> call = service.getjobDetail(Integer.parseInt(jobId));
            call.enqueue(new Callback<JobDetailPojo>() {
                @Override
                public void onResponse(Call<JobDetailPojo> call, Response<JobDetailPojo> response) {
                    if (response.isSuccessful()) {
                        hideLoader();

                        if (response.body().getStatus() == 0) {
                            id.setText(response.body().getData().getId());
                            location.setText(response.body().getData().getAddress());
                            task.setText(response.body().getData().getName());
                            date.setText(convertDate(response.body().getData().getCreatedAt(),"dd-MM-yyyy | hh.mm aa"));
                            desc.setText(response.body().getData().getDescription());
                            approx_hour.setText("Approximation hours:"+" "+response.body().getData().getHoursDaily()+" "+"Hours");
                            adapter = new JobsDetailAdapter(response.body().getData().getTasks(), JobDetailActivity.this);
                            recyclerView.setAdapter(adapter);

                        } else {

                        }


                    } else {
                        hideLoader();
                        Toast.makeText(JobDetailActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();


                    }
                }

                @Override
                public void onFailure(Call<JobDetailPojo> call, Throwable t) {
                    hideLoader();
                    Log.d("otp", t.getMessage());
                    Toast.makeText(JobDetailActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            hideLoader();
            customDialog(JobDetailActivity.this, "Pleasr check your Internet Connection");

        }

    }

    public static String convertDate(String dateInMilliseconds, String dateFormat) {
        return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
    }


}
