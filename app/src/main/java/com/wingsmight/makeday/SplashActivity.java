package com.wingsmight.makeday;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

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

        final View loginView = findViewById(R.id.sign_in_button);
        final View loginView2 = findViewById(R.id.skip_auth);
        loginView.setVisibility(View.GONE);

        loginView2.setVisibility(View.GONE);

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

                loginView.setVisibility(View.VISIBLE);
                loginView2.setVisibility(View.VISIBLE);

                //startActivity(intent);
                //finish();
            }
        };

        handler = new Handler();
        handler.postDelayed(runnable, 1000);
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

