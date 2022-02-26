package com.momid.mainactivity.contacts;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData;
import androidx.paging.PagingSource;

import com.momid.mainactivity.util.CallUtil;
import com.momid.mainactivity.R;
import com.momid.mainactivity.contacts_reader.ContactsReader;
import com.momid.mainactivity.contacts_reader.ContactsReaderListener;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlin.jvm.functions.Function0;
import kotlinx.coroutines.CoroutineScope;

@HiltViewModel
public class ContactsViewModel extends ViewModel implements ContactsReaderListener {

    public MutableLiveData<Boolean> loading = new MutableLiveData<>();
    public MutableLiveData<String> loadError = new MutableLiveData<>();
    public MutableLiveData<Boolean> searchMode = new MutableLiveData<>();
    public MutableLiveData<Boolean> contactsPermissionNeeded = new MutableLiveData<>();
    public MutableLiveData<Boolean> permissionDeniedMode = new MutableLiveData<>();
    public MutableLiveData<Boolean> noContactExists = new MutableLiveData<>();
    public MutableLiveData<Boolean> readComplete = new MutableLiveData<>();

    public MutableLiveData<String> errorMessageLiveDate = new MutableLiveData<>();

    public ContactsReader contactsReader;

    private static final int PERMISSION_REQUEST_CONTACT = 0;

    @Inject
    public ContactsViewModel(ContactsReader contactsReader) {

            this.contactsReader = contactsReader;

//            getAllContacts();

            contactsReader.startToRead(this);
    }

    public void askForPermission(Activity activity) {

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            activity.requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSION_REQUEST_CONTACT);
        }
    }

    public void onSearchViewTextChange(String searchQuery) {

        if (!searchQuery.equals("")) {
            searchMode.setValue(true);
        }
    }

    public void onPermissionGrant() {

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
//        pagingSource.invalidate();
        readComplete.postValue(true);
    }

    @Override
    public void permissionNeeded() {

        contactsPermissionNeeded.postValue(true);
    }
}

