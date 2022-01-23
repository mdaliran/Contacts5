package com.momid.mainactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
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
import com.momid.mainactivity.response_model.ContactsListResponse;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ContactsActivity extends AppCompatActivity implements ContactsClickListener {

    public ContactsViewModel viewModel;
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
                viewModel.state.loading.postValue(false);
                adapter.notifyDataSetChanged();
            }
        });

        adapter = new ContactsAdapter();
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        viewModel.getContactsListLivedata().observe(this, adapter::submitList);
        recyclerView.setAdapter(adapter);
//        adapter.enableLoadMore();
//        adapter.setOnLoadMoreListener(new ContactsAdapter.OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                if (!viewModel.allContactsNextPage()) {
//                    adapter.disableLoadMore();
//                }
//            }
//        });
        adapter.setLoadMore(recyclerView, layoutManager);
        adapter.setOnItemClick(new ContactsAdapter.OnItemClick() {
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
                return true;
            }
        });

        viewModel.state.loading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

            }
        });

        viewModel.state.searchMode.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

            }
        });

        viewModel.state.contactsPermissionNeeded.observe(this, new Observer<Boolean>() {
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

    private boolean askForPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSION_REQUEST_CONTACT);
            return false;
        }

        return true;
    }

    public void startPermissionDeniedMode() {

        permissionDeniedLayout.setVisibility(View.VISIBLE);
    }

    public void endPermissionMode() {

        permissionDeniedLayout.setVisibility(View.GONE);
    }

    @Override
    public void onGivePermissionClick() {
        viewModel.state.contactsPermissionNeeded.postValue(true);
    }

    @Override
    public void onSearchBackClick() {
        viewModel.state.searchMode.postValue(false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CONTACT) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                viewModel.state.contactsPermissionNeeded.setValue(false);
            }
            else {
                startPermissionDeniedMode();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (viewModel.state.searchMode.getValue() != null && viewModel.state.searchMode.getValue()) {
            viewModel.state.searchMode.setValue(false);
        }
        else {
            super.onBackPressed();
        }
    }
}
