package com.wingsmight.makeday.Menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.wingsmight.makeday.GoogleAuthHelper;
import com.wingsmight.makeday.GoogleAuthListener;
import com.wingsmight.makeday.MainActivity;
import com.wingsmight.makeday.R;
import com.wingsmight.makeday.TabName;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
        youAreSignIn = view.findViewById(R.id.youAreSignIn);
        clientEmail = view.findViewById(R.id.clientEmail);
        signInButton = view.findViewById(R.id.signInButton);
        signOutButton = view.findViewById(R.id.signOutButton);

        googleSignInHelper = new GoogleAuthHelper(this.getContext(), this, this);

        signInButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                googleSignInHelper.signIn();
            }
        });

        updateUI(googleSignInHelper.getUser());

        signOutButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View view)
            {
                if(googleSignInHelper.getUser() != null)
                {
                    googleSignInHelper.signOut();
                }
            }
        });

        TextView aboutAppTextView = view.findViewById(R.id.aboutAppText);
        aboutAppTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getContext(), AboutAppActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        googleSignInHelper.updateUIonActivityResult(requestCode, data);
    }

    @Override
    public void signIn(GoogleSignInAccount account)
    {
        Toast.makeText(getContext(), account.getDisplayName() + ", добро пожаловать", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void signOut(GoogleSignInAccount account)
    {

    }

    @Override
    public void updateUI(GoogleSignInAccount account)
    {
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
    }
}
