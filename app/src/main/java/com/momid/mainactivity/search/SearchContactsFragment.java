package com.momid.mainactivity.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.momid.mainactivity.R;
import com.momid.mainactivity.contacts.SharedViewModel;
import com.momid.mainactivity.contacts.ContactsAdapter;
import com.momid.mainactivity.contacts.OnItemClick;
import com.momid.mainactivity.databinding.FragmentSearchContactsBinding;

public class SearchContactsFragment extends Fragment {

    private ContactsAdapter adapter;
    private SearchView searchView;
    private SearchContactsViewModel searchViewModel;
    private FragmentSearchContactsBinding binding;


    public SearchContactsFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_contacts, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView nothingFound = view.findViewById(R.id.search_contacts_nothing_found);
        searchView = binding.contactsSearchview;

        searchViewModel = new ViewModelProvider(requireActivity()).get(SearchContactsViewModel.class);
        SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        binding.setLifecycleOwner(requireActivity());
        binding.setViewmodel(searchViewModel);
        binding.setClickListener(sharedViewModel.getContactsClickListener());

        searchView.requestFocus();

        adapter = new ContactsAdapter(new OnItemClick() {
            @Override
            public void onItemClick(String phoneNumber) {

            }

            @Override
            public void onCallClick(String phoneNumber) {
                searchViewModel.onCall(getContext(), phoneNumber);
            }

            @Override
            public void onSmsClick(String phoneNumber) {
                searchViewModel.onMessage(getContext(), phoneNumber);
            }
        });

        binding.searchContactsRecycler.setAdapter(adapter);

        nothingFound.setVisibility(View.GONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchView.setIconified(true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchViewModel.onSearchViewTextChange(s);
                return true;
            }
        });

        searchViewModel.getSearchContactsListLivedata().observe(requireActivity(),
                pagingData -> adapter.submitData(getLifecycle(), pagingData));

        searchViewModel.errorMessageLiveDate.observe(requireActivity(), s -> Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show());
    }
}
