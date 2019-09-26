package com.android.ajtprestigecleaning.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.ajtprestigecleaning.R;
import com.android.ajtprestigecleaning.apiServices.ApiInterface;
import com.android.ajtprestigecleaning.apiServices.BaseUrl;
import com.android.ajtprestigecleaning.model.AddLogPojo.AddLogPojo;
import com.android.ajtprestigecleaning.model.UpdateProfilePojo.UpdateProfilePojo;
import com.android.ajtprestigecleaning.util.Constants;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

import io.paperdb.Paper;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LogsActivity extends BaseActivityk {
    Dialog dialog;
    ImageView add_logs, back;
    Uri selectedUri;
    Bitmap bitmap;
    File file;
    boolean permissionStatus = false;
    LinearLayout linearLayout;
    EditText et_note;
    Button submit;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkpermission();
        add_logs = findViewById(R.id.add_logs_btn);
        back = findViewById(R.id.back);
        add_logs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomdialog();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected int getLayoutResourceId() {
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
        linearLayout=dialog.findViewById(R.id.choose_file_layout);
        et_note=dialog.findViewById(R.id.et_note);
        submit=dialog.findViewById(R.id.submit);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showimagePicker();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLog("1","2","1",et_note.getText().toString(),file);
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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




        public void addLog (String jobId, String userId, String taskId, String
        text, File image){

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
            showLoader(LogsActivity.this);
            if (isNetworkConnected(LogsActivity.this)) {
                ApiInterface service = BaseUrl.CreateService(ApiInterface.class);
                Call<AddLogPojo> call = service.addLogs(data);
                call.enqueue(new Callback<AddLogPojo>() {
                    @Override
                    public void onResponse(Call<AddLogPojo> call, Response<AddLogPojo> response) {
                        if (response.isSuccessful()) {
                            hideLoader();
                            dialog.dismiss();

                        } else {
                            hideLoader();
                            Toast.makeText(LogsActivity.this, getApplicationContext().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<AddLogPojo> call, Throwable t) {
                        hideLoader();
                        Log.d("otp", t.getMessage());
                        Toast.makeText(LogsActivity.this, "fail", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                hideLoader();
                customDialog(LogsActivity.this, getApplicationContext().getString(R.string.no_internet));

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


}