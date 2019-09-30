package com.android.ajtprestigecleaning.activities;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.ajtprestigecleaning.R;
import com.android.ajtprestigecleaning.adapter.CustomAdapter;
import com.android.ajtprestigecleaning.adapter.JobsDetailAdapter;
import com.android.ajtprestigecleaning.apiServices.ApiInterface;
import com.android.ajtprestigecleaning.apiServices.BaseUrl;
import com.android.ajtprestigecleaning.model.AllJobsPojo.Datum;
import com.android.ajtprestigecleaning.model.ChangePasswordPojo.ChangePasswordPojo;
import com.android.ajtprestigecleaning.model.JobDetailPojo.JobDetailPojo;
import com.android.ajtprestigecleaning.model.SubmitHourPojo.SubmitHourPojo;
import com.android.ajtprestigecleaning.model.UpdateJobStatusPojo.UpdateJobStatusPojo;
import com.android.ajtprestigecleaning.util.Constants;
import com.google.gson.JsonObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RequiresApi(api = Build.VERSION_CODES.O)
public class JobDetailActivity extends BaseActivityk implements AdapterView.OnItemSelectedListener {
    ImageView back, navigation;
    TextView id, location, task, date, desc, approx_hour, tv_log_hour,tv_end_job;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    JobsDetailAdapter adapter;
    String jobId = "";
    String lat = "";
    String lng = "";
    Spinner spinner;
    List<String> tasks;
    List<String> taskid;
    String taskId="";
    Dialog dialog;
    CustomAdapter customAdapter;
    HorizontalScrollView horizontalScrollView;
    String starttime="";
    String endtime="";
    Datum datum;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       final Intent intent = getIntent();
       jobId = intent.getStringExtra("jobId");
        datum = (Datum) intent.getSerializableExtra("sampleObject");
      //  jobDetail();
        back = findViewById(R.id.back);
        id = findViewById(R.id.id_number);
        navigation = findViewById(R.id.navigation);
        location = findViewById(R.id.location);
        task = findViewById(R.id.desc);
        approx_hour = findViewById(R.id.approx_hour);
        date = findViewById(R.id.date_time);
        spinner=findViewById(R.id.spinner);
        recyclerView = findViewById(R.id.task_recyclerview);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        desc = findViewById(R.id.description);
        tv_log_hour = findViewById(R.id.tv_log_hour);
        tv_end_job = findViewById(R.id.tv_end_job);
        horizontalScrollView=findViewById(R.id.tasks_hsv);
        spinner.setOnItemSelectedListener(this);
        tasks = new ArrayList<String>();
        taskid = new ArrayList<String>();
        getDatafromIntent();

        tv_log_hour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIosDialog();

            }
        });
        
        tv_end_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateJobStatus();
            }
        });
        
         // Creating adapter for spinner

        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
       // Toast.makeText(this, date, Toast.LENGTH_SHORT).show();



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

