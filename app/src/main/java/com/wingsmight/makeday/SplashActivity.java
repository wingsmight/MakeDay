package com.wingsmight.makeday;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.wingsmight.makeday.SavingSystem.SaveLoad;

public class SplashActivity extends AppCompatActivity implements GoogleAuthListener
{
    private Handler handler;
    private Runnable runnable;
    private int requestCode;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        intent = new Intent(this, MainActivity.class);

        final View skipAuth = findViewById(R.id.skip_auth);
        final SignInButton signInButton = findViewById(R.id.sign_in_button);
        skipAuth.setVisibility(View.GONE);

        GoogleAuth.Init(this, this);
        signInButton.setVisibility(View.GONE);
        signInButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                GoogleAuth.signIn();
            }
        });

        skipAuth.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(intent);
                finish();
            }
        });

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

                if(GoogleAuth.getUser() == null)
                {
                    skipAuth.setVisibility(View.VISIBLE);
                    signInButton.setVisibility(View.VISIBLE);
                }
                else
                {
                    startActivity(intent);
                    finish();
                }
            }
        };

        handler = new Handler();
        handler.postDelayed(runnable, 800);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        this.requestCode = requestCode;
    }

    private static Context context;
    public static Context getContext()
    {
        return context;
    }

    @Override
    public void updateUI(GoogleSignInAccount account)
    {
        if(account != null)
        {
            startActivity(intent);
            finish();
        }
    }
}

