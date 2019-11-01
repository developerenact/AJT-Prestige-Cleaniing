package com.android.ajtprestigecleaning.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.ajtprestigecleaning.R;
import com.android.ajtprestigecleaning.adapter.SpinnerAdapter;
import com.android.ajtprestigecleaning.adapter.JobsDetailAdapter;
import com.android.ajtprestigecleaning.apiServices.ApiInterface;
import com.android.ajtprestigecleaning.apiServices.BaseUrl;
import com.android.ajtprestigecleaning.model.HoursModel;
import com.android.ajtprestigecleaning.model.JobsPojo.Datum;
import com.android.ajtprestigecleaning.model.JobsPojo.Hour;
import com.android.ajtprestigecleaning.model.JobsPojo.Task;
import com.android.ajtprestigecleaning.model.SubmitHourPojo.SubmitHourPojo;
import com.android.ajtprestigecleaning.model.UpdateJobStatusPojo.UpdateJobStatusPojo;
import com.android.ajtprestigecleaning.util.Constants;
import com.google.gson.JsonObject;

import java.math.BigInteger;
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RequiresApi(api = Build.VERSION_CODES.O)
public class JobDetailActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {
    ImageView back, navigation;
    TextView id, location, task, date, desc, approx_hour, tv_log_hour, tv_end_job, tv_job_type, tv_job_price, tv_carpets, tv_notes;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    JobsDetailAdapter adapter;
    String jobId = "";
    String lat = "";
    Spinner spinner;
    String lng = "";
    int pos;
    String str_taskId = "";
    String str_taskname = "";
    String str_checklistid = "";
    Dialog dialog;
    SpinnerAdapter customAdapter;
    HorizontalScrollView horizontalScrollView;
    LinearLayout hsv_linear_layout;
    LinearLayoutCompat start_job_layout, log_hours_layout;
    String starttime = "";
    String endtime = "";
    Datum datum;
    List<Hour> hoursModelList;
    Hour hour;
    List<Task> tasks;
    List<String> checklistId;
    View view;
    LinearLayout.LayoutParams layoutParams;
    EditText et_note;
    int checkklist_position = -1;
    int task_position = -1;
    ImageView startjob;
    String jobstatus = "";

    int starthour=0;
    int endHour=0;
    long startMili=0;
    long endMili=0;
    long diff=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Intent intent = getIntent();
        jobId = intent.getStringExtra("jobId");
        datum = (Datum) intent.getSerializableExtra("sampleObject");
        //  jobDetail();
        hour = new Hour();


