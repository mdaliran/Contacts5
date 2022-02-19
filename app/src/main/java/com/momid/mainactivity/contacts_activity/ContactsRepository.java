package com.momid.mainactivity.contacts_activity;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.PagingSource;

import com.momid.mainactivity.search_fragment.SearchContactsRequest;

import java.util.List;

public interface ContactsRepository {


    PagingSource<Integer, Contact> getAllContacts(AllContactsRequest request);

    PagingSource<Integer, Contact> searchContacts(SearchContactsRequest request);

    void insertContactsToDatabase(List<Contact> contacts);

    LiveData<Integer> getContactsCount();

    LiveData<Integer> getSearchContactsCount(String searchQuery);
}
