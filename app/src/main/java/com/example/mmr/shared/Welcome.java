package com.example.mmr.shared;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mmr.R;

public class Welcome extends AppCompatActivity {
    public static int SPLASH_SCREEN =2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.top_anim);
        Animation bottomAnim = AnimationUtils.loadAnimation(this, R.anim.down_anim);

        ImageView img = (ImageView) findViewById(R.id.client_logo);
        TextView msg=(TextView) findViewById(R.id.msg_welcome);

        img.setAnimation(topAnim);
        msg.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(Welcome.this,TypeUser.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);

    }
}