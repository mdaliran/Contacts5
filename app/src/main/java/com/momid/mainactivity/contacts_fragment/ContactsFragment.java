package com.momid.mainactivity.contacts_fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.momid.mainactivity.R;
import com.momid.mainactivity.contacts.view.ContactsAdapter;
import com.momid.mainactivity.contacts.view.OnItemClick;
import com.momid.mainactivity.databinding.FragmentContactsBinding;

public class ContactsFragment extends Fragment {

    private ContactsFragmentViewModel viewModel;
    private RecyclerView recyclerView;
    private ContactsAdapter adapter;
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

        binding.setLifecycleOwner(requireActivity());

        recyclerView = binding.contactsRecycler;

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
    }
}
