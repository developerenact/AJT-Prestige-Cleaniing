package com.android.ajtprestigecleaning.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.ajtprestigecleaning.R;

public class ContactUsActivity extends BaseActivity {
    ImageView back;
    TextView label_us,label_contact,toolbar_label;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(ContactUsActivity.this,R.color.white));
        }
        back=findViewById(R.id.back);
        label_us=findViewById(R.id.label_us);
        toolbar_label=findViewById(R.id.toolbar_label);
        label_contact=findViewById(R.id.label_contact);
        Typeface custom_font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Montserrat-Medium.ttf");
        label_us.setTypeface(custom_font);
        toolbar_label.setTypeface(custom_font);

        Typeface custom_font2 = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Light.ttf");
        label_contact.setTypeface(custom_font2);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public int getLayoutResourceId() {
       /* requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        return R.layout.activity_contact_us;
    }
}
