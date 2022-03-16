package com.momid.mainactivity.contacts_fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.momid.mainactivity.R;
import com.momid.mainactivity.contacts.SharedViewModel;
import com.momid.mainactivity.contacts.ContactsAdapter;
import com.momid.mainactivity.contacts.OnItemClick;
import com.momid.mainactivity.databinding.FragmentContactsBinding;

public class ContactsFragment extends Fragment {

    private ContactsFragmentViewModel viewModel;
    private SharedViewModel sharedViewModel;
    private RecyclerView recyclerView;
    private ContactsAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FragmentContactsBinding binding;

    public ContactsFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contacts, container, false);
        View view = binding.getRoot();
//        binding.setViewmodel(viewModel);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(ContactsFragmentViewModel.class);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        binding.setLifecycleOwner(requireActivity());
        binding.setClickListener(sharedViewModel.getContactsClickListener());

        recyclerView = binding.contactsRecycler;
        swipeRefreshLayout = binding.contactsSwip;

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

        recyclerView.setAdapter(adapter);

        viewModel.getContactsListLivedata().observe(requireActivity(), contactPagingData -> adapter.submitData(getLifecycle(), contactPagingData));

        sharedViewModel.dbRefresh.observe(requireActivity(), dbRefresh ->  {
            if (dbRefresh) {
                viewModel.refresh();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            viewModel.refresh();
            swipeRefreshLayout.setRefreshing(false);
        });
    }
}
