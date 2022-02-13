package com.momid.mainactivity;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.arch.core.util.Function;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.momid.mainactivity.data_model.Contact;
import com.momid.mainactivity.repository.ContactsRepositoryInterface;
import com.momid.mainactivity.request_model.AllContactsRequest;
import com.momid.mainactivity.request_model.SearchContactsRequest;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SearchContactsViewModel extends AndroidViewModel {

    private MutableLiveData<SearchContactsRequest> searchContactsRequest;
    private LiveData<PagedList<Contact>> searchContactsListLivedata;

    public MutableLiveData<Boolean> searchMode = new MutableLiveData<>();

    public ContactsRepositoryInterface contactsRepositoryInterface;

    @Inject
    public SearchContactsViewModel(Application application, ContactsRepositoryInterface contactsRepositoryInterface) {
        super(application);
        this.contactsRepositoryInterface = contactsRepositoryInterface;
    }

    public void init() {
        if (searchContactsListLivedata == null) {

            searchContactsRequest = new MutableLiveData<SearchContactsRequest>();

            searchContactsListLivedata = Transformations.switchMap(searchContactsRequest, new Function<SearchContactsRequest, LiveData<PagedList<Contact>>>() {
                @Override
                public LiveData<PagedList<Contact>> apply(SearchContactsRequest input) {
                    return new LivePagedListBuilder<Integer, Contact>(contactsRepositoryInterface.searchContacts(searchContactsRequest.getValue()), 25).build();
                }
            });
        }
    }

    public LiveData<PagedList<Contact>> getSearchContactsListLivedata() {
        return searchContactsListLivedata;
    }

    public void searchContacts(SearchContactsRequest request) {

        this.searchContactsRequest.postValue(request);
    }

    public void onSearchViewTextChange(String searchQuery) {

        if (!searchQuery.equals("")) {
            SearchContactsRequest request = new SearchContactsRequest();
            request.setSearchQuery(searchQuery);
            searchContacts(request);
        }
    }

    public void onCall(Contact contact) {

        if (contact.getPhoneNumber() != null && !contact.getPhoneNumber().equals("0")) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            callIntent.setData(Uri.parse("tel:" + contact.getPhoneNumber()));
            getApplication().startActivity(callIntent);
        }
        else {
            Toast.makeText(getApplication(), "شماره موجود نیست", Toast.LENGTH_LONG).show();
        }
    }

    public void onMessage(Contact contact) {

        if (contact.getPhoneNumber() != null && !contact.getPhoneNumber().equals("0")) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("smsto:" + contact.getPhoneNumber())); // This ensures only SMS apps respond
            intent.putExtra("sms_body", "hello, it's me.");
            if (intent.resolveActivity(getApplication().getPackageManager()) != null) {
                getApplication().startActivity(intent);
            }
        }
        else {
            Toast.makeText(getApplication(), "شماره موجود نیست", Toast.LENGTH_LONG).show();
        }
    }

}
