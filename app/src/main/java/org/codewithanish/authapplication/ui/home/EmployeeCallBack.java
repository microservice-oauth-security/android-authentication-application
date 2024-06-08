package org.codewithanish.authapplication.ui.home;

import static org.codewithanish.authapplication.api.ApiUtil.getErrorMessage;

import android.util.Log;

import androidx.annotation.NonNull;

import org.codewithanish.authapplication.data.response.EmployeeResponse;
import org.codewithanish.authapplication.ui.FragmentNavigator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeCallBack implements Callback<List<EmployeeResponse>> {

    private final FragmentNavigator<String> fragmentNavigator;
    private final HomeViewModel homeViewModel;
    public EmployeeCallBack(final FragmentNavigator<String> fragmentNavigator, final HomeViewModel homeViewModel)
    {
        this.fragmentNavigator = fragmentNavigator;
        this.homeViewModel = homeViewModel;
        homeViewModel.handleProgressBarFromMainThread(true);
    }

    @Override
    public void onResponse(@NonNull Call<List<EmployeeResponse>> call, Response<List<EmployeeResponse>> response) {
        if(response.isSuccessful())
        {
            homeViewModel.handleProgressBarFromBackGroundThread(false);
            homeViewModel.setEmployeeListFromBackgroundThread(response.body());
        }
        else if(response.code() == 401)
        {
            homeViewModel.handleProgressBarFromBackGroundThread(false);
            fragmentNavigator.navigate("Your Authorization Expired!!! Please login again");
        }
        else if(response.code() == 403)
        {
            homeViewModel.setErrorFromBackGroundThread("You don't have permission to access this resource");
        }
        else {
            String error = getErrorMessage(response.errorBody());
            homeViewModel.setErrorFromBackGroundThread(error != null ? error :"Oops!!! Something went wrong");
        }
    }

    @Override
    public void onFailure(@NonNull Call<List<EmployeeResponse>> call, @NonNull Throwable throwable) {
        Log.e("","Error while fetching employees", throwable);
        homeViewModel.setErrorFromBackGroundThread(throwable.getMessage());
    }
}
