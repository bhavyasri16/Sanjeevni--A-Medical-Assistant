package com.finalproject.it.sanjeevni.activities.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.finalproject.it.sanjeevni.R;

import java.util.Calendar;

public class medicineReminder extends AppCompatActivity implements View.OnClickListener {

    private int notificationId = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_reminder);

        findViewById(R.id.setBtn).setOnClickListener(this);
        findViewById(R.id.cancelBtn).setOnClickListener(this);
        Button btn = (Button)findViewById(R.id.btn1);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(medicineReminder.this, medicineReminder.class));
            }
        });
    }
    @Override
    public void onClick(View view)
    {
        EditText editText = findViewById(R.id.editText);
        TimePicker timePicker = findViewById(R.id.timePicker);


        Intent intent = new Intent(medicineReminder.this, AlarmReceiver.class);
        intent.putExtra("notificationId", notificationId);
        intent.putExtra("todo", editText.getText().toString());
        sendBroadcast(intent);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(medicineReminder.this, 0, intent,PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);

        switch (view.getId())
        {
            case R.id.setBtn:
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();


                Calendar startTime = Calendar.getInstance();
                startTime.set(Calendar.YEAR, 2020);
                startTime.set(Calendar.MONTH, 8);
                startTime.set(Calendar.DAY_OF_MONTH, 15);
                startTime.set(Calendar.HOUR_OF_DAY, hour);
                startTime.set(Calendar.MINUTE, minute);
                startTime.set(Calendar.SECOND, 0);
                long alarmStartTime = startTime.getTimeInMillis();

                alarm.set(AlarmManager.RTC_WAKEUP, alarmStartTime, alarmIntent);

                Toast.makeText(this, "Done!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.cancelBtn:
                alarm.cancel(alarmIntent);
                Toast.makeText(this, "Canceled.", Toast.LENGTH_SHORT).show();
                break;

        }

    }

}
