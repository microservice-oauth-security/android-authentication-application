package org.codewithanish.authapplication.ui.auth;

import org.codewithanish.authapplication.api.ApiUtil;
import org.codewithanish.authapplication.data.response.AuthenticationResponse;
import org.codewithanish.authapplication.ui.FragmentNavigator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthenticationCallback implements Callback<AuthenticationResponse> {

    private final FragmentNavigator<AuthenticationResponse> fragmentNavigator;
    private final AuthenticationViewModel viewModel;

    public AuthenticationCallback(FragmentNavigator<AuthenticationResponse> fragmentNavigator,
                                  AuthenticationViewModel viewModel)
    {
        this.fragmentNavigator = fragmentNavigator;
        this.viewModel = viewModel;
        viewModel.handleProgressBarFromMainThread(true);
    }

    @Override
    public void onResponse(Call<AuthenticationResponse> call, Response<AuthenticationResponse> response) {
        if (response.isSuccessful()) {
            viewModel.handleProgressBarFromBackGroundThread(false);
            fragmentNavigator.navigate(response.body());
        } else {
            String error = ApiUtil.getErrorMessage(response.errorBody());
            viewModel.setErrorFromBackGroundThread(error != null ? error : "Oops!!! Something went wrong");
        }
    }

    @Override
    public void onFailure(Call<AuthenticationResponse> call, Throwable throwable) {
        viewModel.setErrorFromBackGroundThread(throwable.getMessage());
    }
}
