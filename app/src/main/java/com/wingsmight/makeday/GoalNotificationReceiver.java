package com.wingsmight.makeday;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import java.util.List;

public class GoalNotificationReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock= powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"YOUR TAG1");

//Осуществляем блокировку
        wakeLock.acquire();

//Здесь можно делать обработку.

        Intent i = new Intent();
        i.setClassName(context.getPackageName(), GoalCheckingActivity.class.getName());
        if(isAppOnForeground(context))//user is using app now
        {
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        else
        {
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }

        context.startActivity(i);

//Разблокируем поток.
        wakeLock.release();
    }

    private boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        final String packageName = context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }
}
