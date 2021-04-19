package com.example.mars.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mars.R;
import com.example.mars.util.MyPreferences;

public class ActivityCertificate extends AppCompatActivity {
    TextView tv_name,tv_course1,tv_course2,tv_date;
    MyPreferences preferences;
    Button btnThankYou;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cirtificate);
        tv_name=findViewById(R.id.tv_name);
        tv_course1=findViewById(R.id.tv_course1);
        tv_course2=findViewById(R.id.tv_course2);
        tv_date=findViewById(R.id.tv_date);
        btnThankYou=findViewById(R.id.btnThankYou);
        preferences=new MyPreferences(this);

        tv_name.setText(preferences.getString(MyPreferences.USER_NAME));
        tv_course1.setText("Has Completed the "+getIntent().getStringExtra("course")+" training \n course for ");

        btnThankYou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
