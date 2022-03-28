package com.momid.mainactivity.contacts.views;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.momid.mainactivity.R;
import com.momid.mainactivity.contacts.ContactsClickListener;
import com.momid.mainactivity.contacts.SharedViewModel;
import com.momid.mainactivity.contacts.ContactsViewModel;
import com.momid.mainactivity.contacts_fragment.ContactsFragmentDirections;
import com.momid.mainactivity.databinding.ActivityContactsBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ContactsActivity extends AppCompatActivity implements ContactsClickListener {

    public ContactsViewModel viewModel;
    public SharedViewModel sharedViewModel;
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

        viewModel = new ViewModelProvider(this).get(ContactsViewModel.class);
        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        sharedViewModel.setContactsClickListener(this);

        binding.setViewmodel(viewModel);
        binding.setClickListener(this);

        viewModel.shouldRefresh.observe(this, aBoolean -> sharedViewModel.dbRefresh.postValue(aBoolean));

//        sharedViewModel.dbRefresh.observe(this, aBoolean -> viewModel.shouldRefresh.postValue(aBoolean));

        viewModel.searchMode.observe(this, aBoolean -> {
            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_container);
            NavController navController = navHostFragment.getNavController();
            if (aBoolean) {
                if (navController.getCurrentDestination().getId() == R.id.contactsFragment) {
                    navController.navigate(ContactsFragmentDirections.actionContactsFragmentToSearchContactsFragment());
                }
            }
            else {
                navController.popBackStack();
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
    protected void onResume() {
        super.onResume();

        viewModel.refresh();
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
    public void onSearchClick() {
        viewModel.searchMode.postValue(true);
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
