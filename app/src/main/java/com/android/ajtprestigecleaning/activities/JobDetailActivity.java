package com.android.ajtprestigecleaning.activities;

import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ajtprestigecleaning.R;
import com.android.ajtprestigecleaning.adapter.CustomAdapter;
import com.android.ajtprestigecleaning.adapter.JobsDetailAdapter;
import com.android.ajtprestigecleaning.apiServices.ApiInterface;
import com.android.ajtprestigecleaning.apiServices.BaseUrl;
import com.android.ajtprestigecleaning.model.ChangePasswordPojo.ChangePasswordPojo;
import com.android.ajtprestigecleaning.model.JobDetailPojo.JobDetailPojo;
import com.android.ajtprestigecleaning.model.JobDetailPojo.Task;
import com.android.ajtprestigecleaning.model.SubmitHourPojo.SubmitHourPojo;
import com.android.ajtprestigecleaning.util.Constants;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RequiresApi(api = Build.VERSION_CODES.O)
public class JobDetailActivity extends BaseActivityk implements AdapterView.OnItemSelectedListener {
    ImageView back, navigation;
    TextView id, location, task, date, desc, approx_hour, tv_log_hour;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    JobsDetailAdapter adapter;
    JobDetailPojo jobDetailPojo;
    String jobId = "";
    String lat = "";
    String lng = "";
    BottomSheetBehavior behavior;
    CoordinatorLayout coordinatorLayout;
    LinearLayout bottomsheet;
    Spinner spinner;
    ArrayAdapter<String> dataAdapter;
    List<String> tasks;
    List<String> taskid;
    TextView task_categoty;
    EditText et_from_hour,et_from_min,et_to_hour,et_to_min,et_note;
    TextView dots,dots2;
    Button submit;
    String starttime="";
    String endtime="";
    String taskId="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Intent intent = getIntent();
        jobId = intent.getStringExtra("jobId");
        jobDetail();
        back = findViewById(R.id.back);
        id = findViewById(R.id.id_number);
        navigation = findViewById(R.id.navigation);
        location = findViewById(R.id.location);
        task = findViewById(R.id.desc);
        approx_hour = findViewById(R.id.approx_hour);
        date = findViewById(R.id.date_time);
        recyclerView = findViewById(R.id.task_recyclerview);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        desc = findViewById(R.id.description);
        tv_log_hour = findViewById(R.id.tv_log_hour);
        coordinatorLayout = findViewById(R.id.coordinator_layout);
        bottomsheet = findViewById(R.id.bottom_sheet);
        spinner = findViewById(R.id.spinner);
        task_categoty = findViewById(R.id.category);
        submit=findViewById(R.id.submit);
        et_from_hour=findViewById(R.id.et_from_hour);
        et_from_min=findViewById(R.id.et_from_min);
        et_to_hour=findViewById(R.id.et_to_hour);
        et_to_min=findViewById(R.id.et_to_min);
        et_note=findViewById(R.id.et_note);
        dots=findViewById(R.id.dots);
        dots2=findViewById(R.id.dots2);
        behavior = BottomSheetBehavior.from(bottomsheet);
        spinner.setOnItemSelectedListener(this);
        tasks = new ArrayList<String>();
        taskid = new ArrayList<String>();
        tv_log_hour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            }
        });

        // Creating adapter for spinner


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starttime=et_from_hour.getText().toString()+":"+et_from_min.getText().toString()+" AM";
                endtime=et_to_hour.getText().toString()+":"+et_to_min.getText().toString()+" PM";
                submit(starttime,endtime);

            }
        });

        navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =
                        new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://maps.google.com/maps?" +
                                        "&daddr=" + String.valueOf(lat) + ","
                                        + String.valueOf(lng)));
                startActivity(intent);

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
                            lat = response.body().getData().getLatitude();
                            lng = response.body().getData().getLongitude();
                            id.setText("Id number / #" + response.body().getData().getId());
                            location.setText(response.body().getData().getAddress());
                            task.setText(response.body().getData().getName());
                            date.setText(convertDate(response.body().getData().getCreatedAt(), "dd-MM-yyyy | hh.mm aa"));
                            desc.setText(response.body().getData().getDescription());
                            approx_hour.setText(response.body().getData().getHoursDaily() + " " + "Hours");
                            adapter = new JobsDetailAdapter(response.body().getData().getTasks(), JobDetailActivity.this);
                            recyclerView.setAdapter(adapter);
                            for (int i = 0; i < response.body().getData().getTasks().size(); i++) {
                                tasks.add(response.body().getData().getTasks().get(i).getName());
                                taskid.add(response.body().getData().getTasks().get(i).getId());

                            }
                            CustomAdapter customAdapter=new CustomAdapter(getApplicationContext(),tasks,taskid);
                            spinner.setAdapter(customAdapter);


                        } else {

                        }


                    } else {
                        hideLoader();
                        Toast.makeText(JobDetailActivity.this, getApplicationContext().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();


                    }
                }

                @Override
                public void onFailure(Call<JobDetailPojo> call, Throwable t) {
                    hideLoader();
                    Log.d("otp", t.getMessage());
                    Toast.makeText(JobDetailActivity.this, getApplicationContext().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            hideLoader();
            customDialog(JobDetailActivity.this, getApplicationContext().getString(R.string.no_internet));

        }

    }

    public static String convertDate(String dateInMilliseconds, String dateFormat) {
        return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
         taskId=taskid.get(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void submit(String starttime,String endtime) {
        showLoader(JobDetailActivity.this);
        if (isNetworkConnected(JobDetailActivity.this)) {
            ApiInterface service = BaseUrl.CreateService(ApiInterface.class);
            Call<SubmitHourPojo> call = service.submithour(jobId,Paper.book().read(Constants.USERID,"2"),taskId,starttime,endtime,timeDiff(starttime,endtime),et_note.getText().toString());
            call.enqueue(new Callback<SubmitHourPojo>() {
                @Override
                public void onResponse(Call<SubmitHourPojo> call, Response<SubmitHourPojo> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(JobDetailActivity.this, response.body().getData().getHours(), Toast.LENGTH_SHORT).show();
                        hideLoader();

                    } else {
                        hideLoader();
                        Toast.makeText(JobDetailActivity.this, getApplicationContext().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();


                    }
                }

                @Override
                public void onFailure(Call<SubmitHourPojo> call, Throwable t) {
                    hideLoader();
                    Log.d("otp", t.getMessage());
                    Toast.makeText(JobDetailActivity.this, getApplicationContext().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            hideLoader();
            customDialog(JobDetailActivity.this, getApplicationContext().getString(R.string.no_internet));

        }

    }


    public String timeDiff(String starttime,String endtime){
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:m a", Locale.ENGLISH);

        LocalTime start = LocalTime.parse(starttime, timeFormatter);
        LocalTime end = LocalTime.parse(endtime, timeFormatter);

        Duration diff = Duration.between(start, end);

        long hours = diff.toHours();
        long minutes = diff.minusHours(hours).toMinutes();
        return String.format("%02d:%02d", hours, minutes);

    }



}
