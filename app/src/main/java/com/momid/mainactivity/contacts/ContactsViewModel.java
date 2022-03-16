package com.momid.mainactivity.contacts;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.momid.mainactivity.contacts_reader.ContactsReader;
import com.momid.mainactivity.contacts_reader.ContactsReaderListener;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ContactsViewModel extends ViewModel implements ContactsReaderListener {

    public MutableLiveData<Boolean> loading = new MutableLiveData<>();
//    public MutableLiveData<String> loadError = new MutableLiveData<>();
    public MutableLiveData<Boolean> searchMode = new MutableLiveData<>();
    public MutableLiveData<Boolean> contactsPermissionNeeded = new MutableLiveData<>();
    public MutableLiveData<Boolean> permissionDeniedMode = new MutableLiveData<>();
    public MutableLiveData<Boolean> noContactExists = new MutableLiveData<>();
    public MutableLiveData<Boolean> shouldRefresh = new MutableLiveData<>();

    public MutableLiveData<String> errorMessageLiveDate = new MutableLiveData<>();

    public ContactsRepository repository;
    public ContactsReader contactsReader;

    private static final int PERMISSION_REQUEST_CONTACT = 0;

    @Inject
    public ContactsViewModel(ContactsReader contactsReader, ContactsRepository repository) {

        this.repository = repository;
        this.contactsReader = contactsReader;

//        loading.postValue(true);

        new Thread(() -> {
            repository.removeAllContacts();
            contactsReader.startToRead(this);
        }).start();

    }

    public void askForPermission(Activity activity) {

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            activity.requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSION_REQUEST_CONTACT);
        }
    }

    public void onPermissionGrant() {

//        loading.postValue(true);

//        repository.getRemoteContacts(new DataCallback<List<Contact>>() {
//            @Override
//            public void onFinish(List<Contact> contacts) {
//                repository.insertContactsToDatabase(contacts);
////                contactsReader.startToRead(contacts, ContactsViewModel.this);
////                loading.postValue(false);
//            }
//
//            @Override
//            public void onFail(String error) {
//
//            }
//        });

        new Thread(repository::removeAllContacts).start();

        contactsReader.startToRead(this);
    }

    @Override
    public void alreadyStored() {

    }

    @Override
    public void readStart() {
        loading.postValue(true);
    }

    @Override
    public void readEnd() {
        loading.postValue(false);
        shouldRefresh.postValue(true);

        repository.getRemoteContacts(new DataCallback<List<Contact>>() {
            @Override
            public void onFinish(List<Contact> contacts) {
                repository.insertContactsToDatabase(contacts);
                shouldRefresh.postValue(true);
//                contactsReader.startToRead(contacts, ContactsViewModel.this);
//                loading.postValue(false);
            }

            @Override
            public void onFail(String error) {

            }
        });
    }

    @Override
    public void permissionNeeded() {

        contactsPermissionNeeded.postValue(true);
    }
}
