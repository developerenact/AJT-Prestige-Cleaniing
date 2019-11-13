package com.android.ajtprestigecleaning.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
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
import com.android.ajtprestigecleaning.model.ResetPassword.ResetPassword;
import com.android.ajtprestigecleaning.util.Constants;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.Dash;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

public class DashboardActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView nav_contactus, nav_terms, nav_privacy, nav_logout, nav_name, nav_change_pass, nav_profile, nav_workhistory, nav_settings, nav_about, nav_help;
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
    boolean doubleBackToExitPressedOnce = false;
    int pos = 0;
    Typeface custom_font;
    SwipeRefreshLayout swipeRefreshLayout;
    public boolean showFirstTime = true;
    int state = 0;
    BottomNavigationView bottomNavigationView;
    String refreshedToken="null";
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();
        login_Username = Paper.book().read(Constants.FIRSTNAME, "john") + " " + Paper.book().read(Constants.LASTNAME, "Doe");
        nav_name.setText(login_Username);
        String imageUrl = Paper.book().read(Constants.USERIMAGE);
        Glide.with(DashboardActivity.this).load(imageUrl.isEmpty() ? "" : imageUrl).placeholder(R.drawable.demoprofile).into(nav_image);
        unSelectItem(nav_workhistory);
        unSelectItem(nav_change_pass);
        unSelectItem(nav_profile);
        unSelectItem(nav_about);
        unSelectItem(nav_help);
        unSelectItem(nav_terms);
        unSelectItem(nav_privacy);
        unSelectItem(nav_contactus);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        frameLayout = findViewById(R.id.frame);
        swipeRefreshLayout=findViewById(R.id.pullToRefresh);
        checkpermission();
        hsvLayout = findViewById(R.id.hsvLinearLayout);
        activity = DashboardActivity.this;
        label_alljobs = findViewById(R.id.label_alljobs);
        nav_change_pass = findViewById(R.id.changepassword);
        alljobs_arrow = findViewById(R.id.alljobs_arrow);
        nav_profile = findViewById(R.id.profile);
        nav_workhistory = findViewById(R.id.work_history);
        nav_about = findViewById(R.id.about);
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        nav_help = findViewById(R.id.help);
        horizontalScrollView = findViewById(R.id.horizontal_scroll);
        bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);
        int[] image_array = new int[]{R.mipmap.all_jobs, R.mipmap.in_progess, R.mipmap.upcpming_jobs, R.mipmap.past_jobs, R.mipmap.rejected_jobs, R.mipmap.completed_jobs};
        String[] category_name = new String[]{"All jobs", "In Progress", "Upcoming Jobs", "Past jobs", "Rejected jobs", "Completed jobs"};
        if (manager == null) manager = getSupportFragmentManager();
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        nav_name = findViewById(R.id.nav_name);
        nav_image = findViewById(R.id.nav_imageView);
        nav_contactus = findViewById(R.id.contactus);
        nav_terms = findViewById(R.id.terms);
        nav_privacy = findViewById(R.id.privacy);
        nav_logout = findViewById(R.id.logout);
        custom_font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Montserrat-Medium.ttf");
        nav_name.setTypeface(custom_font);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fragment.getjobs(state, DashboardActivity.this);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        final ArrayList<TextView> textViewList = new ArrayList<>();
        final ArrayList<ImageView> category_imgview = new ArrayList<>();
        ArrayList<View> category_view = new ArrayList<>();
        for (int i = 0; i < category_name.length; i++) {
            View main_view = LayoutInflater.from(this).inflate(R.layout.categoty_layout, null);
            //  LinearLayout categoryMainLayout = main_view.findViewById(R.id.categoryMainLayout);

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
                    switch (finalI){
                        case 0:
                            state=0;
                            break;
                        case 1:
                            state=Constants.INPROGRESS;
                            break;
                        case 2:
                            state=Constants.UPCOMING;
                            break;
                        case 3:
                            state=Constants.PAST;
                            break;
                        case 4:
                            state=Constants.REJECTED;
                            break;
                        case 5:
                            state=Constants.COMPLETED;
                            break;
                    }
                    fragment.getjobs(state, DashboardActivity.this);
                   // state = finalI;
                    for (TextView tempItemView : textViewList) {
                        if (textViewList.get(finalI) == tempItemView) {
                            tempItemView.setTextColor(getResources().getColor(R.color.white));
                        } else {
                            tempItemView.setTextColor(getResources().getColor(R.color.unselectcategory));
                        }
                    }

                    for (ImageView tempimageView : category_imgview) {
                        if (category_imgview.get(finalI) == tempimageView) {
                            tempimageView.setBackground(getResources().getDrawable(R.drawable.selected_circle_bg));
                        } else {
                            tempimageView.setBackground(getResources().getDrawable(R.drawable.circle_bg));
                        }
                    }


                }
            });


        }
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        nav_profile.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                unSelectItem(nav_workhistory);
                unSelectItem(nav_change_pass);
                unSelectItem(nav_contactus);
                unSelectItem(nav_about);
                unSelectItem(nav_help);
                unSelectItem(nav_terms);
                unSelectItem(nav_privacy);
                selectItem(nav_profile);
                Intent intent = new Intent(DashboardActivity.this, UpdateProfileActivity.class);
                startActivity(intent);
                drawer.closeDrawer(GravityCompat.START);

            }
        });


        nav_contactus.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                unSelectItem(nav_workhistory);
                unSelectItem(nav_change_pass);
                unSelectItem(nav_profile);
                unSelectItem(nav_about);
                unSelectItem(nav_help);
                unSelectItem(nav_terms);
                unSelectItem(nav_privacy);
                selectItem(nav_contactus);
                Intent intent = new Intent(DashboardActivity.this, ContactUsActivity.class);
                startActivity(intent);
                drawer.closeDrawer(GravityCompat.START);

            }
        });

        nav_change_pass.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                unSelectItem(nav_workhistory);
                unSelectItem(nav_profile);
                unSelectItem(nav_contactus);
                unSelectItem(nav_about);
                unSelectItem(nav_help);
                unSelectItem(nav_terms);
                unSelectItem(nav_privacy);
                selectItem(nav_change_pass);
                Intent intent = new Intent(DashboardActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
                drawer.closeDrawer(GravityCompat.START);

            }
        });

        nav_workhistory.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                unSelectItem(nav_profile);
                unSelectItem(nav_contactus);
                unSelectItem(nav_about);
                unSelectItem(nav_change_pass);
                unSelectItem(nav_help);
                unSelectItem(nav_terms);
                unSelectItem(nav_privacy);
                selectItem(nav_workhistory);
                Intent intent = new Intent(DashboardActivity.this, WorkHistoryActivity.class);
                startActivity(intent);
                drawer.closeDrawer(GravityCompat.START);

            }
        });


        nav_about.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                unSelectItem(nav_profile);
                unSelectItem(nav_workhistory);
                unSelectItem(nav_contactus);
                unSelectItem(nav_change_pass);
                unSelectItem(nav_help);
                unSelectItem(nav_terms);
                unSelectItem(nav_privacy);
                selectItem(nav_about);
                Intent intent = new Intent(DashboardActivity.this, TermsActivity.class);
                intent.putExtra("url", "https://enacteservices.net/AJT/POST/webLinks/aboutUs.php");
                intent.putExtra("title", "About");
                startActivity(intent);
                drawer.closeDrawer(GravityCompat.START);


            }
        });

        nav_help.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                unSelectItem(nav_profile);
                unSelectItem(nav_workhistory);
                unSelectItem(nav_contactus);
                unSelectItem(nav_change_pass);
                unSelectItem(nav_about);
                unSelectItem(nav_terms);
                unSelectItem(nav_privacy);
                selectItem(nav_help);
                Intent intent = new Intent(DashboardActivity.this, TermsActivity.class);
                intent.putExtra("url", "https://enacteservices.net/AJT/POST/webLinks/faq.php");
                intent.putExtra("title", "Help/FAQ");
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


        nav_terms.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                unSelectItem(nav_workhistory);
                unSelectItem(nav_change_pass);
                unSelectItem(nav_contactus);
                unSelectItem(nav_about);
                unSelectItem(nav_help);
                unSelectItem(nav_profile);
                unSelectItem(nav_privacy);
                selectItem(nav_terms);
                Intent intent = new Intent(DashboardActivity.this, TermsActivity.class);
                intent.putExtra("url", "https://enacteservices.net/AJT/POST/webLinks/terms.php");
                intent.putExtra("title", "Terms");
                startActivity(intent);
                drawer.closeDrawer(GravityCompat.START);

            }
        });


        nav_privacy.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                unSelectItem(nav_workhistory);
                unSelectItem(nav_change_pass);
                unSelectItem(nav_contactus);
                unSelectItem(nav_about);
                unSelectItem(nav_help);
                unSelectItem(nav_terms);
                unSelectItem(nav_profile);
                selectItem(nav_privacy);
                Intent intent = new Intent(DashboardActivity.this, TermsActivity.class);
                intent.putExtra("url", "https://enacteservices.net/AJT/POST/webLinks/policy.php");
                intent.putExtra("title", "Privacy Policy");
                startActivity(intent);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        nav_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIosDialog();


            }
        });


        fragment = new JobsFragment();
        manager.beginTransaction().replace(R.id.frame, fragment).commit();
        if (fragment != null) {
            fragment.getjobs(0, DashboardActivity.this);
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        item.setChecked(true);
                        switch (item.getItemId()) {
                            case R.id.jobs:
                                manager.beginTransaction().replace(R.id.frame, fragment).commit();
                                if (fragment != null) {
                                    fragment.getjobs(0, DashboardActivity.this);
                                }
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
    public int getLayoutResourceId() {
        return R.layout.activity_dashboard;
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Tap again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
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
        text.setTypeface(custom_font);
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
        // showLoader(DashboardActivity.this);
        showProgress();
        if (isNetworkAvailable()) {
            ApiInterface service = BaseUrl.CreateService(ApiInterface.class);
            Call<JsonObject> call = service.logout(Paper.book().read(Constants.USERID, "2"), refreshedToken);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        //  hideLoader();
                        hideProgress();
                        dialog.dismiss();
                        Paper.book().delete(Constants.ISLOGIN);
                        Paper.book().delete(Constants.USERNAME);
                        Paper.book().delete(Constants.EMAIL);
                        Paper.book().delete(Constants.USERID);
                        Paper.book().delete(Constants.FIRSTNAME);
                        Paper.book().delete(Constants.LASTNAME);
                        Paper.book().delete(Constants.USERIMAGE);
                        Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // hideLoader();
                        hideProgress();
                        Toast.makeText(DashboardActivity.this, getApplicationContext().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();


                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    // hideLoader();
                    hideProgress();
                    Log.d("otp", t.getMessage());
                    Toast.makeText(DashboardActivity.this, getApplicationContext().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            //  hideLoader();
            hideProgress();
            customDialog(getApplicationContext().getString(R.string.no_internet), DashboardActivity.this);

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void selectItem(TextView textView) {
        textView.setBackground(getResources().getDrawable(R.drawable.profile_border));
        textView.setTextColor(getResources().getColor(R.color.white));
        textView.getCompoundDrawables()[0].setTint(getResources().getColor(R.color.white));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void unSelectItem(TextView textView) {
        textView.setBackground(null);
        textView.setTextColor(getResources().getColor(R.color.draweritemcolour));
        // textView.setPadding(12, 12, 64, 12);
        textView.getCompoundDrawables()[0].setTint(getResources().getColor(R.color.draweritemcolour));

    }

    public void updateadapter() {
        fragment.update(state);
    }


}
