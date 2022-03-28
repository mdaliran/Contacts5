package com.momid.mainactivity.contacts;

import androidx.lifecycle.LiveData;
import androidx.paging.PagingSource;

import com.momid.mainactivity.database.ContactsDao;
import com.momid.mainactivity.network.ContactsClient;
import com.momid.mainactivity.network.ContactsNetworkService;
import com.momid.mainactivity.search.SearchContactsRequest;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ContactsRepositoryImpl implements ContactsRepository {

    public ContactsDao contactsDao;
    public ContactsNetworkService contactsNetworkService;

    @Inject
    public ContactsRepositoryImpl(ContactsDao contactsDao, ContactsNetworkService contactsNetworkService) {

        this.contactsDao = contactsDao;
        this.contactsNetworkService = contactsNetworkService;
    }

    @Override
    public PagingSource<Integer, Contact> getAllContacts(AllContactsRequest request) {

        return contactsDao.getAllContacts();
    }

    @Override
    public PagingSource<Integer, Contact> searchContacts(SearchContactsRequest request) {

        return contactsDao.searchContacts(request.getSearchQuery());
    }

    @Override
    public void insertContactsToDatabase(List<Contact> contacts) {

        new Thread(() -> contactsDao.addAllContacts(contacts)).start();
    }

    @Override
    public LiveData<Integer> getContactsCount() {

        return contactsDao.getAllContactsCount();
    }

    @Override
    public LiveData<Integer> getSearchContactsCount(String searchQuery) {

        return contactsDao.searchContactsCount(searchQuery);
    }

    public int getContactsCount1() {

        return contactsDao.getContactsCount();
    }

    public void getRemoteContacts(DataCallback<List<Contact>> callback) {

        contactsNetworkService.getContacts().enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                List<Contact> body = response.body();
                for (Contact contact : body) {
                    contact.setUpForRemote();
                }
                callback.onFinish(body);
            }

            @Override
            public void onFailure(Call<List<Contact>> call, Throwable t) {
                callback.onFail(t.getMessage());
            }
        });
    }

    public void removeAllContacts() {

        contactsDao.removeAll();
    }

    public void removeUnnesseceryContacts(String source, List<String> ids) {

        contactsDao.removeIfNotInIds(source, ids);
    }
}
