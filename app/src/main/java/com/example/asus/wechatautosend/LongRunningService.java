package com.example.asus.wechatautosend;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;


public class LongRunningService extends Service {
    public static int GAPTIME = 5;
    public MainActivity mainActivity;
    public LongRunningService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Log.d("GAPTIME", "onCreate");
        Log.d("GAPTIME", String.valueOf(GAPTIME));
        GAPTIME = MainActivity.GAPTIME;
        mainActivity = MainActivity.thisActivity;
        Log.d("GAPTIME", String.valueOf(GAPTIME));
        Log.d("GAPTIME", "onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("GAPTIME", "onStartCommand");
        Log.d("GAPTIME", String.valueOf(GAPTIME));
        //GAPTIME = intent.getIntExtra("GAPTIME", 3);
        Log.d("GAPTIME", String.valueOf(GAPTIME));
        Log.d("GAPTIME", "onStartCommand");
        new Thread(new Runnable(){
            @Override
            public void run() {
                Log.d("longrunning", "excuted at" + new Date().toString());
                if(AutoSendMsgService.GROUPDONE && AutoSendMsgService.CONTACTDONE){
                    mainActivity.goWechat();
                } else{
                    Toast.makeText(mainActivity, "上次群发未结束，本次群发终止", Toast.LENGTH_LONG).show();
                }
            }
        }).start();
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int tm = GAPTIME * 60 * 1000;
        long triggertime = System.currentTimeMillis() + tm;
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggertime, pi);
        String remind = "下一轮发送时间是:" + transferLongToDate("yyyy-MM-dd HH:mm:ss", triggertime) + "该轮发送结束后，不要锁屏";
        Toast.makeText(mainActivity, remind, Toast.LENGTH_LONG).show();
        return super.onStartCommand(intent, flags, startId);
    }

    public String transferLongToDate(String dateFormat, Long millSec) {
                 SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
                 Date date = new Date(millSec);
                 return sdf.format(date);
    }
}
