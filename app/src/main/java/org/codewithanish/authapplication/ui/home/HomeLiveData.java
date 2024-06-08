package org.codewithanish.authapplication.ui.home;

import org.codewithanish.authapplication.data.response.EmployeeResponse;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class HomeLiveData {
    private List<EmployeeResponse> employeeResponseList;
    private String errorMessage;
    private boolean showProgressBar;
}
