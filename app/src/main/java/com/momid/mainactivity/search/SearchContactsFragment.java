package com.momid.mainactivity.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.momid.mainactivity.R;
import com.momid.mainactivity.contacts.SharedViewModel;
import com.momid.mainactivity.contacts.view.ContactsAdapter;
import com.momid.mainactivity.contacts.view.OnItemClick;
import com.momid.mainactivity.databinding.FragmentSearchContactsBinding;

public class SearchContactsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ContactsAdapter adapter;
    private LinearLayoutManager layoutManager;
    private TextView nothingFound;
    private SearchView searchView;
    private ImageButton searchBack;
    private SearchContactsViewModel viewModel;
    private SharedViewModel sharedViewModel;
    private FragmentSearchContactsBinding binding;


    public SearchContactsFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_contacts, container, false);
        View view = binding.getRoot();
//        binding.setViewmodel(viewModel);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.search_contacts_recycler);
        nothingFound = view.findViewById(R.id.search_contacts_nothing_found);
        searchView = binding.contactsSearchview;
        searchBack = binding.contactsSearchBack;

        viewModel = new ViewModelProvider(requireActivity()).get(SearchContactsViewModel.class);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        binding.setLifecycleOwner(requireActivity());
        binding.setViewmodel(viewModel);
        binding.setClickListener(sharedViewModel.getContactsClickListener());

        searchView.requestFocus();

        adapter = new ContactsAdapter(new OnItemClick() {
            @Override
            public void onItemClick(String phoneNumber) {

            }

            @Override
            public void onCallClick(String phoneNumber) {
                viewModel.onCall(getContext(), phoneNumber);
            }

            @Override
            public void onSmsClick(String phoneNumber) {
                viewModel.onMessage(getContext(), phoneNumber);
            }
        });

        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

        nothingFound.setVisibility(View.GONE);

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

        viewModel.getSearchContactsListLivedata().observe(requireActivity(), pagingData -> {
//                adapter.notifyDataSetChanged();
            adapter.submitData(getLifecycle(), pagingData);
        });

        viewModel.errorMessageLiveDate.observe(requireActivity(), s -> Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show());
    }

    public static SearchContactsFragment getInstance() {
        SearchContactsFragment searchContactsFragment = new SearchContactsFragment();
        return searchContactsFragment;
    }
}
