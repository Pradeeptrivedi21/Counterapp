package com.dev.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class NotificationMessage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_message);
        onShakeImage();
    }
    public void onShakeImage() {
        Animation shake;
        shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.watchvibrate);

        ImageView image;
        image = (ImageView) findViewById(R.id.image_view);

        image.startAnimation(shake); // starts animation

        TextView txtvtitle=findViewById(R.id.tv_title);
        TextView txtvmessage=findViewById(R.id.tv_desc);

        if(getIntent().getExtras()!=null){
            txtvtitle.setText(getIntent().getStringExtra("message"));
            txtvmessage.setText(getIntent().getStringExtra("notes"));
        }
    }

}