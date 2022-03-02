package com.momid.mainactivity.contacts.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.momid.mainactivity.ContactsFragmentDirections;
import com.momid.mainactivity.contacts_fragment.ContactsFragmentViewModel;
import com.momid.mainactivity.R;
import com.momid.mainactivity.contacts.ContactsViewModel;
import com.momid.mainactivity.search.SearchContactsViewModel;
import com.momid.mainactivity.databinding.ActivityContactsBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ContactsActivity extends AppCompatActivity implements ContactsClickListener {

    public ContactsViewModel viewModel;
    public ContactsFragmentViewModel contactsFragmentViewModel;
    public SearchContactsViewModel searchContactsViewModel;
    private SearchView searchView;
    private FrameLayout searchLayout, loadingLayout;
    private ImageButton searchBack;
    private ConstraintLayout permissionDeniedLayout;
    private Button givePermission;
    private static final int PERMISSION_REQUEST_CONTACT = 0;
    private ActivityContactsBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_contacts);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_contacts);
        binding.setLifecycleOwner(this);
//        binding.setViewmodel(viewModel);
        View view = binding.getRoot();

        setContentView(view);

        searchView = binding.contactsSearchview;
        searchLayout = binding.searchContactsFrame;
        searchBack = binding.contactsSearchBack;
        permissionDeniedLayout = binding.contactsPermissionDeniedLayout;
        givePermission = binding.contactsGiveAccess;
        loadingLayout = binding.contactsLoadingLayout;

        viewModel = new ViewModelProvider(this).get(ContactsViewModel.class);
        contactsFragmentViewModel = new ViewModelProvider(this).get(ContactsFragmentViewModel.class);
        searchContactsViewModel = new ViewModelProvider(this).get(SearchContactsViewModel.class);

        binding.setViewmodel(viewModel);
        binding.setClickListener(this);

        searchLayout.setVisibility(View.INVISIBLE);
        searchBack.setVisibility(View.GONE);

        viewModel.readComplete.observe(this, aBoolean -> {
            if (aBoolean) {
                contactsFragmentViewModel.refresh();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchView.setIconified(true);
                searchView.setIconified(true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                viewModel.onSearchViewTextChange(s);
                searchContactsViewModel.onSearchViewTextChange(s);
                return true;
            }
        });

        viewModel.searchMode.observe(this, aBoolean -> {
            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_container);
            NavController navController = navHostFragment.getNavController();
            if (aBoolean) {
                if (navController.getCurrentDestination().getId() == R.id.contactsFragment) {
                    navController.navigate(ContactsFragmentDirections.actionContactsFragmentToSearchContactsFragment());
                }
                else {
//                    Toast.makeText(getApplicationContext(), "not in emptyFragment", Toast.LENGTH_LONG).show();
                }
            }
            else {
                navController.popBackStack();
//                searchContactsViewModel.refresh();
//                Toast.makeText(getApplicationContext(), "back", Toast.LENGTH_LONG).show();
            }
        });

        viewModel.contactsPermissionNeeded.observe(this, aBoolean -> {
            if (aBoolean) {
                askForPermission();
            }
            else {
                viewModel.permissionDeniedMode.postValue(false);
                viewModel.onPermissionGrant();
            }
        });

        viewModel.errorMessageLiveDate.observe(this, s -> Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show());
    }

    private void askForPermission() {

        viewModel.askForPermission(this);
    }

    @Override
    public void onGivePermissionClick() {
        viewModel.contactsPermissionNeeded.postValue(true);
    }

    @Override
    public void onSearchBackClick() {
        viewModel.searchMode.postValue(false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CONTACT) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                viewModel.contactsPermissionNeeded.setValue(false);
            }
            else {
                viewModel.permissionDeniedMode.postValue(true);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (viewModel.searchMode.getValue() != null && viewModel.searchMode.getValue()) {
            viewModel.searchMode.setValue(false);
        }
        else {
            super.onBackPressed();
        }
    }
}
