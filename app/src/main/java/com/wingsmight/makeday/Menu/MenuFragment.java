package com.wingsmight.makeday.Menu;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.wingsmight.makeday.GoogleAuth;
import com.wingsmight.makeday.R;
import com.wingsmight.makeday.TabName;

public class MenuFragment extends Fragment
{
    TabName tabName = TabName.MENU;

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

        final TextView youAreSignIn = view.findViewById(R.id.youAreSignIn);
        final TextView clientEmail = view.findViewById(R.id.clientEmail);
        final SignInButton signInButton = view.findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                GoogleAuth.signIn();

                youAreSignIn.setText("Вы вошли через Google");
                //clientEmail.setText(account.getEmail());//error is here
                signInButton.setVisibility(View.GONE);
            }
        });

        if(account != null)
        {
            youAreSignIn.setText("Вы вошли через Google");
            clientEmail.setText(account.getEmail());
            signInButton.setVisibility(View.GONE);
        }
        else
        {
            youAreSignIn.setVisibility(View.GONE);
            clientEmail.setVisibility(View.GONE);
            signInButton.setVisibility(View.VISIBLE);
        }



        final ImageView signOutButton = view.findViewById(R.id.signOutButton);
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
                    GoogleAuth.signOut(onCompleteListener);
                }
            }
        });
    }
}
