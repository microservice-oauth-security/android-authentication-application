package org.codewithanish.authapplication.ui.auth;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AuthenticationViewModel extends ViewModel {

    private final MutableLiveData<AuthenticationLiveData> viewData;

    public AuthenticationViewModel()
    {
        viewData = new MutableLiveData<>();
        viewData.setValue(AuthenticationLiveData.builder().build());
    }

    MutableLiveData<AuthenticationLiveData> getLiveData()
    {
        return this.viewData;
    }

    public void handleProgressBarFromMainThread(boolean visible)
    {
        AuthenticationLiveData authenticationLiveData = viewData.getValue();
        if(authenticationLiveData != null)
        {
            authenticationLiveData.setShowProgressBar(visible);
            viewData.setValue(authenticationLiveData);
        }
    }

    public void handleProgressBarFromBackGroundThread(boolean visible)
    {
        AuthenticationLiveData authenticationLiveData = viewData.getValue();
        if(authenticationLiveData != null)
        {
            authenticationLiveData.setShowProgressBar(visible);
            viewData.postValue(authenticationLiveData);
        }
    }

    public void setErrorFromMainThread(String errorMessage)
    {
        viewData.setValue(AuthenticationLiveData.builder()
                .errorMessage(errorMessage)
                .build());
    }

    public void setErrorFromBackGroundThread(String errorMessage)
    {
        viewData.postValue(AuthenticationLiveData.builder()
                .errorMessage(errorMessage)
                .build());
    }

}
