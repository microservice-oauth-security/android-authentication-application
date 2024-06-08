package org.codewithanish.authapplication.ui.auth;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;

import org.codewithanish.authapplication.R;
import org.codewithanish.authapplication.api.AuthenticationServiceRetrofitBuilder;
import org.codewithanish.authapplication.api.repository.AuthenticationRepository;
import org.codewithanish.authapplication.api.service.AuthenticationService;
import org.codewithanish.authapplication.data.request.AuthenticationRequest;
import org.codewithanish.authapplication.data.request.ThirdPartyAuthRequest;
import org.codewithanish.authapplication.data.response.AuthenticationResponse;
import org.codewithanish.authapplication.databinding.FragmentAuthenticationBinding;
import org.codewithanish.authapplication.ui.home.HomeFragment;

import retrofit2.Call;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class AuthenticationFragment extends Fragment {

    private FragmentAuthenticationBinding binding;
    private AuthenticationViewModel authenticationViewModel;
    private AuthenticationService authenticationService;

    private final static String GOOGLE_PROVIDER = "GOOGLE";

    public AuthenticationFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authenticationService = new AuthenticationServiceRetrofitBuilder()
                .build(requireContext())
                .create(AuthenticationService.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAuthenticationBinding.inflate(inflater, container, false);
        FragmentActivity activity = requireActivity();
        // Create a ViewModel only for the first time. ViewModel is associated with activity/fragment lifecycle.
        // Re-created activities/fragment receive the same MyViewModel instance created by the first activity/fragment.
        authenticationViewModel = new ViewModelProvider(this)
                .get(AuthenticationViewModel.class);
        authenticationViewModel.getLiveData()
                .observe(getViewLifecycleOwner(), this::onLiveDataChanged);
        setOnClickListeners();
        if (savedInstanceState != null) {
            AuthenticationState authenticationState = (AuthenticationState) savedInstanceState.getSerializable("authState");
            displaySavedState(authenticationState);
        }
        return binding.getRoot();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("authState",
                AuthenticationState.builder()
                        .signInViewValue(
                                AuthenticationState.SignInViewValue.builder()
                                        .userName(binding.signUpUserName.getText())
                                        .password(binding.signUpPassword.getText())
                                        .confirmPassword(binding.signUpConfirmPassword.getText())
                                        .userNameError(binding.signUpUserName.getError())
                                        .passwordError(binding.signUpPassword.getError())
                                        .confirmPasswordError(binding.signUpConfirmPassword.getError())
                                        .build())
                        .loginViewValue(
                                AuthenticationState.LoginViewValue.builder()
                                        .userName(binding.loginUserName.getText())
                                        .password(binding.loginPassword.getText())
                                        .userNameError(binding.loginUserName.getError())
                                        .passwordError(binding.loginPassword.getError())
                                        .build())
                        .isSignInViewVisible(binding.signUpView.getVisibility() == View.VISIBLE)
                        .build());
    }

    private void displaySavedState(AuthenticationState state)
    {
        binding.loginUserName.setText(state.getLoginViewValue().getUserName());
        binding.loginPassword.setText(state.getLoginViewValue().getPassword());
        binding.signUpUserName.setText(state.getSignInViewValue().getUserName());
        binding.signUpPassword.setText(state.getSignInViewValue().getPassword());
        binding.signUpConfirmPassword.setText(state.getSignInViewValue().getConfirmPassword());
        binding.loginUserName.setError(state.getLoginViewValue().getUserNameError());
        binding.loginPassword.setError(state.getLoginViewValue().getPasswordError());
        binding.signUpUserName.setError(state.getSignInViewValue().getUserNameError());
        binding.signUpPassword.setError(state.getSignInViewValue().getPasswordError());
        binding.signUpConfirmPassword.setError(state.getSignInViewValue().getConfirmPasswordError());
        if(state.isSignInViewVisible())
        {
            binding.loginView.setVisibility(View.GONE);
            binding.signUpView.setVisibility(View.VISIBLE);
        }
    }


    private void setOnClickListeners() {
        binding.loginButton.setOnClickListener(v -> {
            resetError();
            if (isLoginFieldsValid()) {
                doLogin();
            }
        });

        binding.signUpButton.setOnClickListener(v -> {
            resetError();
            if (isSignUpFieldsValid()) {
                doSignUp();
            }
        });

        binding.googleSignInButton.setOnClickListener(v -> {
            resetView();
            new GoogleSignIn(getContext(), this::onSuccessGoogleSignIn,
                    this::onErrorGoogleSignIn).signIn();
        });

        binding.signUpText.setOnClickListener(v ->
        {
            resetView();
            binding.loginView.setVisibility(View.GONE);
            binding.signUpView.setVisibility(View.VISIBLE);

        });

        binding.loginText.setOnClickListener(v -> {
            resetView();
            binding.signUpView.setVisibility(View.GONE);
            binding.loginView.setVisibility(View.VISIBLE);
        });

    }

    private boolean isLoginFieldsValid() {
        if (binding.loginUserName.getText().length() == 0) {
            binding.loginUserName.setError("Enter UserName");
            return false;
        } else if (binding.loginPassword.getText().length() == 0) {
            binding.loginPassword.setError("Enter Password");
            return false;
        }
        return true;
    }

    private boolean isSignUpFieldsValid() {
        if (binding.signUpUserName.getText().length() == 0) {
            binding.signUpUserName.setError("Enter UserName");
            return false;
        } else if (binding.signUpPassword.getText().length() == 0) {
            binding.signUpPassword.setError("Enter Password");
            return false;
        } else if (binding.signUpConfirmPassword.getText().length() == 0) {
            binding.signUpConfirmPassword.setError("Enter Confirm Password");
            return false;
        } else if (!binding.signUpConfirmPassword.getText().toString().equals(binding.signUpPassword.getText().toString())) {
            binding.signUpConfirmPassword.setError("Password does not match");
            return false;
        }
        return true;
    }

    private void doLogin() {
        AuthenticationRepository repository = new AuthenticationRepository(authenticationService);
        Call<AuthenticationResponse> responseCall = repository.doLogin(AuthenticationRequest.builder()
                .userName(binding.loginUserName.getText().toString())
                .password(binding.loginPassword.getText().toString()).build());
        responseCall.enqueue(new AuthenticationCallback(this::navigateToHome, authenticationViewModel));
    }

    private void doSignUp() {
        AuthenticationRepository repository = new AuthenticationRepository(authenticationService);
        Call<AuthenticationResponse> responseCall = repository.doSignUp(AuthenticationRequest.builder()
                .userName(binding.signUpUserName.getText().toString())
                .password(binding.signUpPassword.getText().toString()).build());
        responseCall.enqueue(new AuthenticationCallback(this::navigateToHome, authenticationViewModel));
    }

    private void onSuccessGoogleSignIn(GoogleIdTokenCredential tokenCredential) {
        String token = tokenCredential.getIdToken();
        Log.i("<<>>", "Google Id Token = " + token);
        AuthenticationRepository repository = new AuthenticationRepository(authenticationService);
        Call<AuthenticationResponse> responseCall = repository.doThirdPartySignIn(ThirdPartyAuthRequest.builder()
                .provider(GOOGLE_PROVIDER).build(),  token);
        responseCall.enqueue(new AuthenticationCallback(this::navigateToHome, authenticationViewModel));
    }

    private void onErrorGoogleSignIn(String errorMessage) {
        authenticationViewModel.setErrorFromMainThread(errorMessage);
    }

    private void navigateToHome(AuthenticationResponse response) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(getContext().getPackageName(), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("AccessToken", response.getAccessToken());
        editor.apply();
        getActivity().getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_container, HomeFragment.class, null)
                .commit();
    }

    private void onLiveDataChanged(AuthenticationLiveData authenticationLiveData) {
        binding.errorView.setText(authenticationLiveData.getErrorMessage());
        binding.progressBar.setVisibility(authenticationLiveData.isShowProgressBar() ? View.VISIBLE : View.GONE);
    }

    private void resetView()
    {
        resetLoginView();
        resetSignInView();
        resetError();
    }
    private void resetError()
    {
        resetLoginError();
        resetSignInError();
        authenticationViewModel.setErrorFromMainThread(null);
    }

    private void resetLoginView() {
        binding.loginUserName.setText(null);
        binding.loginPassword.setText(null);
        resetLoginError();
    }

    private void resetSignInView() {
        binding.signUpUserName.setText(null);
        binding.signUpPassword.setText(null);
        binding.signUpConfirmPassword.setText(null);
        resetSignInError();
    }

    private void resetLoginError()
    {
        binding.loginUserName.setError(null);
        binding.loginPassword.setError(null);
    }

    private void resetSignInError()
    {
        binding.signUpUserName.setError(null);
        binding.signUpPassword.setError(null);
        binding.signUpConfirmPassword.setError(null);
    }
}