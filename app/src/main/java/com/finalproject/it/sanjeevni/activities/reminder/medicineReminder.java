package com.finalproject.it.sanjeevni.activities.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import com.allyants.notifyme.NotifyMe;
import com.finalproject.it.sanjeevni.activities.WelcomeActivity;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;



import androidx.appcompat.app.AppCompatActivity;

import com.finalproject.it.sanjeevni.R;
import com.finalproject.it.sanjeevni.activities.BaseActivity;

import java.util.Calendar;

public class medicineReminder extends  AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    Calendar now = Calendar.getInstance();
    TimePickerDialog tpd;
    DatePickerDialog dpd;
    EditText etTitle, etContent;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_reminder);
        Button btnNotify = findViewById(R.id.btnNotify);
        etTitle = findViewById(R.id.etTitle);
        etContent = findViewById(R.id.etContent);

        Button btnCancel = findViewById(R.id.btnCancel);
        Button btnAddanother = findViewById(R.id.btn1);




        dpd = DatePickerDialog.newInstance(medicineReminder.this, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH) );
        tpd = TimePickerDialog.newInstance(medicineReminder.this, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), now.get(Calendar.SECOND),false);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotifyMe.cancel(getApplicationContext(), "test");
                Toast.makeText(medicineReminder.this, "Medicine Reminder Cancelled", Toast.LENGTH_SHORT).show();
            }
        });
        btnNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dpd.show(getFragmentManager(),"Datepickerdialog");
                Toast.makeText(medicineReminder.this, "Medicine Reminder Added", Toast.LENGTH_SHORT).show();
            }
        });
        btnAddanother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, 18);
                calendar.set(Calendar.MINUTE, 30);
                calendar.set(Calendar.SECOND, 0);
                Intent intent1 = new Intent(medicineReminder.this, medicineReminder.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(medicineReminder.this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager am = (AlarmManager) medicineReminder.this.getSystemService(medicineReminder.this.ALARM_SERVICE);
                am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                Toast.makeText(medicineReminder.this, "Medicine Reminder Will Repeat Daily!", Toast.LENGTH_SHORT).show();

            }
        });







    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        now.set(Calendar.YEAR,year);
        now.set(Calendar.MONTH,monthOfYear);
        now.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        tpd.show(getFragmentManager(),"Timepickerdialog");



    }
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second){
        now.set(Calendar.HOUR_OF_DAY,hourOfDay);
        now.set(Calendar.MINUTE,minute);
        now.set(Calendar.SECOND,second);


        NotifyMe notifyme = new NotifyMe.Builder(getApplicationContext())
                .title(etTitle.getText().toString())
                .content(etContent.getText().toString())
                .color(187,187,187,255)
                .led_color(86,0,39,255)
                .time(now)
                .addAction(new Intent(),"snooze",false)
                .key("test")
                .addAction(new Intent(), "Dismiss", true,false)
                .addAction(new Intent(), "Done")
                .large_icon(R.mipmap.ic_launcher_round)
                .build();

    }






}
