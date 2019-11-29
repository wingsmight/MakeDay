package com.wingsmight.makeday.Menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.wingsmight.makeday.GoogleAuth;
import com.wingsmight.makeday.GoogleAuthHelper;
import com.wingsmight.makeday.GoogleAuthListener;
import com.wingsmight.makeday.MainActivity;
import com.wingsmight.makeday.R;
import com.wingsmight.makeday.SplashActivity;
import com.wingsmight.makeday.TabName;

public class MenuFragment extends Fragment implements GoogleAuthListener
{
    TabName tabName = TabName.MENU;
    GoogleAuthHelper googleSignInHelper;

    TextView youAreSignIn;
    TextView clientEmail;
    SignInButton signInButton;
    ImageView signOutButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        final GoogleSignInAccount account = GoogleAuth.getUser();

        youAreSignIn = view.findViewById(R.id.youAreSignIn);
        clientEmail = view.findViewById(R.id.clientEmail);
        signInButton = view.findViewById(R.id.signInButton);
        signOutButton = view.findViewById(R.id.signOutButton);

        googleSignInHelper = new GoogleAuthHelper((MainActivity)getActivity(), this);

        signInButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                signIn();
            }
        });

        if(account != null)
        {
            youAreSignIn.setVisibility(View.VISIBLE);
            clientEmail.setVisibility(View.VISIBLE);
            youAreSignIn.setText("Вы вошли через Google");
            clientEmail.setText(account.getEmail());
            signInButton.setVisibility(View.GONE);
            signOutButton.setVisibility(View.VISIBLE);
        }
        else
        {
            youAreSignIn.setVisibility(View.GONE);
            clientEmail.setVisibility(View.GONE);
            signInButton.setVisibility(View.VISIBLE);
            signOutButton.setVisibility(View.GONE);
        }




        signOutButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View view)
            {
                if(GoogleAuth.getUser() != null)
                {
                    OnCompleteListener<Void> onCompleteListener = new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            youAreSignIn.setVisibility(View.GONE);
                            clientEmail.setVisibility(View.GONE);
                            signInButton.setVisibility(View.VISIBLE);
                        }
                    };

                    googleSignInHelper.signOut();
                    //GoogleAuth.signOut(onCompleteListener);
                }
            }
        });
    }

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == GoogleAuthHelper.RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                googleSignInHelper.getGoogleAccountDetails(result);

                updateUI(GoogleAuth.getUser());
            } else {
                // Google Sign In failed, update UI appropriately
                // [START_EXCLUDE]
                Log.d("Splash screen", "signInWith Google failed");

                updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }
    // [END onactivityresult]

    // [START signin]
    public void signIn()
    {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleSignInHelper.getGoogleClient());
        this.startActivityForResult(signInIntent, GoogleAuthHelper.RC_SIGN_IN);
    }
    // [END signin]

    @Override
    public void updateUI(GoogleSignInAccount account)
    {
        if(account != null)
        {
            youAreSignIn.setVisibility(View.VISIBLE);
            clientEmail.setVisibility(View.VISIBLE);
            youAreSignIn.setText("Вы вошли через Google");
            clientEmail.setText(account.getEmail());//error is here
            signInButton.setVisibility(View.GONE);
            signOutButton.setVisibility(View.VISIBLE);
        }
        else
        {
            youAreSignIn.setVisibility(View.GONE);
            clientEmail.setVisibility(View.GONE);
            signInButton.setVisibility(View.VISIBLE);
            signOutButton.setVisibility(View.GONE);
        }
    }
}
