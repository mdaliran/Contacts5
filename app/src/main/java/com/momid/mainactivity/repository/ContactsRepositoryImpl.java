package com.momid.mainactivity.repository;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

import com.momid.mainactivity.data_model.Contact;
import com.momid.mainactivity.database.ContactsDao;
import com.momid.mainactivity.request_model.AllContactsRequest;
import com.momid.mainactivity.request_model.SearchContactsRequest;

import java.util.List;

import javax.inject.Inject;

public class ContactsRepositoryImpl implements ContactsRepository {

    public ContactsDao contactsDao;

    @Inject
    public ContactsRepositoryImpl(ContactsDao contactsDao) {

        this.contactsDao = contactsDao;
    }

    @Override
    public DataSource.Factory<Integer, Contact> getAllContacts(AllContactsRequest request) {

        return contactsDao.getAllContacts();
    }

    @Override
    public DataSource.Factory<Integer, Contact> searchContacts(SearchContactsRequest request) {

        return contactsDao.searchContacts(request.getSearchQuery());
    }

    @Override
    public void insertContactsToDatabase(List<Contact> contacts) {

        contactsDao.addAllContacts(contacts);
    }

    @Override
    public LiveData<Integer> getContactsCount() {

        return contactsDao.getAllContactsCount();
    }

    @Override
    public LiveData<Integer> getSearchContactsCount(String searchQuery) {

        return contactsDao.searchContactsCount(searchQuery);
    }
}
