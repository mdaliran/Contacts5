package com.momid.mainactivity.contacts;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData;
import androidx.paging.PagingSource;

import com.momid.mainactivity.CallUtil;
import com.momid.mainactivity.contacts_reader.ContactsReader;
import com.momid.mainactivity.PermissionHelper;
import com.momid.mainactivity.R;
import com.momid.mainactivity.contacts_reader.ContactsReaderListener;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlin.jvm.functions.Function0;
import kotlinx.coroutines.CoroutineScope;

@HiltViewModel
public class ContactsViewModel extends ViewModel implements ContactsReaderListener {

    private MutableLiveData<AllContactsRequest> allContactsRequest = new MutableLiveData<>();
    private LiveData<PagingData<Contact>> contactsListLivedata;

    public MutableLiveData<Boolean> loading = new MutableLiveData<>();
    public MutableLiveData<String> loadError = new MutableLiveData<>();
    public MutableLiveData<Boolean> searchMode = new MutableLiveData<>();
    public MutableLiveData<Boolean> contactsPermissionNeeded = new MutableLiveData<>();
    public MutableLiveData<Boolean> permissionDeniedMode = new MutableLiveData<>();
    public MutableLiveData<Boolean> noContactExists = new MutableLiveData<>();

    public MutableLiveData<String> errorMessageLiveDate = new MutableLiveData<>();

    public ContactsRepository contactsRepository;
    public ContactsReader contactsReader;
    @Inject
    public CallUtil callUtil;

    private static final int PERMISSION_REQUEST_CONTACT = 0;

    @Inject
    public ContactsViewModel(ContactsRepository contactsRepository, ContactsReader contactsReader) {

            this.contactsRepository = contactsRepository;
            this.contactsReader = contactsReader;

            getAllContacts();

            contactsReader.startToRead(this);
    }

    public LiveData<PagingData<Contact>> getContactsListLivedata() {
        return contactsListLivedata;
    }

    public void getAllContacts() {

        loading.postValue(true);

        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);
        Pager<Integer, Contact> pager = new Pager<Integer, Contact>(
                new PagingConfig(/* pageSize = */ 25), new Function0() {
            @Override
            public PagingSource<Integer, Contact> invoke() {
                return contactsRepository.getAllContacts(allContactsRequest.getValue());
            }
        });

        contactsListLivedata = PagingLiveData.cachedIn(PagingLiveData.getLiveData(pager), viewModelScope);
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

    public void onCall(Context context, String contactPhoneNumber) {

        if (contactPhoneNumber != null && !contactPhoneNumber.equals("0")) {
            callUtil.call(contactPhoneNumber);
        }
        else {
            errorMessageLiveDate.postValue(context.getString(R.string.phone_number_doesnt_exist));
        }
    }

    public void onMessage(Context context, String contactPhoneNumber) {

        if (contactPhoneNumber != null && !contactPhoneNumber.equals("0")) {
            callUtil.message("hi", contactPhoneNumber);
        }
        else {
            errorMessageLiveDate.postValue(context.getString(R.string.phone_number_doesnt_exist));
        }
    }

    public void startPermissionDeniedMode() {

        permissionDeniedMode.postValue(true);
    }

    public void endPermissionDeniedMode() {

        permissionDeniedMode.postValue(false);
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
    }

    @Override
    public void permissionNeeded() {

        contactsPermissionNeeded.postValue(true);
    }
}
