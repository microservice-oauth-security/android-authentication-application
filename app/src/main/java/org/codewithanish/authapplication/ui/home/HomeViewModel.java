package org.codewithanish.authapplication.ui.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.codewithanish.authapplication.data.response.EmployeeResponse;

import java.util.Collections;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<HomeLiveData> liveData;

    public HomeViewModel()
    {
        liveData = new MutableLiveData<>();
        liveData.setValue(HomeLiveData.builder()
                .employeeResponseList(Collections.emptyList())
                .build());
    }

    MutableLiveData<HomeLiveData> getLiveData()
    {
        return liveData;
    }

    public void setEmployeeListFromBackgroundThread(List<EmployeeResponse> employeeResponseList)
    {
        HomeLiveData homeLiveData = liveData.getValue();
        if(homeLiveData != null)
        {
            homeLiveData.setEmployeeResponseList(employeeResponseList);
            liveData.postValue(homeLiveData);
        }
    }

    public void handleProgressBarFromMainThread(boolean visible)
    {
        HomeLiveData authenticationLiveData = liveData.getValue();
        if(authenticationLiveData != null)
        {
            authenticationLiveData.setShowProgressBar(visible);
            liveData.setValue(authenticationLiveData);
        }
    }

    public void handleProgressBarFromBackGroundThread(boolean visible)
    {
        HomeLiveData authenticationLiveData = liveData.getValue();
        if(authenticationLiveData != null)
        {
            authenticationLiveData.setShowProgressBar(visible);
            liveData.postValue(authenticationLiveData);
        }
    }

    public void setErrorFromMainThread(String errorMessage)
    {
        liveData.setValue(HomeLiveData.builder()
                .employeeResponseList(Collections.emptyList())
                .errorMessage(errorMessage)
                .build());
    }

    public void setErrorFromBackGroundThread(String errorMessage)
    {
        liveData.postValue(HomeLiveData.builder()
                .employeeResponseList(Collections.emptyList())
                .errorMessage(errorMessage)
                .build());
    }
}
