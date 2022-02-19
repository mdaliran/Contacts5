package com.momid.mainactivity.contacts;

import androidx.lifecycle.LiveData;
import androidx.paging.PagingSource;

import com.momid.mainactivity.search.SearchContactsRequest;

import java.util.List;

public interface ContactsRepository {


    PagingSource<Integer, Contact> getAllContacts(AllContactsRequest request);

    PagingSource<Integer, Contact> searchContacts(SearchContactsRequest request);

    void insertContactsToDatabase(List<Contact> contacts);

    LiveData<Integer> getContactsCount();

    LiveData<Integer> getSearchContactsCount(String searchQuery);
}
