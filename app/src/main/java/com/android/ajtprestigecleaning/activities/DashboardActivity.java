package com.android.ajtprestigecleaning.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import com.android.ajtprestigecleaning.R;
import com.android.ajtprestigecleaning.adapter.JobsAdapter;
import com.android.ajtprestigecleaning.apiServices.ApiInterface;
import com.android.ajtprestigecleaning.apiServices.BaseUrl;
import com.android.ajtprestigecleaning.fragments.JobsFragment;
import com.android.ajtprestigecleaning.model.JobListPojo.JobListPojo;
import com.android.ajtprestigecleaning.model.ResetPassword.ResetPassword;
import com.android.ajtprestigecleaning.util.Constants;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends BaseActivityk
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView contactus, terms, privacy, logout, nav_name, change_pass, tv_profile;
    CircleImageView nav_image;
    String login_Username;
    JobsFragment fragment;
    FragmentManager manager;
    FrameLayout frameLayout;
    LinearLayout hsvLayout;
    TextView label_alljobs;
    ImageView alljobs_arrow;
    Activity activity;
    Dialog dialog;
    HorizontalScrollView horizontalScrollView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        frameLayout = findViewById(R.id.frame);
        checkpermission();
        hsvLayout = findViewById(R.id.hsvLinearLayout);
        activity = DashboardActivity.this;
        label_alljobs = findViewById(R.id.label_alljobs);
        change_pass = findViewById(R.id.changepassword);
        alljobs_arrow = findViewById(R.id.alljobs_arrow);
        tv_profile = findViewById(R.id.profile);
        horizontalScrollView = findViewById(R.id.horizontal_scroll);
        int[] image_array = new int[]{R.mipmap.all_jobs, R.mipmap.in_progess, R.mipmap.upcpming_jobs, R.mipmap.past_jobs, R.mipmap.rejected_jobs, R.mipmap.completed_jobs};
        String[] category_name = new String[]{"All jobs", "In Progress", "Upcoming Jobs", "Past jobs", "Rejected jobs", "Completed jobs"};
        if (manager == null) manager = getSupportFragmentManager();
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        login_Username = Paper.book().read(Constants.FIRSTNAME, "john") + " " + Paper.book().read(Constants.LASTNAME, "Doe");
        nav_name = findViewById(R.id.nav_name);
        nav_image = findViewById(R.id.nav_imageView);
        contactus = findViewById(R.id.contactus);
        terms = findViewById(R.id.terms);
        privacy = findViewById(R.id.privacy);
        logout = findViewById(R.id.logout);
        Typeface custom_font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Montserrat-Medium.ttf");
        nav_name.setTypeface(custom_font);
        nav_name.setText(login_Username);
        String imageUrl = Paper.book().read(Constants.USERIMAGE);
        if (!imageUrl.isEmpty()) {
            Picasso.with(getApplicationContext()).load(imageUrl).placeholder(R.drawable.demoprofile).error(R.drawable.demoprofile).into(nav_image);
        } else {
            Picasso.with(getApplicationContext()).load(R.drawable.demoprofile).into(nav_image);

        }

        ArrayList<TextView> textViewList = new ArrayList<>();
        ArrayList<ImageView> category_imgview = new ArrayList<>();
        ArrayList<View> category_view = new ArrayList<>();
        for (int i = 0; i < category_name.length; i++) {
            View main_view = LayoutInflater.from(this).inflate(R.layout.categoty_layout, null);
            LinearLayout categoryMainLayout = main_view.findViewById(R.id.categoryMainLayout);

            final ImageView cat_img = main_view.findViewById(R.id.img);
            TextView cat_text = main_view.findViewById(R.id.text);
            cat_text.setText(category_name[i]);
            cat_img.setImageResource(image_array[i]);
            textViewList.add(cat_text);
            category_imgview.add(cat_img);
            category_view.add(main_view);
            hsvLayout.addView(main_view);


            final int finalI = i;
            main_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment.getjobs(finalI, activity);


                }
            });


        }
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        tv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, UpdateProfileActivity.class);
                startActivity(intent);
                drawer.closeDrawer(GravityCompat.START);

            }
        });


        contactus.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
              //  changeBackground(contactus);
                Intent intent = new Intent(DashboardActivity.this, ContactUsActivity.class);
                startActivity(intent);
                drawer.closeDrawer(GravityCompat.START);

            }
        });

        change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
                drawer.closeDrawer(GravityCompat.START);

            }
        });

        label_alljobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (horizontalScrollView.getVisibility() == View.VISIBLE) {
                    horizontalScrollView.setVisibility(View.GONE);
                    alljobs_arrow.setImageResource(R.mipmap.small_white_arrow);
                } else {
                    horizontalScrollView.setVisibility(View.VISIBLE);
                    alljobs_arrow.setImageResource(R.mipmap.small_white_arrow_up);
                }


            }
        });

        alljobs_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (horizontalScrollView.getVisibility() == View.VISIBLE) {
                    horizontalScrollView.setVisibility(View.GONE);
                    alljobs_arrow.setImageResource(R.mipmap.small_white_arrow);
                } else {
                    horizontalScrollView.setVisibility(View.VISIBLE);
                    alljobs_arrow.setImageResource(R.mipmap.small_white_arrow_up);
                }


            }
        });


        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, TermsActivity.class);
                startActivity(intent);
                drawer.closeDrawer(GravityCompat.START);

            }
        });


        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, PrivacyActivity.class);
                startActivity(intent);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIosDialog();


            }
        });

        fragment = new JobsFragment();
        manager.beginTransaction().add(R.id.frame, fragment).commit();
        fragment.getjobs(0, activity);


        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);


        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        item.setChecked(true);
                        switch (item.getItemId()) {
                            case R.id.jobs:
                                fragment = new JobsFragment();
                                manager.beginTransaction().replace(R.id.frame, fragment).commit();
                                fragment.getjobs(0, activity);
                                break;
                            case R.id.reports:
                                break;
                            case R.id.more:
                                break;
                        }
                        return false;
                    }
                });
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_dashboard;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int id) {
                            finishAffinity();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public boolean onItemSelected(TextView textView){
        textView.setSelected(true);
        int id = textView.getId();

        if (id == R.id.profile) {

        } else if (id == R.id.contactus) {

        } else if (id == R.id.about) {

        } else if (id == R.id.privacy) {

        } else if (id == R.id.terms) {

        } else if (id == R.id.changepassword) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public void showIosDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.logout_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView text = (TextView) dialog.findViewById(R.id.ios_text_logout);
        TextView dialogButton = (TextView) dialog.findViewById(R.id.ios_cancel);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView dialogButton_logout = (TextView) dialog.findViewById(R.id.ios_logout);
        dialogButton_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogout();

            }
        });

        dialog.show();


    }


    private void checkpermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 5);

            } else {
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 5: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                return;
            }
        }
    }


    public void userLogout() {
        showLoader(DashboardActivity.this);
        if (isNetworkConnected(DashboardActivity.this)) {
            ApiInterface service = BaseUrl.CreateService(ApiInterface.class);
            Call<JsonObject> call = service.logout(Paper.book().read(Constants.USERID, ""), FirebaseInstanceId.getInstance().getToken());
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        hideLoader();
                        dialog.dismiss();
                        Paper.book().delete(Constants.ISLOGIN);
                        Paper.book().delete(Constants.USERNAME);
                        Paper.book().delete(Constants.EMAIL);
                        Paper.book().delete(Constants.USERID);
                        Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        hideLoader();
                        Toast.makeText(DashboardActivity.this, getApplicationContext().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();


                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    hideLoader();
                    Log.d("otp", t.getMessage());
                    Toast.makeText(DashboardActivity.this, getApplicationContext().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            hideLoader();
            customDialog(DashboardActivity.this, getApplicationContext().getString(R.string.no_internet));

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void changeBackground(TextView textView) {
        textView.setBackground(getResources().getDrawable(R.drawable.profile_border));
        textView.setTextColor(getResources().getColor(R.color.white));
        textView.setPadding(8, 16, 64, 16);
        textView.getCompoundDrawables()[0].setTint(getResources().getColor(R.color.white));
    }

}
