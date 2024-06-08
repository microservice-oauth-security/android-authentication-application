package org.codewithanish.authapplication.api.service;

import org.codewithanish.authapplication.data.response.EmployeeResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface EmployeeService {

    @GET("employee")
    Call<List<EmployeeResponse>> getEmployees(@Header("Authorization") String token);
}
