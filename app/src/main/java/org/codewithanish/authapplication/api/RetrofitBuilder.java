package org.codewithanish.authapplication.api;

import android.content.Context;

import retrofit2.Retrofit;

public interface RetrofitBuilder {
    Retrofit build(Context context);
}
