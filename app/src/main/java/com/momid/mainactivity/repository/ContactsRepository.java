package com.momid.mainactivity.repository;

import android.app.Application;

import androidx.room.Room;

import com.momid.mainactivity.data_model.Contact;
import com.momid.mainactivity.database.ContactsDao;
import com.momid.mainactivity.database.ContactsDatabase;
import com.momid.mainactivity.request_model.AllContactsRequest;
import com.momid.mainactivity.request_model.SearchContactsRequest;
import com.momid.mainactivity.responseModel.AllContactsResponse;
import com.momid.mainactivity.responseModel.SearchContactsResponse;

import java.util.List;

public class ContactsRepository {

    private Application application;
    private ContactsDatabase contactsDatabase;
    private ContactsDao contactsDao;


    public ContactsRepository(Application application) {

        this.application = application;
        contactsDatabase = Room.databaseBuilder(application, ContactsDatabase.class, "contacts_database15").build();
        contactsDao = contactsDatabase.contactsDao();
    }

    public void getAllContacts(AllContactsRequest request, DataCallback<AllContactsResponse> dataCallback) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                AllContactsResponse allContactsResponse = new AllContactsResponse();
                List<Contact> contacts = contactsDao.getAllContacts(request.getPage(), request.getPageSize());
                allContactsResponse.setContacts(contacts);
                allContactsResponse.setTotalPages(contactsDao.getAllContactsCount()/request.getPageSize() + 1);
                dataCallback.onFinish(allContactsResponse);
            }
        }).start();
    }

    public void searchContacts(SearchContactsRequest request, DataCallback<SearchContactsResponse> dataCallback) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                SearchContactsResponse searchContactsResponse = new SearchContactsResponse();
                List<Contact> contacts = contactsDao.searchContacts(request.getSearchQuery(), request.getPage(), request.getPageSize());
                searchContactsResponse.setContacts(contacts);
                searchContactsResponse.setTotalPages(contactsDao.searchContactsCount(request.getSearchQuery())/request.getPageSize() + 1);
                dataCallback.onFinish(searchContactsResponse);
            }
        }).start();
    }

    public void insertContactsToDatabase(List<Contact> contacts, DataCallback<Boolean> dataCallback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                contactsDao.addAllContacts(contacts);
                dataCallback.onFinish(true);
            }
        }).start();
    }

    public void areContactsStoredInDatabase(DataCallback<Boolean> dataCallback) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Boolean areStored = contactsDao.getAllContactsCount() > 0;
                dataCallback.onFinish(areStored);
            }
        }).start();
    }
}
