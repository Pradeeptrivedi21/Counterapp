package com.dev.myapplication;

import static com.dev.myapplication.appconfig.BIRTHDAY_DATE;
import static com.dev.myapplication.appconfig.BIRTHDAY_EVENT_NOTES;
import static com.dev.myapplication.appconfig.BIRTHDAY_EVENT_TIME;
import static com.dev.myapplication.appconfig.CHANNEL_ID;
import static com.dev.myapplication.appconfig.CHANNEL_NAME;
import static com.dev.myapplication.appconfig.FRIEND_NAME;
import static com.dev.myapplication.appconfig.NOTIFICATION_ID;
import static com.dev.myapplication.appconfig.PERMISSION_REQUEST_CODE;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements ContactAdapter.onRefresh {


    private SqliteDatabase mDatabase;
    ArrayList<Contacts> allContacts=new ArrayList<>();
    ContactAdapter mAdapter;
    RecyclerView recyclerView;
    Vibrator vibrator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initv();
    }

    public void openSomeActivityForResult() {
        Intent intent = new Intent(this, CreateEvent.class);
        someActivityResultLauncher.launch(intent);
    }

    private void  initv(){
        if (Build.VERSION.SDK_INT > 32) {
            if (!shouldShowRequestPermissionRationale("111")){
                getNotificationPermission();
            }
        }
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        recyclerView = findViewById(R.id.myContactList);

        mDatabase = new SqliteDatabase(this);
        allContacts=new ArrayList<>();
        allContacts = mDatabase.listContacts();
        if (mDatabase.listContacts().size()>0) {

            LinearLayoutManager layoutManager
                    = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(layoutManager);
            mAdapter = new ContactAdapter(this, allContacts);
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            mAdapter.setRefresh(this);
        }
        else {
            recyclerView.setVisibility(View.GONE);
            Toast.makeText(this, "There is no event in the database. Start adding now", Toast.LENGTH_LONG).show();
        }
        Button btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                addTaskDialog();
                openSomeActivityForResult();
            }
        });

    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDatabase != null) {
            mDatabase.close();
        }
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    initv();
                }
            });

    public void getNotificationPermission(){
        try {
            if (Build.VERSION.SDK_INT > 32) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        PERMISSION_REQUEST_CODE);
            }
        }catch (Exception e){

        }
    }

    @Override
    public void onClick(Contacts contacts) {
        if(allContacts.size()>0){

            Intent intent1 = new Intent(MainActivity.this, NotificationMessage.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent1.putExtra("message", contacts.getName());
            intent1.putExtra("notes", contacts.getDescription());
            //Notification Builder
            PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 1, intent1, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
            NotificationManager notificationManager = (NotificationManager) MainActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MainActivity.this,NOTIFICATION_ID);

            RemoteViews contentView = new RemoteViews(MainActivity.this.getPackageName(), R.layout.notification_layout);
//        contentView.setImageViewResource(R.id.image, R.mipmap.ic_launcher);
            PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
            contentView.setTextViewText(R.id.message,contacts.getName());
            contentView.setTextViewText(R.id.date, contacts.getDescription());
            mBuilder.setSmallIcon(R.drawable.baseline_alarm_24);
            mBuilder.setAutoCancel(true);
            mBuilder.setOngoing(true);
            mBuilder.setPriority(Notification.PRIORITY_HIGH);
            mBuilder.setOnlyAlertOnce(true);
            mBuilder.build().flags = Notification.FLAG_NO_CLEAR | Notification.PRIORITY_HIGH;
            mBuilder.setContent(contentView);
            mBuilder.setContentIntent(pendingIntent);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String channelId =CHANNEL_ID;
                NotificationChannel channel = new NotificationChannel(channelId, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                channel.enableVibration(true);
                notificationManager.createNotificationChannel(channel);
                mBuilder.setChannelId(channelId);
            }

            Notification notification = mBuilder.build();
            notificationManager.notify(1, notification);
            final VibrationEffect vibrationEffect1;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                // this effect creates the vibration of default amplitude for 1000ms(1 sec)
                vibrationEffect1 = VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE);

                // it is safe to cancel other vibrations currently taking place
                vibrator.cancel();
                vibrator.vibrate(vibrationEffect1);
            }

            initv();
            startActivity(getIntent());
        }


    }
}