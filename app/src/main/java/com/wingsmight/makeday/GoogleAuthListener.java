package com.wingsmight.makeday;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public interface GoogleAuthListener
{
    void updateUI(GoogleSignInAccount account);
}
