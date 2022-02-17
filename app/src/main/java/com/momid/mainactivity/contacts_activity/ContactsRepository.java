package com.momid.mainactivity.contacts_activity;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

import com.momid.mainactivity.search_fragment.SearchContactsRequest;

import java.util.List;

public interface ContactsRepository {


    DataSource.Factory<Integer, Contact> getAllContacts(AllContactsRequest request);

    DataSource.Factory<Integer, Contact> searchContacts(SearchContactsRequest request);

    void insertContactsToDatabase(List<Contact> contacts);

    LiveData<Integer> getContactsCount();

    LiveData<Integer> getSearchContactsCount(String searchQuery);
}