/*
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
                            customAdapter=new CustomAdapter(getApplicationContext(),tasks,taskid);
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
*/

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


    public void submit(long starttime,long endtime,String note) {
        showLoader(JobDetailActivity.this);
        if (isNetworkConnected(JobDetailActivity.this)) {
            ApiInterface service = BaseUrl.CreateService(ApiInterface.class);
            Call<SubmitHourPojo> call = service.submithour(jobId,Paper.book().read(Constants.USERID,"2"),taskId,starttime,endtime,endtime-starttime,note);
            call.enqueue(new Callback<SubmitHourPojo>() {
                @Override
                public void onResponse(Call<SubmitHourPojo> call, Response<SubmitHourPojo> response) {
                    if (response.isSuccessful()) {
                        hideLoader();
                        Log.e("start",response.body().getData().getStartTime()+"");
                        Log.e("end",response.body().getData().getEndTime()+"");
                        Log.e("diff",response.body().getData().getHours()+"");
                        dialog.dismiss();

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



/*
    public String timeDiff(long starttime,long endtime){
        long diffTime = endtime - starttime;
        String elapsedtime = new SimpleDateFormat("HH:mm:ss").format(Long.parseLong(String.valueOf(diffTime)));
        String periodAsHH_MM_SS = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(diffTime),
                TimeUnit.MILLISECONDS.toMinutes(diffTime) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(diffTime) % TimeUnit.MINUTES.toSeconds(1));

        System.out.println("Duration in hh:mm:ss is: "+periodAsHH_MM_SS);

            return elapsedtime;
    }
*/


    public void showIosDialog(){
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.log_hours);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        spinner=dialog.findViewById(R.id.spinner);
       // customAdapter=new CustomAdapter(getApplicationContext(),tasks,taskid);
        spinner.setAdapter(customAdapter);
        final EditText et_from=dialog.findViewById(R.id.et_from);
        final EditText et_to=dialog.findViewById(R.id.et_to);
        final EditText et_note=dialog.findViewById(R.id.et_note);
        et_from.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                mcurrentTime.add(Calendar.MONTH, +1);
                final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                final int minute = mcurrentTime.get(Calendar.MINUTE);
                final int day = mcurrentTime.get(Calendar.DAY_OF_MONTH);
                final int month = mcurrentTime.get(Calendar.MONTH);
                final int year = mcurrentTime.get(Calendar.YEAR);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(JobDetailActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        et_from.setText( selectedHour +"  "+ ":"+"  " + selectedMinute);
                        starttime=day+"-"+month+"-"+year+" "+selectedHour+":"+selectedMinute;

                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        et_to.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                mcurrentTime.add(Calendar.MONTH, +1);
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                final int day = mcurrentTime.get(Calendar.DAY_OF_MONTH);
                final int month = mcurrentTime.get(Calendar.MONTH);
                final int year = mcurrentTime.get(Calendar.YEAR);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(JobDetailActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        et_to.setText( selectedHour +"  "+ ":"+"  " + selectedMinute);
                        endtime=day+"-"+month+"-"+year+" "+selectedHour+":"+selectedMinute;
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        Button submit=dialog.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(et_from.getText().toString().isEmpty()){
                    et_from.requestFocus();
                }
                else if(et_to.getText().toString().isEmpty()){

                    et_to.requestFocus();

                }

                else if(et_note.getText().toString().isEmpty()){
                    et_note.setError("Please write note");

                }
                else{
                       // timeDiff(stringToTimeStamp(starttime),stringToTimeStamp(endtime));
                    submit(stringToTimeStamp(starttime),stringToTimeStamp(endtime),et_note.getText().toString());

                }
            }
        });
        dialog.show();


    }


    public long stringToTimeStamp(String time){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date = null;
        try {
            date = (Date)formatter.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long output=date.getTime()/1000L;
        String str=Long.toString(output);
        long timestamp = Long.parseLong(str) * 1000;

        System.out.println("Today is " +timestamp);
        return timestamp;
    }


    public void updateJobStatus() {
        showLoader(JobDetailActivity.this);
        if (isNetworkConnected(JobDetailActivity.this)) {
            ApiInterface service = BaseUrl.CreateService(ApiInterface.class);
            Call<UpdateJobStatusPojo> call = service.updateJobStatus(jobId,Paper.book().read(Constants.USERID, "1"),"5");
            call.enqueue(new Callback<UpdateJobStatusPojo>() {
                @Override
                public void onResponse(Call<UpdateJobStatusPojo> call, Response<UpdateJobStatusPojo> response) {
                    if (response.isSuccessful()) {
                        hideLoader();

                    } else {
                        hideLoader();
                        Toast.makeText(JobDetailActivity.this, getApplicationContext().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();


                    }
                }

                @Override
                public void onFailure(Call<UpdateJobStatusPojo> call, Throwable t) {
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


    public void getDatafromIntent(){
        final Intent intent = getIntent();
        jobId = intent.getStringExtra("jobId");
        datum = (Datum) intent.getSerializableExtra("sampleObject");
        lat = datum.getLatitude();
        lng = datum.getLongitude();
        id.setText("Id number / #" + datum.getId());
        location.setText(datum.getAddress());
        task.setText(datum.getName());
        date.setText(convertDate(datum.getCreatedAt(), "dd-MM-yyyy | hh.mm aa"));
        desc.setText(datum.getDescription());
      //  approx_hour.setText(datum.getHoursDaily().get(0) + " " + "Hours");
        adapter = new JobsDetailAdapter(datum, JobDetailActivity.this);
       recyclerView.setAdapter(adapter);

        for (int i = 0; i < datum.getCheckList().size(); i++) {
            tasks.add(datum.getCheckList().get(i).getName());
            taskid.add(datum.getCheckList().get(i).getId());

        }
        customAdapter=new CustomAdapter(getApplicationContext(),tasks,taskid);
        spinner.setAdapter(customAdapter);

    }

}
