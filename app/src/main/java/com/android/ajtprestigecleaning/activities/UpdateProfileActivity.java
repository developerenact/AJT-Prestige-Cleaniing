package com.android.ajtprestigecleaning.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ajtprestigecleaning.R;
import com.android.ajtprestigecleaning.apiServices.ApiInterface;
import com.android.ajtprestigecleaning.apiServices.BaseUrl;
import com.android.ajtprestigecleaning.model.ResetPassword.ResetPassword;
import com.android.ajtprestigecleaning.model.UpdateProfilePojo.UpdateProfilePojo;
import com.android.ajtprestigecleaning.util.Constants;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfileActivity extends BaseActivity {
    ImageView back, update_user, edit;
    EditText et_fname, et_lname, et_phone;
    CircleImageView profile_img;
    Uri selectedUri;
    Bitmap bitmap;
    File file;
    boolean permissionStatus = false;
    TextView toolbar_label;
    DialogInterface dialogInterface;
    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(UpdateProfileActivity.this, R.color.white));
        }
        checkpermission();
        back = findViewById(R.id.back);
        profile_img = findViewById(R.id.profile_img);
        et_fname = findViewById(R.id.et_update_fname);
        et_lname = findViewById(R.id.et_update_lname);
        et_phone = findViewById(R.id.et_update_phone);
        update_user = findViewById(R.id.updateuser);
        toolbar_label = findViewById(R.id.toolbar_label);
        Typeface custom_font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Montserrat-Medium.ttf");
        toolbar_label.setTypeface(custom_font);
        edit = findViewById(R.id.edt_btn);
        profile_img.setEnabled(false);
        update_user.setEnabled(false);
        getProfile();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlert();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_fname.setEnabled(true);
                et_lname.setEnabled(true);
                et_phone.setEnabled(true);
                profile_img.setEnabled(true);
                update_user.setEnabled(true);
                edit.setVisibility(View.GONE);
                update_user.setVisibility(View.VISIBLE);
            }
        });


        update_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_fname.getText().toString().isEmpty()) {
                    et_fname.setError(getApplicationContext().getString(R.string.firstname_validation));
                    et_fname.requestFocus();
                } else if (et_lname.getText().toString().isEmpty()) {
                    et_lname.setError(getApplicationContext().getString(R.string.lastname_validation));
                    et_lname.requestFocus();
                } else if (et_phone.getText().toString().isEmpty()) {
                    et_phone.setError(getApplicationContext().getString(R.string.emptyphone_validation));
                    et_phone.requestFocus();
                } else if (et_phone.getText().toString().length() < 10) {
                    et_phone.setError(getApplicationContext().getString(R.string.phone_length_validation));
                    et_phone.requestFocus();
                } else {
                    updateProfile(Paper.book().read(Constants.USERID, "2"), et_fname.getText().toString(), et_lname.getText().toString(), et_phone.getText().toString(), file, "en");
                }
            }
        });
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_update_profile;
    }

    private void showAlert() {
        final CharSequence[] charSequence = {"Camera", "Gallery", "Cancel"};
        final AlertDialog.Builder alert = new AlertDialog.Builder(UpdateProfileActivity.this);
        alert.setCancelable(true);
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
               // profile_img.setImageBitmap(bitmap);
                Glide.with(UpdateProfileActivity.this)
                        .load(bitmap)
                        .placeholder(R.drawable.demoprofile)
                        .into(profile_img);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    private void getGalleryImage(Intent data) {

        Uri uri = data.getData();
        String path = getRealPathFromURI_API19(uri);
        //  String path = getRealPathFromUri(UpdateProfileActivity.this,uri);
        file = new File(path);
        selectedUri = Uri.fromFile(file);
// image.setImageURI(uri);
        Log.e("seletedpath ", "gal : " + selectedUri);
        if (selectedUri != null) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedUri);
                // profile_img.setImageBitmap(bitmap);
                Glide.with(UpdateProfileActivity.this)
                        .load(bitmap)
                        .placeholder(R.drawable.demoprofile)
                        .into(profile_img);
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

        AlertDialog.Builder alert = new AlertDialog.Builder(UpdateProfileActivity.this);
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

                UpdateProfileActivity.this.finish();
            }
        });
        alert.show();
    }


    public void updateProfile(String userId, String firstname, String lastName, String phone, File profileimage, String lang) {


        HashMap<String, RequestBody> data = new HashMap<>();
        RequestBody rb_userId = RequestBody.create(MediaType.parse("text/plain"), userId);
        RequestBody rb_fname = RequestBody.create(MediaType.parse("text/plain"), firstname);
        RequestBody rb_lname = RequestBody.create(MediaType.parse("text/plain"), lastName);
        RequestBody rb_phone = RequestBody.create(MediaType.parse("text/plain"), phone);
        RequestBody rb_lang = RequestBody.create(MediaType.parse("text/plain"), lang);

        data.put("userId", rb_userId);
        data.put("firstName", rb_fname);
        data.put("lastName", rb_lname);
        data.put("phone", rb_phone);
        data.put("lang", rb_lang);

        if (profileimage != null) {
            RequestBody rb_img = RequestBody.create(MediaType.parse("image/*"), profileimage);
            data.put("image\"; filename=\"" + profileimage.getName(), rb_img);

        }


        //  showLoader(UpdateProfileActivity.this);
        showProgress();
        if (isNetworkAvailable()) {
            ApiInterface service = BaseUrl.CreateService(ApiInterface.class);
            Call<UpdateProfilePojo> call = service.updateProfile(data);
            call.enqueue(new Callback<UpdateProfilePojo>() {
                @Override
                public void onResponse(Call<UpdateProfilePojo> call, Response<UpdateProfilePojo> response) {
                    if (response.isSuccessful()) {
                        // hideLoader();
                        hideProgress();
                        edit.setVisibility(View.VISIBLE);
                        et_fname.setEnabled(false);
                        et_lname.setEnabled(false);
                        et_phone.setEnabled(false);
                        profile_img.setEnabled(false);
                        update_user.setEnabled(false);
                        update_user.setVisibility(View.GONE);
                        if (!response.body().getData().getImage().isEmpty()) {
                            Glide.with(UpdateProfileActivity.this)
                                    .load(bitmap == null ? response.body().getData().getImage() : bitmap)
                                    .placeholder(R.drawable.demoprofile)
                                    .into(profile_img);
                            //Picasso.with(getApplicationContext()).load(response.body().getData().getImage()).placeholder(R.drawable.demoprofile).error(R.drawable.demoprofile).into(profile_img);
                        }
                        et_fname.setText(response.body().getData().getFirstName());
                        et_lname.setText(response.body().getData().getLastName());
                        et_phone.setText(response.body().getData().getPhone());
                        Paper.book().write(Constants.USERIMAGE, response.body().getData().getImage());
                        Paper.book().write(Constants.FIRSTNAME, response.body().getData().getFirstName());
                        Paper.book().write(Constants.LASTNAME, response.body().getData().getLastName());
                        Paper.book().write(Constants.NUMBER, response.body().getData().getPhone());
                    } else {
                        // hideLoader();
                        hideProgress();
                        Toast.makeText(UpdateProfileActivity.this, getApplicationContext().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();

                    }
                }


                @Override
                public void onFailure(Call<UpdateProfilePojo> call, Throwable t) {
                    //hideLoader();
                    hideProgress();
                    Log.d("otp", t.getMessage());
                    Toast.makeText(UpdateProfileActivity.this, "fail", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            //  hideLoader();
            hideProgress();
            customDialog(getApplicationContext().getString(R.string.no_internet), UpdateProfileActivity.this);

        }

    }

    public void getProfile() {
        String imageUrl = Paper.book().read(Constants.USERIMAGE);
        et_fname.setText(Paper.book().read(Constants.FIRSTNAME, "John"));
        et_lname.setText(Paper.book().read(Constants.LASTNAME, "Doe"));
        et_phone.setText(Paper.book().read(Constants.NUMBER, "1234567890"));


        Glide.with(UpdateProfileActivity.this).load(imageUrl.isEmpty() ? "" : imageUrl).placeholder(R.drawable.demoprofile).into(profile_img);
        // Picasso.with(getApplicationContext()).load(imageUrl).placeholder(R.drawable.demoprofile).error(R.drawable.demoprofile).into(profile_img);


    }


}
