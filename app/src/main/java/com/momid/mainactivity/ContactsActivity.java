package com.momid.mainactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
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
import com.momid.mainactivity.recycler_adapter.ContactsAdapter;
import com.momid.mainactivity.responseModel.AllContactsResponse;

public class ContactsActivity extends AppCompatActivity {

    private ContactsViewModel viewModel;
    private RecyclerView recyclerView;
    private ContactsAdapter adapter;
    private LinearLayoutManager layoutManager;
    private SearchView searchView;
    private FrameLayout searchLayout, loadingLayout;
    private ImageButton searchBack;
    private ConstraintLayout permissionDeniedLayout;
    private Button givePermission;
    private static final int PERMISSION_REQUEST_CONTACT = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        recyclerView = findViewById(R.id.contacts_recycler);
        searchView = findViewById(R.id.contacts_searchview);
        searchLayout = findViewById(R.id.search_contacts_frame);
        searchBack = findViewById(R.id.contacts_search_back);
        permissionDeniedLayout = findViewById(R.id.contacts_permission_denied_layout);
        givePermission = findViewById(R.id.contacts_give_access);
        loadingLayout = findViewById(R.id.contacts_loading_layout);

        viewModel = new ViewModelProvider(this).get(ContactsViewModel.class);

        viewModel.init();
        viewModel.getAllContactsLivedata().observe(this, new Observer<AllContactsResponse>() {
            @Override
            public void onChanged(AllContactsResponse allContactsResponse) {
                adapter.setContacts(allContactsResponse.getContacts());
                if (recyclerView.getAdapter() == null) {
                    recyclerView.setAdapter(adapter);
                }
                adapter.notifyDataSetChanged();
            }
        });

        adapter = new ContactsAdapter(viewModel.getAllContactsLivedata().getValue().getContacts());
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter.enableLoadMore();
        adapter.setOnLoadMoreListener(new ContactsAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (!viewModel.allContactsNextPage()) {
                    adapter.disableLoadMore();
                }
            }
        });
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
                if (aBoolean) {
                    loadingLayout.setVisibility(View.VISIBLE);
                }
                else {
                    loadingLayout.setVisibility(View.GONE);
                }
            }
        });

        viewModel.state.searchMode.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    searchBack.setVisibility(View.VISIBLE);
                    searchLayout.setVisibility(View.VISIBLE);
                }
                else {
                    searchBack.setVisibility(View.GONE);
                    searchLayout.setVisibility(View.INVISIBLE);
                }
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

        givePermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askForPermission();
            }
        });

        searchBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.state.searchMode.setValue(false);
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
