package com.android.ajtprestigecleaning.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;

import com.kaopiz.kprogresshud.KProgressHUD;

import io.paperdb.Paper;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public abstract class BaseActivityk extends AppCompatActivity {
    public static KProgressHUD kProgressHUD;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        Paper.init(this);

    }


    protected abstract int getLayoutResourceId();

    public static void showAlert(final Activity activity, final String message, final String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message).setTitle(title).setCancelable(false)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        // activity.finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static boolean isNetworkConnected(Context context) {
        boolean connected = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        connected = (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm
                .getActiveNetworkInfo().isConnected());
        return connected;
    }


    public static void showLoader(Activity activity) {

        kProgressHUD = new KProgressHUD(activity).create(activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
    }

    public static void hideLoader() {
        kProgressHUD.dismiss();

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}