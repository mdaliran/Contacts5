package com.momid.mainactivity.repository;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

import com.momid.mainactivity.data_model.Contact;
import com.momid.mainactivity.request_model.AllContactsRequest;
import com.momid.mainactivity.request_model.SearchContactsRequest;

import java.util.List;

public interface ContactsRepositoryInterface {


    DataSource.Factory<Integer, Contact> getAllContacts(AllContactsRequest request);

    DataSource.Factory<Integer, Contact> searchContacts(SearchContactsRequest request);

    void insertContactsToDatabase(List<Contact> contacts);

//    void areContactsStoredInDatabase(DataCallback<Boolean> dataCallback);

    LiveData<Integer> getContactsCount();

    LiveData<Integer> getSearchContactsCount(String searchQuery);
}
