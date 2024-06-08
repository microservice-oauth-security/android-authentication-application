package org.codewithanish.authapplication.api.repository;

import org.codewithanish.authapplication.api.service.EmployeeService;
import org.codewithanish.authapplication.data.response.EmployeeResponse;

import java.util.List;

import lombok.AllArgsConstructor;
import retrofit2.Call;

@AllArgsConstructor
public class EmployeeRepository {

    private final EmployeeService employeeService;
    private final String token;

    public Call<List<EmployeeResponse>> getEmployees()
    {
        return employeeService.getEmployees("Bearer "+token);
    }

}
