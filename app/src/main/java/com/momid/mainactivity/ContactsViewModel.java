package com.momid.mainactivity;

import android.Manifest;
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
//            searchContactsCount = contactsRepositoryInterface.getSearchContactsCount(searchContactsRequest.getValue().getSearchQuery());

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

//        contactsRepositoryInterface.getAllContacts(allContactsRequest).observeForever(new Observer<List<Contact>>() {
//            @Override
//            public void onChanged(List<Contact> contacts) {
//                ContactsListResponse contactsListResponse = new ContactsListResponse();
//                contactsListResponse.setContacts(contacts);
//                contactsListResponse.setTotalPages(contactsCount/allContactsRequest.getPageSize() + 1);
//                allContactsLivedata.postValue(contactsListResponse);
//
//                state.loading.postValue(false);
//            }
//        });

        contactsRepositoryInterface.getAllContacts(allContactsRequest.getValue());
//        contactsRepositoryInterface.getContactsCount();
    }

    public void searchContacts(SearchContactsRequest request) {

        this.searchContactsRequest.postValue(request);

//        contactsRepositoryInterface.searchContacts(request).observeForever(new Observer<List<Contact>>() {
//                    @Override
//                    public void onChanged(List<Contact> contacts) {
//                        contactsRepositoryInterface.getSearchContactsCount(request.getSearchQuery())
//                                .observeForever(new Observer<Integer>() {
//                            @Override
//                            public void onChanged(Integer integer) {
//                                ContactsListResponse contactsListResponse = new ContactsListResponse();
//                                contactsListResponse.setContacts(contacts);
//                                contactsListResponse.setTotalPages(integer/request.getPageSize());
//                                searchContactsLivedata.postValue(contactsListResponse);
//                            }
//                        });
//                    }
//                });

//        contactsRepositoryInterface.searchContacts(request);
    }

//    public boolean allContactsNextPage() {
//
////        if (contactsCount.getValue() != null) {
//            contactsTotalPages = contactsCount.getValue() / allContactsRequest.getValue().getPageSize() + 1;
//            Log.d("", contactsCount.getValue().toString());
////        }
//
//        if (allContactsRequest.getValue().getPage() < contactsTotalPages) {
//            allContactsRequest.getValue().setPage(allContactsRequest.getValue().getPage() + 1);
//
////            contactsRepositoryInterface.getAllContacts(allContactsRequest).observeForever(new Observer<List<Contact>>() {
////                        @Override
////                        public void onChanged(List<Contact> contacts) {
////                            allContactsLivedata.getValue().getContacts().addAll(contacts);
////                            allContactsLivedata.postValue(allContactsLivedata.getValue());
////                        }
////                    });
//
//            contactsRepositoryInterface.getAllContacts(allContactsRequest.getValue());
//
//            return true;
//        }
//
//        return false;
//    }

//    public boolean searchContactsNextPage() {
//
//        if (searchContactsCount.getValue() != null) {
//            searchContactsTotalPages = searchContactsCount.getValue() / searchContactsRequest.getValue().getPageSize() + 1;
//        }
//
//        if (searchContactsRequest.getValue().getPage() < searchContactsTotalPages) {
//            searchContactsRequest.getValue().setPage(searchContactsRequest.getValue().getPage() + 1);
//
////            contactsRepositoryInterface.searchContacts(searchContactsRequest).observeForever(new Observer<List<Contact>>() {
////                        @Override
////                        public void onChanged(List<Contact> contacts) {
////                            searchContactsLivedata.getValue().getContacts().addAll(contacts);
////                            searchContactsLivedata.postValue(searchContactsLivedata.getValue());
////                        }
////                    });
//
//            contactsRepositoryInterface.searchContacts(searchContactsRequest.getValue());
//
//            return true;
//        }
//
//        return false;
//    }

    public void onSearchViewTextChange(String searchQuery) {

        if (!searchQuery.equals("")) {
            state.searchMode.setValue(true);
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
