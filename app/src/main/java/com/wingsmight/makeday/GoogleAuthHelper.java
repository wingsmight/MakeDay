package com.wingsmight.makeday;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public class GoogleAuthHelper implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks
{
    private AppCompatActivity mActivity;
    private Context context;
    private FragmentActivity fragmentActivity;
    private Fragment fragment;
    private GoogleApiClient mGoogleApiClient;
    public static final int RC_SIGN_IN = 9001;
    private boolean isLoggingOut = false;

    public GoogleAuthHelper(Context context, AppCompatActivity mActivity, GoogleAuthListener listener)
    {
        this.fragmentActivity = null;
        this.fragment = null;
        this.mActivity = mActivity;
        this.context = context;
        addListener(listener);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getResources().getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .enableAutoManage(mActivity, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addConnectionCallbacks(this)
                .build();
    }

    public GoogleAuthHelper(Context context, Fragment fragment, GoogleAuthListener listener)
    {
        this.fragmentActivity = fragment.getActivity();
        this.fragment = fragment;
        this.mActivity = null;
        this.context = context;
        addListener(listener);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getResources().getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .enableAutoManage(fragmentActivity, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addConnectionCallbacks(this)
                .build();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {
        Toast.makeText(context, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    public GoogleSignInAccount getUser()
    {
        return GoogleSignIn.getLastSignedInAccount(context);
    }

    public void signOut() {

        if (mGoogleApiClient.isConnected())
        {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            isLoggingOut = false;

                            onUpdateUI(getUser());
                        }
                    });
        }
        else
        {
            isLoggingOut = true;

            onUpdateUI(getUser());
        }

        onSignOut(getUser());
    }

    public GoogleApiClient getGoogleClient() {
        return mGoogleApiClient;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        if (isLoggingOut)
        {
            signOut();
        }
    }

    @Override
    public void onConnectionSuspended(int i)
    {
        Toast.makeText(context, "onConnectionSuspended", Toast.LENGTH_SHORT).show();
    }


    public void signIn()
    {
        if(fragment != null)
        {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(getGoogleClient());
            fragment.startActivityForResult(signInIntent, GoogleAuthHelper.RC_SIGN_IN);
        }
        else if(mActivity != null)
        {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(getGoogleClient());
            mActivity.startActivityForResult(signInIntent, GoogleAuthHelper.RC_SIGN_IN);
        }
    }

    public void updateUIonActivityResult(int requestCode, Intent data)
    {
        if (requestCode == GoogleAuthHelper.RC_SIGN_IN)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess())
            {
                onUpdateUI(getUser());
            }
            else
            {
                onUpdateUI(null);
            }

            onSignIn(getUser());
        }
    }

    private List<GoogleAuthListener> listeners = new ArrayList<>();

    public void addListener(GoogleAuthListener toAdd)
    {
        listeners.add(toAdd);
    }

    private void onUpdateUI(GoogleSignInAccount account)
    {
        for (GoogleAuthListener hl : listeners)
        {
            hl.updateUI(account);
        }
    }

    private void onSignIn(GoogleSignInAccount account)
    {
        for (GoogleAuthListener hl : listeners)
        {
            hl.signIn(account);
        }
    }

    private void onSignOut(GoogleSignInAccount account)
    {
        for (GoogleAuthListener hl : listeners)
        {
            hl.signOut(account);
        }
    }
}