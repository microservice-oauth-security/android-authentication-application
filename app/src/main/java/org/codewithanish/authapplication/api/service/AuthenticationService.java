package org.codewithanish.authapplication.api.service;

import org.codewithanish.authapplication.data.request.AuthenticationRequest;
import org.codewithanish.authapplication.data.request.ThirdPartyAuthRequest;
import org.codewithanish.authapplication.data.response.AuthenticationResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AuthenticationService {
    @POST("login")
    Call<AuthenticationResponse> doLogin(@Body AuthenticationRequest request);

    @POST("sign-in")
    Call<AuthenticationResponse> doSignIn(@Body AuthenticationRequest request);

    @POST("3p/sign-in")
    Call<AuthenticationResponse> doThirdPartySignIn(@Body ThirdPartyAuthRequest request, @Header("Authorization") String token);
}
