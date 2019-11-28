package com.wingsmight.makeday;

import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class GoogleAuth
{
    private GoogleSignInClient client;
    private GoogleSignInAccount account;
    private Context context;

    public GoogleAuth(Context context)
    {
        this.context = context;

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        client = GoogleSignIn.getClient(context, gso);
    }

    public void checkUser()
    {
        account = GoogleSignIn.getLastSignedInAccount(context);
        //updateUI(account);
    }
}
