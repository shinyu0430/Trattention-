package com.github.pwittchen.neurosky.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class AttentionTestHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        setContentView(R.layout.activity_attention_test_home);

        Button nextPageBtn = (Button)findViewById(R.id.btn_home_start);
        Intent intent = getIntent();
        String attention_value = intent.getStringExtra("attention");
        TextView tv1=(TextView) findViewById(R.id.textView4);
        tv1.setText(attention_value);

        nextPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(AttentionTestHome.this , AttentionTestIntro.class);
                startActivity(intent);
            }
        });

    }

}