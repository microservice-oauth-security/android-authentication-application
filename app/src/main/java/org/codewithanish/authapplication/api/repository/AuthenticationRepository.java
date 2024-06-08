package org.codewithanish.authapplication.api.repository;

import org.codewithanish.authapplication.api.service.AuthenticationService;
import org.codewithanish.authapplication.data.request.AuthenticationRequest;
import org.codewithanish.authapplication.data.request.ThirdPartyAuthRequest;
import org.codewithanish.authapplication.data.response.AuthenticationResponse;

import lombok.AllArgsConstructor;
import retrofit2.Call;

@AllArgsConstructor
public class AuthenticationRepository {

    private final AuthenticationService authenticationService;

    public Call<AuthenticationResponse> doLogin(AuthenticationRequest authenticationRequest)
    {
       return authenticationService.doLogin(authenticationRequest);
    }

    public Call<AuthenticationResponse> doSignUp(AuthenticationRequest authenticationRequest)
    {
        return authenticationService.doSignIn(authenticationRequest);
    }

    public Call<AuthenticationResponse> doThirdPartySignIn(ThirdPartyAuthRequest authenticationRequest, String token)
    {
        return authenticationService.doThirdPartySignIn(authenticationRequest,"Bearer " + token);
    }

}
