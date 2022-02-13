package com.momid.mainactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.momid.mainactivity.data_model.Contact;
import com.momid.mainactivity.databinding.ActivityContactsBinding;
import com.momid.mainactivity.recycler_adapter.ContactsAdapter;
import com.momid.mainactivity.recycler_adapter.OnItemClick;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ContactsActivity extends AppCompatActivity implements ContactsClickListener {

    public ContactsViewModel viewModel;
    public SearchContactsViewModel searchContactsViewModel;
    private RecyclerView recyclerView;
    private ContactsAdapter adapter;
    private LinearLayoutManager layoutManager;
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

        recyclerView = binding.contactsRecycler;
        searchView = binding.contactsSearchview;
        searchLayout = binding.searchContactsFrame;
        searchBack = binding.contactsSearchBack;
        permissionDeniedLayout = binding.contactsPermissionDeniedLayout;
        givePermission = binding.contactsGiveAccess;
        loadingLayout = binding.contactsLoadingLayout;

        viewModel = new ViewModelProvider(this).get(ContactsViewModel.class);
        searchContactsViewModel = new ViewModelProvider(this).get(SearchContactsViewModel.class);

        binding.setViewmodel(viewModel);
        binding.setClickListener(this);

        viewModel.init();
        viewModel.getContactsListLivedata().observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contacts) {
                adapter.setContacts(contacts);
                if (recyclerView.getAdapter() == null) {
                    recyclerView.setAdapter(adapter);
                }
                viewModel.loading.postValue(false);
                adapter.notifyDataSetChanged();
            }
        });

        adapter = new ContactsAdapter(new OnItemClick() {
            @Override
            public void onItemClick(Contact contact) {

            }

            @Override
            public void onCallClick(Contact contact) {
                viewModel.onCall(contact);
            }

            @Override
            public void onSmsClick(Contact contact) {
                viewModel.onMessage(contact);
            }
        });
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        viewModel.getContactsListLivedata().observe(this, adapter::submitList);
        recyclerView.setAdapter(adapter);

        SearchContactsFragment searchContactsFragment = SearchContactsFragment.getInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.search_contacts_frame, searchContactsFragment).commit();
        searchLayout.setVisibility(View.INVISIBLE);
        searchBack.setVisibility(View.GONE);

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

        viewModel.contactsPermissionNeeded.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    askForPermission();
                }
                else {
                    endPermissionMode();
                    viewModel.onPermissionGrant();
                }
            }
        });
    }

    private void askForPermission() {

        viewModel.askForPermission(this);
    }

    public void startPermissionDeniedMode() {

        permissionDeniedLayout.setVisibility(View.VISIBLE);
    }

    public void endPermissionMode() {

        permissionDeniedLayout.setVisibility(View.GONE);
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
                startPermissionDeniedMode();
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
