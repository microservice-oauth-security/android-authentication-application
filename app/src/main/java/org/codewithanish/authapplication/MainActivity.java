package org.codewithanish.authapplication;

import static org.codewithanish.authapplication.api.ApiUtil.isTokenExpired;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import org.codewithanish.authapplication.ui.auth.AuthenticationFragment;
import org.codewithanish.authapplication.ui.home.HomeFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // on activity rotated, the old fragment attached to the activity will be restored by the activity ,
        // so we don't need to initialize fragment again
        if(savedInstanceState == null) {
            initializeFragments();
        }
    }

    private void initializeFragments()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        if(sharedPreferences.contains("AccessToken") && !isTokenExpired(sharedPreferences.getString("AccessToken","")))
        {
            getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_container, HomeFragment.class,null)
                .commit();

        }else {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_container, AuthenticationFragment.class,null)
                    .commit();
        }
    }
}