package com.android.ajtprestigecleaning.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.ajtprestigecleaning.R;
import com.android.ajtprestigecleaning.adapter.JobsDetailAdapter;
import com.android.ajtprestigecleaning.adapter.TaskLogsAdapter;
import com.android.ajtprestigecleaning.apiServices.ApiInterface;
import com.android.ajtprestigecleaning.apiServices.BaseUrl;
import com.android.ajtprestigecleaning.model.AddLogPojo.AddLogPojo;
import com.android.ajtprestigecleaning.model.AllLogsPojo.AllLogsPojo;
import com.android.ajtprestigecleaning.model.ChangePasswordPojo.ChangePasswordPojo;
import com.android.ajtprestigecleaning.model.JobsPojo.CheckList;
import com.android.ajtprestigecleaning.model.JobsPojo.Datum;
import com.android.ajtprestigecleaning.model.JobsPojo.Task;
import com.android.ajtprestigecleaning.model.UpdateProfilePojo.UpdateProfilePojo;
import com.android.ajtprestigecleaning.util.Constants;
import com.bumptech.glide.Glide;
import com.google.android.material.circularreveal.cardview.CircularRevealCardView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import id.zelory.compressor.Compressor;
import io.paperdb.Paper;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LogsActivity extends BaseActivity {
    Dialog dialog;
    ImageView add_logs, back;
    Uri selectedUri;
    Bitmap bitmap;
    File file;
    boolean permissionStatus = false;
    LinearLayout linearLayout;
    EditText et_note;
    Button submit;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    TaskLogsAdapter adapter;
    CheckList checkList;
    Task tasks;
    Datum datum;
    TextView tv_log_name, tv_log_desc, log_label;
    ArrayList<com.android.ajtprestigecleaning.model.AllLogsPojo.Datum> logs = new ArrayList<>();
    com.android.ajtprestigecleaning.model.AllLogsPojo.Datum logspojo;
    int position = -1;
    int img_count = 0;
    int txt_count = 0;
    String checklistId = "";
    String taskId = "";
    ImageView log_image;
    LinearLayout choose_file_layout;
    CircularRevealCardView log_img_card;
    File compressedFile;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkpermission();
        final Intent intent = getIntent();
        tasks = (Task) intent.getSerializableExtra("Logs");
        datum = (Datum) intent.getSerializableExtra("Alldata");
        position = intent.getIntExtra("position", 0);
        checklistId = intent.getStringExtra("checklistId");
        taskId = intent.getStringExtra("taskId");
        allLogsApi();
        recyclerView = findViewById(R.id.logs_recycle);
        tv_log_name = findViewById(R.id.log_name);
        tv_log_desc = findViewById(R.id.log_desc);
        tv_log_name.setText(tasks.getName());
        tv_log_desc.setText(datum.getDescription());

        add_logs = findViewById(R.id.add_logs_btn);
        log_label = findViewById(R.id.log_label);

        Typeface custom_font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Montserrat-Medium.ttf");
        log_label.setTypeface(custom_font);


        back = findViewById(R.id.back);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);
        logspojo = new com.android.ajtprestigecleaning.model.AllLogsPojo.Datum();

        add_logs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomdialog();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_logs;
    }

    public void openBottomdialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.add_log_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout = dialog.findViewById(R.id.choose_file_layout);
        et_note = dialog.findViewById(R.id.et_note);
        submit = dialog.findViewById(R.id.submit);
        choose_file_layout = dialog.findViewById(R.id.choose_file_layout);
        log_image = dialog.findViewById(R.id.log_image);
        log_img_card = dialog.findViewById(R.id.log_img_card);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showimagePicker();
            }
        });

        log_img_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showimagePicker();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_note.getText().toString().isEmpty()) {
                    et_note.setError("Please enter note");
                    et_note.requestFocus();
                } else {
                    addLog(datum.getId(), "2", tasks.getId(), et_note.getText().toString(), file);

                }
            }
        });
        dialog.show();


    }


    private void showimagePicker() {
        final CharSequence[] charSequence = {"Camera", "Gallery", "Cancel"};
        final AlertDialog.Builder alert = new AlertDialog.Builder(LogsActivity.this);
        alert.setCancelable(false);
        alert.setTitle("Select Image");
        alert.setItems(charSequence, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (charSequence[i].equals("Camera"))
                    openCamera();
                else if (charSequence[i].equals("Gallery"))
                    openGallery();
                else
                    dialogInterface.dismiss();

            }
        });
        alert.show();
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), 2);

    }

    private void openCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == 1) {
                getCameraImage(data);
            } else if (requestCode == 2) {
                getGalleryImage(data);
            }

        }
    }

    private void getCameraImage(Intent data) {

        bitmap = (Bitmap) data.getExtras().get("data");
        //image.setImageBitmap(bitmap);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
        choose_file_layout.setVisibility(View.GONE);
        log_img_card.setVisibility(View.VISIBLE);
        log_image.setImageBitmap(bitmap);
        file = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            file.createNewFile();
            fo = new FileOutputStream(file);
            fo.write(outputStream.toByteArray());
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        selectedUri = Uri.fromFile(file);
        Log.e("selecteduri ", "urcam " + selectedUri);
// performCrop(String.valueOf(selectedUri));

        if (selectedUri != null) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    private void getGalleryImage(Intent data) {

        Uri uri = data.getData();
        String path = getRealPathFromURI_API19(uri);
        file = new File(path);
        selectedUri = Uri.fromFile(file);
// image.setImageURI(uri);
        Log.e("seletedpath ", "gal : " + selectedUri);
        if (selectedUri != null) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedUri);
                choose_file_layout.setVisibility(View.GONE);
                log_img_card.setVisibility(View.VISIBLE);
               // log_image.setImageBitmap(bitmap);
                Glide.with(LogsActivity.this)
                        .load(bitmap)
                        .placeholder(R.drawable.demoprofile)
                        .into(log_image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("position", position);
        returnIntent.putExtra("img_count", img_count);
        returnIntent.putExtra("txt_count", txt_count);
        returnIntent.putExtra("checklistId", checklistId);
        returnIntent.putExtra("taskId", taskId);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();

    }

    public String getRealPathFromURI_API19(Uri uri) {
        String filePath = "";
        String wholeID = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            wholeID = DocumentsContract.getDocumentId(uri);
        }
// Split at colon, use second item in the array
        String id = wholeID.split(":")[1];
        String[] column = {MediaStore.Images.Media.DATA};
// where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";
        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{id}, null);
        int columnIndex = cursor.getColumnIndex(column[0]);
        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

    private void checkpermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 5);

            } else {
                permissionStatus = true;
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 5: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    permissionStatus = true;
                } else {
                    showPermissionDialog1();
                }
                return;
            }
        }
    }

    private void showPermissionDialog1() {

        AlertDialog.Builder alert = new AlertDialog.Builder(LogsActivity.this);
        alert.setTitle("Permissions Required")
                .setMessage("You have denied Required permissions " +
                        "To Continue, Please open settings, go to app and then to its permissions and allow them.");
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                LogsActivity.this.finish();
            }
        });
        alert.show();
    }


    public void addLog(String jobId, String userId, String taskId, String
            text, File image) {

       /* HashMap<String, RequestBody> data = new HashMap<>();
        if (image != null) {
            RequestBody rb_img = RequestBody.create(MediaType.parse("image/*"), image);
            data.put("image\"; filename=\"" + image.getName(), rb_img);

        }

        showProgress();
        if (isNetworkAvailable()) {
            ApiInterface service = BaseUrl.demoCreateService(ApiInterface.class);
            Call<String> call = service.demo(data);

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    hideProgress();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    hideProgress();
                }
            });
        } else {
            // hideLoader();
            hideProgress();
            customDialog( getApplicationContext().getString(R.string.no_internet),LogsActivity.this);

        }
*/
        HashMap<String, RequestBody> data = new HashMap<>();
        RequestBody rb_jobId = RequestBody.create(MediaType.parse("text/plain"), jobId);
        RequestBody rb_userId = RequestBody.create(MediaType.parse("text/plain"), userId);
        RequestBody rb_taskId = RequestBody.create(MediaType.parse("text/plain"), taskId);
        RequestBody rb_text = RequestBody.create(MediaType.parse("text/plain"), text);

        data.put("jobId", rb_jobId);
        data.put("userId", rb_userId);
        data.put("taskId", rb_taskId);
        data.put("text", rb_text);


        if (image != null) {
            RequestBody rb_img = RequestBody.create(MediaType.parse("image/*"), image);
            data.put("image\"; filename=\"" + image.getName(), rb_img);

        }
        // showLoader(LogsActivity.this);
        showProgress();
        if (isNetworkAvailable()) {
            ApiInterface service = BaseUrl.CreateService(ApiInterface.class);
            Call<AddLogPojo> call = service.addLogs(data);
            call.enqueue(new Callback<AddLogPojo>() {
                @Override
                public void onResponse(Call<AddLogPojo> call, Response<AddLogPojo> response) {
                    if (response.isSuccessful()) {
                        // hideLoader();
                        hideProgress();
                        dialog.dismiss();
                        choose_file_layout.setVisibility(View.VISIBLE);
                        log_img_card.setVisibility(View.GONE);
                        logspojo.setText(response.body().getData().getText());
                        logspojo.setImage(response.body().getData().getImage());
                        logs.add(logspojo);
                        adapter.addItems(logs);
                       // allLogsApi();
                        if (!response.body().getData().getText().toString().isEmpty()) {
                            txt_count += 1;
                        } else {
                            txt_count += 0;

                        }

                        if (!response.body().getData().getImage().toString().isEmpty()) {
                            img_count += 1;
                        } else {
                            img_count += 0;

                        }


                    } else {
                        //hideLoader();
                        hideProgress();
                        Toast.makeText(LogsActivity.this, getApplicationContext().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onFailure(Call<AddLogPojo> call, Throwable t) {
                    // hideLoader();
                    hideProgress();
                    Log.d("otp", t.getMessage());
                    Toast.makeText(LogsActivity.this, "fail", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            // hideLoader();
            hideProgress();
            customDialog(getApplicationContext().getString(R.string.no_internet), LogsActivity.this);

        }

    }

    public void customImagePicker() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.image_picker_dialog);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog.show();


    }

    public void allLogsApi() {
        // showLoader(LogsActivity.this);
        showProgress();
        if (isNetworkAvailable()) {
            ApiInterface service = BaseUrl.CreateService(ApiInterface.class);
            Call<AllLogsPojo> call = service.allLogs("2", datum.getId(), tasks.getId());
            call.enqueue(new Callback<AllLogsPojo>() {
                @Override
                public void onResponse(Call<AllLogsPojo> call, Response<AllLogsPojo> response) {
                    if (response.isSuccessful()) {
                        // hideLoader();
                        hideProgress();
                        adapter = new TaskLogsAdapter(response.body().getData(), LogsActivity.this);
                        recyclerView.setAdapter(adapter);
                    } else {
                        //hideLoader();
                        hideProgress();
                        Toast.makeText(LogsActivity.this, getApplicationContext().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();


                    }
                }

                @Override
                public void onFailure(Call<AllLogsPojo> call, Throwable t) {
                    // hideLoader();
                    hideProgress();
                    Log.d("otp", t.getMessage());
                    Toast.makeText(LogsActivity.this, getApplicationContext().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            // hideLoader();
            hideProgress();
            customDialog(getApplicationContext().getString(R.string.no_internet), LogsActivity.this);

        }

    }


}
