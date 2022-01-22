package com.momid.mainactivity.repository;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

import com.momid.mainactivity.data_model.Contact;
import com.momid.mainactivity.database.ContactsDao;
import com.momid.mainactivity.request_model.AllContactsRequest;
import com.momid.mainactivity.request_model.SearchContactsRequest;

import java.util.List;

import javax.inject.Inject;

public class ContactsRepository implements ContactsRepositoryInterface {

//    public ContactsDatabase contactsDatabase;
    public ContactsDao contactsDao;

    @Inject
    public ContactsRepository(ContactsDao contactsDao) {

        this.contactsDao = contactsDao;

//        contactsDatabase = Room.databaseBuilder(application, ContactsDatabase.class, "contacts_database15").build();
//        contactsDao = contactsDatabase.contactsDao();
    }

    @Override
    public DataSource.Factory<Integer, Contact> getAllContacts(AllContactsRequest request) {

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                ContactsListResponse contactsListResponse = new ContactsListResponse();
//                List<Contact> contacts = contactsDao.getAllContacts(request.getPage(), request.getPageSize());
//                contactsListResponse.setContacts(contacts);
//                contactsListResponse.setTotalPages(contactsDao.getAllContactsCount()/request.getPageSize() + 1);
//                dataCallback.onFinish(contactsListResponse);
//            }
//        }).start();

        return contactsDao.getAllContacts();
    }

    @Override
    public DataSource.Factory<Integer, Contact> searchContacts(SearchContactsRequest request) {

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                ContactsListResponse contactsListResponse = new ContactsListResponse();
//                List<Contact> contacts = contactsDao.searchContacts(request.getSearchQuery(), request.getPage(), request.getPageSize());
//                contactsListResponse.setContacts(contacts);
//                contactsListResponse.setTotalPages(contactsDao.searchContactsCount(request.getSearchQuery())/request.getPageSize() + 1);
//                dataCallback.onFinish(contactsListResponse);
//            }
//        }).start();

        return contactsDao.searchContacts(request.getSearchQuery());
    }

    @Override
    public void insertContactsToDatabase(List<Contact> contacts) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                contactsDao.addAllContacts(contacts);
//                dataCallback.onFinish(true);
//            }
//        }).start();

        contactsDao.addAllContacts(contacts);
    }

//    public void areContactsStoredInDatabase(DataCallback<Boolean> dataCallback) {
//
////        new Thread(new Runnable() {
////            @Override
////            public void run() {
////                Boolean areStored = contactsDao.getAllContactsCount() > 0;
////                dataCallback.onFinish(areStored);
////            }
////        }).start();
//    }

    @Override
    public LiveData<Integer> getContactsCount() {

        return contactsDao.getAllContactsCount();
    }

    @Override
    public LiveData<Integer> getSearchContactsCount(String searchQuery) {

        return contactsDao.searchContactsCount(searchQuery);
    }
}
