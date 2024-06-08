package org.codewithanish.authapplication.ui.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticationLiveData {

    private String errorMessage;
    private boolean showProgressBar;
}
