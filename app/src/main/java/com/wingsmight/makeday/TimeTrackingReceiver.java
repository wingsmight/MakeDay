package com.wingsmight.makeday;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

public class TimeTrackingReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock= powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"YOUR TAG2");

//Осуществляем блокировку
        wakeLock.acquire();

//Здесь можно делать обработку.
        intent = new Intent(context, TimeTrackingService.class);
        context.startService(intent);

//Разблокируем поток.
        wakeLock.release();
    }
}