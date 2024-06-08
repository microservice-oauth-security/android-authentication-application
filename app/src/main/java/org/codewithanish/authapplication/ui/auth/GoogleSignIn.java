package org.codewithanish.authapplication.ui.auth;


import android.content.Context;
import android.os.CancellationSignal;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialException;

import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;

import org.codewithanish.authapplication.R;

public class GoogleSignIn implements  CredentialManagerCallback<GetCredentialResponse, GetCredentialException>{

    private final Context context;
    private final OnGoogleSignInSuccess onGoogleSignInSuccess;
    private final OnGoogleSignInError onGoogleSignInError;
    private final GetSignInWithGoogleOption signInWithGoogleOption;

    public GoogleSignIn(Context context,OnGoogleSignInSuccess onGoogleSignInSuccess, OnGoogleSignInError onGoogleSignInError)
    {
        this.context = context;
        this.onGoogleSignInSuccess = onGoogleSignInSuccess;
        this.onGoogleSignInError = onGoogleSignInError;
        this.signInWithGoogleOption = new GetSignInWithGoogleOption
                .Builder(context.getString(R.string.google_oauth_client_id))
                .build();
    }

    void signIn()
    {
        GetCredentialRequest credentialRequest = new GetCredentialRequest.Builder()
                .addCredentialOption(signInWithGoogleOption)
                .build();
       CredentialManager credentialManager = CredentialManager.create(context);
       CancellationSignal cancellationSignal = new CancellationSignal();
       cancellationSignal.setOnCancelListener(this::onCancel);
       credentialManager.getCredentialAsync(context, credentialRequest, cancellationSignal, this::execute,this);
    }

    private void onCancel()
    {
        Log.i("<<>>","Google sign-in request cancelled");
    }


    @Override
    public void onError(@NonNull GetCredentialException e) {
        Log.e("<<>>","Error while google sign-in", e);
        onGoogleSignInError.onError("SignIn with Google Error : " +e.getMessage());
    }

    @Override
    public void onResult(GetCredentialResponse response) {
       Credential credential = response.getCredential();
       GoogleIdTokenCredential googleIdTokenCredential =  GoogleIdTokenCredential.createFrom(credential.getData());
       onGoogleSignInSuccess.onSuccess(googleIdTokenCredential);
    }

    private void execute(Runnable runnable) {
        runnable.run();
    }

    public interface OnGoogleSignInSuccess {
        void onSuccess(GoogleIdTokenCredential tokenCredential);
    }

    public interface OnGoogleSignInError {
        void onError(String errorMessage);
    }

}
