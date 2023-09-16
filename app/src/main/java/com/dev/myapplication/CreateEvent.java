package com.dev.myapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class CreateEvent extends AppCompatActivity implements View.OnClickListener {
    Button btn_time, btn_date, btn_done;
    EditText edt_name;
    EditText editext_message;
    String timeTonotify;

    private SqliteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        edt_name = findViewById(R.id.editext_name);
        btn_time = findViewById(R.id.btn_time);
        btn_date = findViewById(R.id.btn_date);
        btn_done = findViewById(R.id.btn_done);
        editext_message = findViewById(R.id.editext_message);
        btn_time.setOnClickListener(this);
        btn_date.setOnClickListener(this);
        btn_done.setOnClickListener(this);

        mDatabase = new SqliteDatabase(this);

    }



    @Override
    public void onClick(View view) {
        if (view == btn_time) {
            selectTime();
        } else if (view == btn_date) {
            selectDate();
        } else {
            submit();
        }
    }

    private void submit() {
        String name = edt_name.getText().toString().trim();
        String text = editext_message.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, "Please Enter name", Toast.LENGTH_SHORT).show();
        }
        if (text.isEmpty()) {
            Toast.makeText(this, "Please Enter desription", Toast.LENGTH_SHORT).show();
        } else {
            if (btn_time.getText().toString().equals("Select Time") || btn_date.getText().toString().equals("Select date")) {
                Toast.makeText(this, "Please select date and time", Toast.LENGTH_SHORT).show();
            } else {
                String value = (editext_message.getText().toString().trim());
                String date = (btn_date.getText().toString().trim());

                String dateandtime = date + " " + timeTonotify;
                DateFormat formatter = new SimpleDateFormat("d-M-yyyy hh:mm");
                Date curDate = new Date();
                long curMillis = curDate.getTime();
                String curTime = formatter.format(curDate);
                Log.e("currMiles",String.valueOf(curMillis));
                String oldTime = dateandtime;
                Date oldDate = null;
                try {
                    oldDate = formatter.parse(oldTime);
                    long oldMillis = oldDate.getTime();
                    long remianingtime=oldMillis-curMillis;
                    long seconds = TimeUnit.MILLISECONDS.toSeconds(remianingtime);
                    Log.e("oldMiles",String.valueOf(oldMillis));

                    Contacts newContact = new Contacts(name,text,String.valueOf(oldMillis),btn_date.getText().toString(),timeTonotify);
                    mDatabase.addContacts(newContact);
                    Intent intent=new Intent();
                    intent.putExtra("MESSAGE","ok");
                    setResult(2,intent);
                    finish();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }


            }
        }
    }

    private void selectTime() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                timeTonotify = i + ":" + i1;
                btn_time.setText(FormatTime(i, i1));
            }
        }, hour, minute, false);
        timePickerDialog.show();

    }

    private void selectDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                btn_date.setText(day + "-" + (month + 1) + "-" + year);
            }
        }, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    public String FormatTime(int hour, int minute) {

        String time;
        time = "";
        String formattedMinute;

        if (minute / 10 == 0) {
            formattedMinute = "0" + minute;
        } else {
            formattedMinute = "" + minute;
        }


        if (hour == 0) {
            time = "12" + ":" + formattedMinute + " AM";
        } else if (hour < 12) {
            time = hour + ":" + formattedMinute + " AM";
        } else if (hour == 12) {
            time = "12" + ":" + formattedMinute + " PM";
        }

        else {
            int temp = hour - 12;
            time = temp + ":" + formattedMinute + " PM";
        }


        return time;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                editext_message.setText(text.get(0));
            }
        }

    }


}