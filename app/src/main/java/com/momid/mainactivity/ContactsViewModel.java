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
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.momid.mainactivity.data_model.Contact;
import com.momid.mainactivity.repository.ContactsRepository;
import com.momid.mainactivity.repository.ContactsRepositoryInterface;
import com.momid.mainactivity.repository.DataCallback;
import com.momid.mainactivity.request_model.AllContactsRequest;
import com.momid.mainactivity.request_model.SearchContactsRequest;
import com.momid.mainactivity.response_model.ContactsListResponse;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ContactsViewModel extends AndroidViewModel {

    private MutableLiveData<AllContactsRequest> allContactsRequest;
    private MutableLiveData<SearchContactsRequest> searchContactsRequest;
    private LiveData<PagedList<Contact>> contactsListLivedata;
    private LiveData<PagedList<Contact>> searchContactsListLivedata;
    private LiveData<Integer> contactsCount;
    public ContactsRepositoryInterface contactsRepositoryInterface;
    public ContactsActivityState state = new ContactsActivityState();
    private static final int PERMISSION_REQUEST_CONTACT = 0;

    @Inject
    public ContactsViewModel(Application application, ContactsRepositoryInterface contactsRepositoryInterface) {
        super(application);
        this.contactsRepositoryInterface = contactsRepositoryInterface;
    }

    public void init() {
        if (contactsListLivedata == null) {

            allContactsRequest = new MutableLiveData<AllContactsRequest>();
            searchContactsRequest = new MutableLiveData<SearchContactsRequest>();

            contactsListLivedata = new LivePagedListBuilder<Integer, Contact>(contactsRepositoryInterface.getAllContacts(allContactsRequest.getValue()), 25).build();
            searchContactsListLivedata = Transformations.switchMap(searchContactsRequest, new Function<SearchContactsRequest, LiveData<PagedList<Contact>>>() {
                @Override
                public LiveData<PagedList<Contact>> apply(SearchContactsRequest input) {
                    return new LivePagedListBuilder<Integer, Contact>(contactsRepositoryInterface.searchContacts(searchContactsRequest.getValue()), 25).build();
                }
            });

            contactsCount = contactsRepositoryInterface.getContactsCount();

            contactsCount.observeForever(new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    if (integer > 0) {
                        getAllContacts();
                        Log.d("", "contacts count are changed");
                    }
                    else {
                        if (hasContactPermission()) {
                            state.loading.postValue(true);

                            new Thread(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            contactsRepositoryInterface.insertContactsToDatabase(ContactsGetter.getAllContactsOnThisDevice(getApplication()));
                                        }
                                    }
                            ).start();
                        }
                        else {
                            state.contactsPermissionNeeded.postValue(true);
                        }
                    }
                }
            });
        }
    }

    public LiveData<PagedList<Contact>> getContactsListLivedata() {
        return contactsListLivedata;
    }

    public LiveData<PagedList<Contact>> getSearchContactsListLivedata() {
        return searchContactsListLivedata;
    }

    public void getAllContacts() {

        state.loading.postValue(true);

        contactsRepositoryInterface.getAllContacts(allContactsRequest.getValue());
    }

    public void searchContacts(SearchContactsRequest request) {

        this.searchContactsRequest.postValue(request);
    }

    public void askForPermission(Activity activity) {

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            activity.requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSION_REQUEST_CONTACT);
        }
    }

    public void onSearchViewTextChange(String searchQuery) {

        if (!searchQuery.equals("")) {
            state.searchMode.setValue(true);
            SearchContactsRequest request = new SearchContactsRequest();
            request.setSearchQuery(searchQuery);
            searchContacts(request);
        }
    }

    public void onGivePermissionClick() {

        state.contactsPermissionNeeded.postValue(true);
    }

    public void onSearchBackClick() {

        state.searchMode.postValue(false);
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

    public boolean hasContactPermission() {

        return ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
    }

    public void onPermissionGrant() {

        if (hasContactPermission()) {
            state.loading.postValue(true);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    contactsRepositoryInterface.insertContactsToDatabase(ContactsGetter.getAllContactsOnThisDevice(getApplication()));
                }
            }).start();
        }
    }

}
