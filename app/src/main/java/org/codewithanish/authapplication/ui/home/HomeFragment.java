package org.codewithanish.authapplication.ui.home;

import static android.content.Context.MODE_PRIVATE;
import static org.codewithanish.authapplication.api.ApiUtil.decodeAccessToken;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.codewithanish.authapplication.R;
import org.codewithanish.authapplication.api.DemoServiceRetrofitBuilder;
import org.codewithanish.authapplication.api.repository.EmployeeRepository;
import org.codewithanish.authapplication.api.service.EmployeeService;
import org.codewithanish.authapplication.data.UserClaim;
import org.codewithanish.authapplication.data.response.EmployeeResponse;
import org.codewithanish.authapplication.databinding.FragmentHomeBinding;
import org.codewithanish.authapplication.ui.auth.AuthenticationFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private SharedPreferences sharedPreferences;

    private HomeViewModel homeViewModel;

    public HomeFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences  = requireContext().getSharedPreferences(requireContext().getPackageName(), MODE_PRIVATE);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.getLiveData().observe(getViewLifecycleOwner(), this::onLiveDataChanged);
        setOnClickListeners();
        setUserAttributes();
        return binding.getRoot();
    }

    private void setUserAttributes()
    {

     UserClaim userClaim = decodeAccessToken(sharedPreferences.getString("AccessToken",""));
     binding.welcomeUsername.setText("Welcome "+userClaim.getUserName());
     binding.rolesList.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, userClaim.getAuthorities()));
     binding.rolesList.setEnabled(false);
     binding.rolesList.setDividerHeight(0);
    }

    private void setOnClickListeners()
    {
        binding.fetchEmployee.setOnClickListener(this::fetchEmployees);
        binding.logoutBtn.setOnClickListener(v -> {
            homeViewModel.setErrorFromMainThread(null);
            logout("You are logged out!!!");
        });
    }

    private void fetchEmployees(View v)
    {
        homeViewModel.setErrorFromMainThread(null);
        Retrofit retrofit = new DemoServiceRetrofitBuilder().build(requireContext());
        EmployeeService employeeService = retrofit.create(EmployeeService.class);
        EmployeeRepository employeeRepository = new EmployeeRepository(employeeService, sharedPreferences.getString("AccessToken",""));
        Call<List<EmployeeResponse>> responseCall = employeeRepository.getEmployees();
        responseCall.enqueue(new EmployeeCallBack(this::logout, homeViewModel));
    }

    private void onLiveDataChanged(HomeLiveData liveData)
    {
        showEmployees(liveData.getEmployeeResponseList());
        binding.errorView.setText(liveData.getErrorMessage());
        binding.progressBar.setVisibility(liveData.isShowProgressBar() ? View.VISIBLE : View.GONE);
    }

    private void showEmployees(List<EmployeeResponse> employeeResponseList)
    {
        binding.employeeTable.removeAllViews();
        if(!employeeResponseList.isEmpty()) {
            setTableHeader();
            employeeResponseList.forEach(this::setTableData);
        }
    }

    private void setTableHeader()
    {
        TableRow tr = new TableRow(requireContext());
        tr.addView(getHeaderColumn("Name"));
        tr.addView(getHeaderColumn("Company"));
        tr.addView(getHeaderColumn("Designation"));
        binding.employeeTable.addView(tr);
    }

    private void setTableData(EmployeeResponse e)
    {
        TableRow tr = new TableRow(requireContext());
        tr.addView(getDataColumn(e.getName()));
        tr.addView(getDataColumn(e.getCompany()));
        tr.addView(getDataColumn(e.getDesignation()));
        binding.employeeTable.addView(tr);
    }

    private TextView getHeaderColumn(String value)
    {
        TextView textView = new TextView(requireContext());
        textView.setPadding(5,5,5,5);
        textView.setText(value);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        return textView;
    }

    private TextView getDataColumn(String value)
    {
        TextView textView = new TextView(requireContext());
        textView.setPadding(5,5,5,5);
        textView.setText(value);
        return textView;
    }

    private void logout(String logoutMessage)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("AccessToken");
        editor.apply();
        requireActivity().getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_container, AuthenticationFragment.class, null)
                .commit();
        Toast.makeText(requireContext(),logoutMessage,Toast.LENGTH_LONG).show();
    }
}