package com.momid.mainactivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.momid.mainactivity.data_model.Contact;
import com.momid.mainactivity.databinding.FragmentSearchContactsBinding;
import com.momid.mainactivity.recycler_adapter.ContactsAdapter;
import com.momid.mainactivity.recycler_adapter.OnItemClick;

import java.util.List;

public class SearchContactsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ContactsAdapter adapter;
    private LinearLayoutManager layoutManager;
    private TextView nothingFound;
    private ContactsViewModel viewModel;
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

        viewModel = new ViewModelProvider(requireActivity()).get(ContactsViewModel.class);

        binding.setLifecycleOwner(requireActivity());
        binding.setViewmodel(viewModel);

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
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
//        adapter.enableLoadMore();
        recyclerView.setLayoutManager(layoutManager);
        viewModel.getSearchContactsListLivedata().observe(requireActivity(), adapter::submitList);
        recyclerView.setAdapter(adapter);

        nothingFound.setVisibility(View.GONE);

        viewModel.getSearchContactsListLivedata().observe(requireActivity(), new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contacts) {
                adapter.setContacts(contacts);
                adapter.notifyDataSetChanged();

//                if (contacts.isEmpty()) {
//                    nothingFound.setVisibility(View.VISIBLE);
//                }
//                else {
//                    nothingFound.setVisibility(View.INVISIBLE);
//                }
            }
        });
    }

    public static SearchContactsFragment getInstance() {
        SearchContactsFragment searchContactsFragment = new SearchContactsFragment();
        return searchContactsFragment;
    }
}
