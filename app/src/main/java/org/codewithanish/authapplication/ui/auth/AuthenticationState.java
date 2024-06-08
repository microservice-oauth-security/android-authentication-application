package org.codewithanish.authapplication.ui.auth;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticationState implements Serializable {

    private LoginViewValue loginViewValue;
    private SignInViewValue signInViewValue;
    private boolean isSignInViewVisible;

    @Getter
    @Setter
    @Builder
    public static class LoginViewValue implements Serializable
    {
        private CharSequence userName;
        private CharSequence password;
        private CharSequence userNameError;
        private CharSequence passwordError;
    }

    @Getter
    @Setter
    @Builder
    public static class SignInViewValue implements Serializable
    {
        private CharSequence userName;
        private CharSequence password;
        private CharSequence confirmPassword;
        private CharSequence userNameError;
        private CharSequence passwordError;
        private CharSequence confirmPasswordError;
    }

}
