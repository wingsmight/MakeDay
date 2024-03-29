package com.wingsmight.makeday;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

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
        intent = new Intent(context, GoalNotificationService.class);
        context.startService(intent);

//Разблокируем поток.
        wakeLock.release();
    }
}
