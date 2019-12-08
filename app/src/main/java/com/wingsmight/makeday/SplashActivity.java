package com.wingsmight.makeday;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.wingsmight.makeday.SavingSystem.SaveLoad;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SplashActivity extends AppCompatActivity implements GoogleAuthListener
{
    private Handler handler;
    private Runnable runnable;
    private View skipAuth;
    private SignInButton signInButton;
    private Intent intent;
    private GoogleAuthHelper googleSignInHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        googleSignInHelper = new GoogleAuthHelper(this, this, this);

        intent = new Intent(this, MainActivity.class);

        ActionBar toolbar = getSupportActionBar();
        toolbar.hide();

        skipAuth = findViewById(R.id.skip_auth);
        signInButton = findViewById(R.id.sign_in_button);
        skipAuth.setVisibility(View.GONE);

        signInButton.setVisibility(View.GONE);
        signInButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                googleSignInHelper.signIn();
            }
        });

        skipAuth.setBackground(null);
        skipAuth.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                closeSplash();
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

                updateUI(googleSignInHelper.getUser());
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
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        googleSignInHelper.updateUIonActivityResult(requestCode, data);
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
            closeSplash();
        }
        else
        {
            skipAuth.setVisibility(View.VISIBLE);
            signInButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void signIn(GoogleSignInAccount account)
    {
        Toast.makeText(context, account.getDisplayName() + ", добро пожаловать", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void signOut(GoogleSignInAccount account)
    {

    }

    private void closeSplash()
    {
        startActivity(intent);
        finish();
    }
}

