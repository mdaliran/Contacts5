package com.momid.mainactivity.search;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagingData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.momid.mainactivity.R;
import com.momid.mainactivity.contacts.Contact;
import com.momid.mainactivity.databinding.FragmentSearchContactsBinding;
import com.momid.mainactivity.contacts.view.ContactsAdapter;
import com.momid.mainactivity.contacts.view.OnItemClick;

public class SearchContactsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ContactsAdapter adapter;
    private LinearLayoutManager layoutManager;
    private TextView nothingFound;
    private SearchContactsViewModel viewModel;
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

        viewModel = new ViewModelProvider(requireActivity()).get(SearchContactsViewModel.class);

        binding.setLifecycleOwner(requireActivity());
        binding.setViewmodel(viewModel);

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
