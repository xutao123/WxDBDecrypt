package com.wx.decrypt.service;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class GuardJobService extends JobService {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        scheduleJobInfo();
        return START_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        startService(new Intent(this, DBWorkService.class));
        return false;
    }

    public void scheduleJobInfo() {
        JobInfo.Builder builder = new JobInfo.Builder(8888, new ComponentName(this.getPackageName(), GuardJobService.class.getName()));

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //android N之后时间必须在15分钟以上
            builder.setPeriodic(15 * 60 * 1000);
        }else{
            builder.setPeriodic(1 * 1000);
        }
        builder.setPersisted(true);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE);
        JobScheduler jobScheduler = (JobScheduler)this.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        int ret = jobScheduler.schedule(builder.build());

        Log.e("service","GuardJobService--getJobInfo -- ret::" + ret);
    }

}
