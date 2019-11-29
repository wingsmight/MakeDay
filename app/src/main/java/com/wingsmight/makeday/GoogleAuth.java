package com.wingsmight.makeday;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class GoogleAuth
{
    public static final int RC_SIGN_IN = 9001;

    private static GoogleSignInClient client;
    private static GoogleSignInAccount account;
    private static Context context;
    private static Intent signInIntent;


    public static void Init(Context context, GoogleAuthListener listener)
    {
        GoogleAuth.context = context;
        addListener(listener);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        client = GoogleSignIn.getClient(context, gso);
    }

    public static void signIn()
    {
        signInIntent = client.getSignInIntent();
        ((Activity)context).startActivityForResult(signInIntent, RC_SIGN_IN);

        getAccount();
    }

    public static GoogleSignInAccount getUser()
    {
        account = GoogleSignIn.getLastSignedInAccount(context);
        return account;
    }

    private static void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try
        {
            account = completedTask.getResult(ApiException.class);
            onUpdateUI(account);
        }
        catch (ApiException e)
        {
            onUpdateUI(null);
        }
    }

    public static void getAccount()
    {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(signInIntent);
        handleSignInResult(task);
    }

    private static List<GoogleAuthListener> listeners = new ArrayList<GoogleAuthListener>();

    private static void addListener(GoogleAuthListener toAdd)
    {
        listeners.add(toAdd);
    }

    private static void onUpdateUI(GoogleSignInAccount account)
    {
        for (GoogleAuthListener hl : listeners)
        {
            hl.updateUI(account);
        }
    }

    public static void signOut(OnCompleteListener<Void> onCompleteListener)
    {
        client.signOut().addOnCompleteListener(onCompleteListener);
    }
}