        back = findViewById(R.id.back);
        id = findViewById(R.id.id_number);
        navigation = findViewById(R.id.navigation);
        location = findViewById(R.id.location);
        task = findViewById(R.id.desc);
        approx_hour = findViewById(R.id.approx_hour);
        date = findViewById(R.id.date_time);
        startjob = findViewById(R.id.img_start_job);
        //  spinner=findViewById(R.id.spinner);
        recyclerView = findViewById(R.id.task_recyclerview);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        desc = findViewById(R.id.description);
        tv_log_hour = findViewById(R.id.tv_log_hour);
        tv_end_job = findViewById(R.id.tv_end_job);
        tv_job_price = findViewById(R.id.tv_job_price);
        hsv_linear_layout = findViewById(R.id.taskshsvLinearLayout);
        start_job_layout = findViewById(R.id.startjob_layout);
        log_hours_layout = findViewById(R.id.startjob_loghours);
        tv_carpets = findViewById(R.id.carpets);
        tv_notes = findViewById(R.id.notes);
        //  spinner.setOnItemSelectedListener(this);
        tasks = new ArrayList<Task>();
        hoursModelList = new ArrayList<Hour>();
        checklistId = new ArrayList<String>();
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
                jobstatus = "5";
                updateJobStatus(jobstatus);
            }
        });

        startjob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jobstatus = "1";
                updateJobStatus(jobstatus);
            }
        });

        logHours();

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
    public int getLayoutResourceId() {
        return R.layout.activity_job_detail;
    }


    public static String convertDate(String dateInMilliseconds, String dateFormat) {
        return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ((LinearLayout) parent.getChildAt(0)).setGravity(Gravity.CENTER);
        pos = position;
        str_taskId = tasks.get(position).getId();
        str_checklistid = checklistId.get(position);
        str_taskname = tasks.get(position).getName();
        Log.e("selected", checklistId.get(position));
        pos = position;

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void submit(long starttime, long endtime,long difference, final String note) {
        BigInteger bg1 = new BigInteger(String.valueOf(endtime - starttime));
        System.out.println(bg1.abs());
        Log.e("Dekho", String.valueOf(bg1.abs()));
        //showLoader(JobDetailActivity.this);
        showProgress();
        if (isNetworkAvailable()) {
            ApiInterface service = BaseUrl.CreateService(ApiInterface.class);
            Call<SubmitHourPojo> call = service.submithour(jobId, "2", str_taskId, starttime, endtime, difference, note);
            call.enqueue(new Callback<SubmitHourPojo>() {
                @Override
                public void onResponse(Call<SubmitHourPojo> call, Response<SubmitHourPojo> response) {
                    if (response.isSuccessful()) {
                        //hideLoader();
                        dialog.dismiss();
                        hideProgress();
                        Log.e("start", response.body().getData().getStartTime() + "");
                        Log.e("end", response.body().getData().getEndTime() + "");
                        Log.e("diff", response.body().getData().getHours() + "");
                        updateTaskStatus();

                        hour.setEndTime(response.body().getData().getEndTime());
                        hour.setStartTime(response.body().getData().getStartTime());
                        hour.setHours(response.body().getData().getHours());
                        hour.setTaskName(str_taskname);
                        hoursModelList.add(hour);
                        if (view.getParent() != null) {
                            ((ViewGroup) view.getParent()).removeView(view);
                            // <- fix
                        }
                        logHours();

                    } else {
                        // hideLoader();
                        hideProgress();
                        Toast.makeText(JobDetailActivity.this, getApplicationContext().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();


                    }
                }

                @Override
                public void onFailure(Call<SubmitHourPojo> call, Throwable t) {
                    // hideLoader();
                    hideProgress();
                    Log.d("otp", t.getMessage());
                    Toast.makeText(JobDetailActivity.this, getApplicationContext().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            //hideLoader();
            hideProgress();
            customDialog(getApplicationContext().getString(R.string.no_internet), JobDetailActivity.this);

        }

    }

    public void showIosDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.log_hours);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        spinner = dialog.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        customAdapter = new SpinnerAdapter(getApplicationContext(), tasks, checklistId);
        spinner.setAdapter(customAdapter);
        final TextView et_from = dialog.findViewById(R.id.et_from);
        final TextView et_to = dialog.findViewById(R.id.et_to);
        et_note = dialog.findViewById(R.id.et_note);
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
                        et_from.setText(selectedHour + "  " + ":" + "  " + selectedMinute);
                        starttime = day + "-" + month + "-" + year + " " + selectedHour + ":" + selectedMinute;

                        final Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                        calendar.set(Calendar.MINUTE, selectedMinute);
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);
                        starthour=selectedHour;
                        startMili=calendar.getTimeInMillis();



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
                        et_to.setText(selectedHour + "  " + ":" + "  " + selectedMinute);
                        endtime = day + "-" + month + "-" + year + " " + selectedHour + ":" + selectedMinute;


                        final Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                        calendar.set(Calendar.MINUTE, selectedMinute);
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);

                        endHour=selectedHour;
                        endMili=calendar.getTimeInMillis();


                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        Button submit = dialog.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_from.getText().toString().isEmpty()) {
                    customDialog(getApplicationContext().getString(R.string.choose_start_time), JobDetailActivity.this);
                } else if (et_to.getText().toString().isEmpty()) {

                    customDialog(getApplicationContext().getString(R.string.choose_end_time), JobDetailActivity.this);


                } else if (et_from.getText().toString().equals(et_to.getText().toString())) {
                    customDialog(getApplicationContext().getString(R.string.time_should_not_same), JobDetailActivity.this);
                } else if (et_note.getText().toString().isEmpty()) {
                    et_note.setError(getApplicationContext().getString(R.string.write_note));
                    et_note.requestFocus();

                } else {


                    // timeDiff(stringToTimeStamp(starttime),stringToTimeStamp(endtime));
                    // updateTaskStatus();
                    if (starthour <= 12 && starthour<endHour) {
                        startMili = startMili + 86400000;

                    }
                    if (endHour <= 12) {
                        endMili = endMili+ 86400000;

                    }


                   // Toast.makeText(JobDetailActivity.this, String.valueOf(convertSecondsToHMmSs(endMili-startMili)), Toast.LENGTH_SHORT).show();
                    diff=endMili-startMili;
                    BigInteger bg1 = new BigInteger(String.valueOf(diff));
                    System.out.println(bg1.abs());
                   // Toast.makeText(JobDetailActivity.this, String.valueOf(bg1.abs()), Toast.LENGTH_SHORT).show();

                    submit(stringToTimeStamp(starttime), stringToTimeStamp(endtime),bg1.abs().longValue(), et_note.getText().toString());

                }
            }
        });
        dialog.show();

    }


    public long stringToTimeStamp(String time) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date = null;
        try {
            date = (Date) formatter.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long output = date.getTime() / 1000L;
        String str = Long.toString(output);
        long timestamp = Long.parseLong(str) * 1000;
        System.out.println("Today is " + timestamp);
        return timestamp;
    }


    public void updateJobStatus(String status) {
        // showLoader(JobDetailActivity.this);
        showProgress();
        if (isNetworkAvailable()) {
            ApiInterface service = BaseUrl.CreateService(ApiInterface.class);
            Call<UpdateJobStatusPojo> call = service.updateJobStatus(jobId, "2", status);
            call.enqueue(new Callback<UpdateJobStatusPojo>() {
                @Override
                public void onResponse(Call<UpdateJobStatusPojo> call, Response<UpdateJobStatusPojo> response) {
                    if (response.isSuccessful()) {
                        // hideLoader();
                        hideProgress();
                        if (response.body().getStatus() == 0) {
                            if (jobstatus.equals("1")) {
                                start_job_layout.setVisibility(View.GONE);
                                log_hours_layout.setVisibility(View.VISIBLE);
                            } else {
                                start_job_layout.setVisibility(View.GONE);
                                log_hours_layout.setVisibility(View.GONE);

                            }
                        } else {
                            Toast.makeText(JobDetailActivity.this, getApplicationContext().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();

                        }

                    } else {
                        // hideLoader();
                        hideProgress();
                        Toast.makeText(JobDetailActivity.this, getApplicationContext().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<UpdateJobStatusPojo> call, Throwable t) {
                    // hideLoader();
                    hideProgress();
                    Log.d("otp", t.getMessage());
                    Toast.makeText(JobDetailActivity.this, getApplicationContext().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            // hideLoader();
            hideProgress();
            customDialog(getApplicationContext().getString(R.string.no_internet), JobDetailActivity.this);

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void getDatafromIntent() {
        final Intent intent = getIntent();
        jobId = intent.getStringExtra("jobId");
        datum = (Datum) intent.getSerializableExtra("sampleObject");
        lat = datum.getLatitude();
        lng = datum.getLongitude();
        id.setText("Id number / #" + datum.getId() + " /" + " " + datum.getJobType());
        location.setText(datum.getAddress());
        task.setText(datum.getName());
        if (!datum.getPrice().isEmpty()) {
            tv_job_price.setText(datum.getPrice());

        } else {
            tv_job_price.setText("N/A");
        }
        date.setText(convertDate(datum.getCreatedAt(), "dd-MM-yyyy | hh.mm aa"));
        desc.setText(datum.getDescription());
        approx_hour.setText(datum.getHoursDaily() + " Hours");
        tv_carpets.setText(datum.getCarpets());
        tv_notes.setText(datum.getNotes());
        if (datum.getJobStatus().equals("2")) {
            log_hours_layout.setVisibility(View.GONE);
            start_job_layout.setVisibility(View.VISIBLE);
        } else if (datum.getJobStatus().equals("1")) {
            log_hours_layout.setVisibility(View.VISIBLE);
            start_job_layout.setVisibility(View.GONE);
        } else {
            log_hours_layout.setVisibility(View.GONE);
            start_job_layout.setVisibility(View.GONE);
        }

        adapter = new JobsDetailAdapter(datum, JobDetailActivity.this);
        recyclerView.setAdapter(adapter);

        for (int i = 0; i < datum.getHours().size(); i++) {
            hoursModelList.add(datum.getHours().get(i));

        }

        if (datum.getCheckList().size() > 0) {
            for (int i = 0; i < datum.getCheckList().size(); i++) {
                for (int j = 0; j < datum.getCheckList().get(i).getTasks().size(); j++) {
                    if (datum.getCheckList().get(i).getTasks() != null) {
                        tasks.add(datum.getCheckList().get(i).getTasks().get(j));
                        checklistId.add(datum.getCheckList().get(i).getId());
                    }
                }

            }
        }

    }

    public void updateTaskStatus() {
        //showLoader(JobDetailActivity.this);
        // showProgress();
        if (isNetworkAvailable()) {
            ApiInterface service = BaseUrl.CreateService(ApiInterface.class);
            Call<JsonObject> call = service.updateTask(jobId, "2", str_taskId);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        // hideLoader();
                        hideProgress();
                        changeStatus(str_checklistid, str_taskId);


                    } else {
                        //hideLoader();
                        hideProgress();

                        Toast.makeText(JobDetailActivity.this, getApplicationContext().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();


                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    // hideLoader();
                    hideProgress();
                    Log.d("otp", t.getMessage());
                    Toast.makeText(JobDetailActivity.this, getApplicationContext().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            // hideLoader();
            hideProgress();
            customDialog(getApplicationContext().getString(R.string.no_internet), JobDetailActivity.this);

        }

    }

    public String convertSecondsToHMmSs(long milliseconds) {
        long s = (int) (milliseconds / 1000) % 60;
        long m = (int) Math.abs((milliseconds / (1000 * 60)) % 60);
        long h = (int) Math.abs((milliseconds / (1000 * 60 * 60)) % 24);
        return String.format("%02d:%02d", h, m);
    }

    public String convertToTime(String dateInMilliseconds) {

        return DateFormat.format("HH:mm", Long.parseLong(dateInMilliseconds)).toString();

    }

    public void showStartButton(int mon, int day, int yr) {
        Calendar today = Calendar.getInstance();
        int dayOfMonth = today.get(Calendar.DAY_OF_MONTH);
        int month = today.get(Calendar.MONTH);
        int year = today.get(Calendar.YEAR);
        if (month == mon && dayOfMonth == day && year == yr) {
            start_job_layout.setVisibility(View.VISIBLE);
        } else {
            start_job_layout.setVisibility(View.GONE);
        }

    }

    public void changeStatus(String checkId, String taskId) {
        tasks.get(pos).setStatus("0");

        boolean breakkLoop = false;
        for (int i = 0; i < datum.getCheckList().size(); i++) {

            String chkListId = datum.getCheckList().get(i).getId();
            if (breakkLoop) {
                break;
            }
            for (int i1 = 0; i1 < datum.getCheckList().get(i).getTasks().size(); i1++) {

                if (datum.getCheckList().get(i).getTasks().get(i1).getId().equalsIgnoreCase(taskId) && chkListId.equalsIgnoreCase(checkId)) {
                    datum.getCheckList().get(i).getTasks().get(i1).setStatus("1");
                    breakkLoop = true;
                    break;
                }
            }
        }
        // tasks.get(pos).setStatus("0");

        adapter.notifyDataSetChanged();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 11 && resultCode == RESULT_OK) {
                int pos = data.getIntExtra("position", 0);
                int img_count = data.getIntExtra("img_count", 0);
                int txt_count = data.getIntExtra("txt_count", 0);
                String chklistid = data.getStringExtra("checklistId");
                String taskid = data.getStringExtra("taskId");


                boolean breakkLoop = false;
                for (int i = 0; i < datum.getCheckList().size(); i++) {

                    String chkListId = datum.getCheckList().get(i).getId();
                    if (breakkLoop) {
                        break;
                    }
                    for (int i1 = 0; i1 < datum.getCheckList().get(i).getTasks().size(); i1++) {

                        if (datum.getCheckList().get(i).getTasks().get(i1).getId().equalsIgnoreCase(taskid) && chkListId.equalsIgnoreCase(chklistid)) {
                            // datum.getCheckList().get(i).getTasks().get(i1).setImageCount(String.valueOf(Integer.parseInt(tasks.get(pos).getImageCount())+1));
                            // datum.getCheckList().get(i).getTasks().get(i1).setTextCount(String.valueOf(Integer.parseInt(tasks.get(pos).getTextCount())+1));
                            datum.getCheckList().get(i).getTasks().get(i1).setImageCount(String.valueOf(Integer.parseInt(datum.getCheckList().get(i).getTasks().get(i1).getImageCount()) + img_count));
                            datum.getCheckList().get(i).getTasks().get(i1).setTextCount(String.valueOf(Integer.parseInt(datum.getCheckList().get(i).getTasks().get(i1).getTextCount()) + txt_count));
                            breakkLoop = true;
                            break;
                        }
                    }
                }
                // tasks.get(pos).setStatus("0");

                adapter.notifyDataSetChanged();

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void logHours() {
        hsv_linear_layout.removeAllViews();
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < hoursModelList.size(); i++) {
            view = inflater.inflate(R.layout.task_time_item, null, false);
            layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 8, 0, 8);
            view.setLayoutParams(layoutParams);
            TextView name = view.findViewById(R.id.name);
            Typeface custom_font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Montserrat-Medium.ttf");
            name.setTypeface(custom_font);
            TextView from = view.findViewById(R.id.from);
            TextView to = view.findViewById(R.id.to);
            TextView total = view.findViewById(R.id.total);
            name.setText(hoursModelList.get(i).getTaskName());
            try {
                from.setText(convertToTime(hoursModelList.get(i).getStartTime()));

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                to.setText(convertToTime(hoursModelList.get(i).getEndTime()));

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {

                total.setText(convertSecondsToHMmSs(Long.parseLong(hoursModelList.get(i).getHours())));

            } catch (Exception e) {
                e.printStackTrace();
            }
            hsv_linear_layout.addView(view);

        }
    }


}
