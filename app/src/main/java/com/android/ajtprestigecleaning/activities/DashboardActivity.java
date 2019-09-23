package com.android.ajtprestigecleaning.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.android.ajtprestigecleaning.util.Constants;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

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

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends BaseActivityk
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView contactus,terms,privacy,logout,nav_name;
    String login_Username;
     JobsFragment fragment;
    FragmentManager manager;
    FrameLayout frameLayout;
    LinearLayout hsvLayout;
    TextView label_alljobs;
    ImageView alljobs_arrow;
    Activity activity;
    HorizontalScrollView horizontalScrollView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        frameLayout=findViewById(R.id.frame);
        hsvLayout=findViewById(R.id.hsvLinearLayout);
        activity=DashboardActivity.this;
        label_alljobs=findViewById(R.id.label_alljobs);
        alljobs_arrow=findViewById(R.id.alljobs_arrow);
        horizontalScrollView=findViewById(R.id.horizontal_scroll);
        int[] image_array = new int[]{R.mipmap.all_jobs,R.mipmap.in_progess,R.mipmap.upcpming_jobs,R.mipmap.past_jobs,R.mipmap.rejected_jobs};
        String[] category_name = new String[]{"All jobs","In Progress","Upcoming Jobs","Past jobs","Rejected jobs"};
        if (manager == null) manager = getSupportFragmentManager();
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        login_Username=Paper.book().read(Constants.USERNAME,"UserName");
        nav_name=findViewById(R.id.nav_name);
        contactus=findViewById(R.id.contactus);
        terms=findViewById(R.id.terms);
        privacy=findViewById(R.id.privacy);
        logout=findViewById(R.id.logout);
        Typeface custom_font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Montserrat-Medium.ttf");
        nav_name.setTypeface(custom_font);
        nav_name.setText(login_Username);
        ArrayList<TextView> textViewList=new ArrayList<>();
        ArrayList<ImageView> category_imgview=new ArrayList<>();
        ArrayList<View> category_view=new ArrayList<>();
        for (int i=0;i<category_name.length;i++) {
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
                    fragment.getjobs(finalI,activity);


                }
            });


        }
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DashboardActivity.this,ContactUsActivity.class);
                startActivity(intent);
                drawer.closeDrawer(GravityCompat.START);

            }
        });

        label_alljobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(horizontalScrollView.getVisibility()==View.VISIBLE){
                    horizontalScrollView.setVisibility(View.GONE);
                    alljobs_arrow.setImageResource(R.mipmap.small_white_arrow);
                }
                else {
                    horizontalScrollView.setVisibility(View.VISIBLE);
                    alljobs_arrow.setImageResource(R.mipmap.small_white_arrow_up);
                }


            }
        });

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DashboardActivity.this,TermsActivity.class);
                startActivity(intent);
                drawer.closeDrawer(GravityCompat.START);

            }
        });


        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DashboardActivity.this,PrivacyActivity.class);
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
        manager.beginTransaction().add(R.id.frame,fragment).commit();
        fragment.getjobs(0,activity);


        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_favorites:
                                fragment = new JobsFragment();
                                manager.beginTransaction().replace(R.id.frame,fragment).commit();
                                fragment.getjobs(0,activity);
                                break;
                            case R.id.action_schedules:
                                break;
                            case R.id.action_music:
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

    public void showIosDialog(){
        final Dialog dialog = new Dialog(this);
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
                Paper.book().delete(Constants.ISLOGIN);
                Paper.book().delete(Constants.USERNAME);
                Paper.book().delete(Constants.EMAIL);
                Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        dialog.show();


    }




}
