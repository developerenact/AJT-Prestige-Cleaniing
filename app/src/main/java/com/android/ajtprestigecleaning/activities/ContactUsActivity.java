package com.android.ajtprestigecleaning.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.ajtprestigecleaning.R;

public class ContactUsActivity extends BaseActivityk {
    ImageView back;
    TextView label_us,label_contact;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        back=findViewById(R.id.back);
        label_us=findViewById(R.id.label_us);
        label_contact=findViewById(R.id.label_contact);
        Typeface custom_font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Montserrat-Medium.ttf");
        label_us.setTypeface(custom_font);

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
    protected int getLayoutResourceId() {
       /* requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        return R.layout.activity_contact_us;
    }
}
