package com.example.harish.geomindr.activity.ebr;

import android.app.Activity;
import android.app.NotificationManager;
import android.os.Bundle;

import com.example.harish.geomindr.R;

public class TimerNotification extends Activity {
    int mHour, mMinute;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_notification);

        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(4444);

        /*final String newTime = getIntent().getStringExtra("later1");  //newTime = name of entity

        getIntent().removeExtra("later");
        getIntent().removeExtra("later1");

        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        final DatabaseHelper databaseHelper = DatabaseHelper.getInstance(this);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        if(hourOfDay>12)
                            hourOfDay = hourOfDay-12;

                        Boolean isUpdate = databaseHelper.updateTime(newTime,Integer.toString(hourOfDay)+":"+Integer.toString(minute));
                        if(!isUpdate)
                            Toast.makeText(TimerNotification.this, "Time not updated", Toast.LENGTH_SHORT).show();
                        Boolean isUpdate1 = databaseHelper.updatelat(newTime,"0.0");
                        if(!isUpdate1)
                            Toast.makeText(TimerNotification.this, "Time not updated", Toast.LENGTH_SHORT).show();
                        Boolean isUpdate2 = databaseHelper.updatelng(newTime,"0.0");
                        if(!isUpdate2)
                            Toast.makeText(TimerNotification.this, "Time not updated", Toast.LENGTH_SHORT).show();
                    }
                },mHour,mMinute,false);
        timePickerDialog.setMessage("Remind me after ...");
        timePickerDialog.show();*/
    }
}
