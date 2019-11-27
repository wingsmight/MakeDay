package com.wingsmight.makeday;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.wingsmight.makeday.MyDays.MyDaysFragment;
import com.wingsmight.makeday.SavingSystem.SaveLoad;

public class SplashActivity extends AppCompatActivity
{
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final Intent intent = new Intent(this, MainActivity.class);

        runnable = new Runnable()
        {
            @Override
            public void run()
            {
                context = getApplicationContext();

                int tabsLength = TabName.values().length;

                if(SaveLoad.isFirstLoad())
                {
                    for(int i = 0; i < tabsLength; i++)
                    {
                        SaveLoad.testInit(TabName.values()[i]);
                    }

                    SaveLoad.setIsFirstLoad();
                }
                else
                {
                    for(int i = 0; i < tabsLength; i++)
                    {
                        SaveLoad.preLoad(TabName.values()[i]);
                    }
                }



                startActivity(intent);

                finish();
            }
        };

        handler = new Handler();
        handler.post(runnable);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if(handler != null && runnable != null)
        {
            handler.removeCallbacks(runnable);
        }
    }

    private static Context context;
    public static Context getContext()
    {
        return context;
    }
}

