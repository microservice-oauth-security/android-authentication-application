package org.codewithanish.authapplication.api;

import android.content.Context;

import org.codewithanish.authapplication.R;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthenticationServiceRetrofitBuilder implements RetrofitBuilder{
    @Override
    public Retrofit build(Context context) {
        return new Retrofit.Builder()
                .baseUrl(context.getString(R.string.auth_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
