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

import com.momid.mainactivity.contacts_reader.ContactsReader;
import com.momid.mainactivity.di.PermissionHelper;
import com.momid.mainactivity.R;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlin.jvm.functions.Function0;
import kotlinx.coroutines.CoroutineScope;

@HiltViewModel
public class ContactsViewModel extends ViewModel {

    private MutableLiveData<AllContactsRequest> allContactsRequest = new MutableLiveData<>();
    private LiveData<PagingData<Contact>> contactsListLivedata;
    private LiveData<Integer> contactsCount;

    public MutableLiveData<Boolean> loading = new MutableLiveData<>();
    public MutableLiveData<String> loadError = new MutableLiveData<>();
    public MutableLiveData<Boolean> searchMode = new MutableLiveData<>();
    public MutableLiveData<Boolean> contactsPermissionNeeded = new MutableLiveData<>();
    public MutableLiveData<Boolean> permissionDeniedMode = new MutableLiveData<>();
    public MutableLiveData<Boolean> noContactExists = new MutableLiveData<>();

    public MutableLiveData<String> errorMessageLiveDate = new MutableLiveData<>();

    public ContactsRepository contactsRepository;
    public ContactsReader contactsReader;
    public PermissionHelper permissionHelper;

    private static final int PERMISSION_REQUEST_CONTACT = 0;

    @Inject
    public ContactsViewModel(ContactsRepository contactsRepository, ContactsReader contactsReader, PermissionHelper permissionHelper) {

        if (contactsListLivedata == null) {

            this.contactsRepository = contactsRepository;
            this.contactsReader = contactsReader;
            this.permissionHelper = permissionHelper;

            allContactsRequest = new MutableLiveData<>();

//            contactsListLivedata = new LivePagedListBuilder<>(contactsRepository.getAllContacts(allContactsRequest.getValue()), 25).build();

//            contactsListLivedata = new MutableLiveData<>();

            contactsCount = contactsRepository.getContactsCount();

            loading.postValue(true);

            CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);
            Pager<Integer, Contact> pager = new Pager<Integer, Contact>(
                    new PagingConfig(/* pageSize = */ 25), new Function0() {
                @Override
                public PagingSource<Integer, Contact> invoke() {
                    return contactsRepository.getAllContacts(allContactsRequest.getValue());
                }
            });

            contactsListLivedata = PagingLiveData.getLiveData(pager);
            PagingLiveData.cachedIn(contactsListLivedata, viewModelScope);


            contactsCount.observeForever(new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    if (integer > 0) {
//                        getAllContacts();
                        Log.d("", "contacts count are changed");
                    }
                    else {
                        if (permissionHelper.hasContactsPermission()) {
                            loading.postValue(true);

                            new Thread(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            contactsRepository.insertContactsToDatabase(contactsReader.startToGetContactsOnDevice());
                                        }
                                    }
                            ).start();
                        }
                        else {
                            contactsPermissionNeeded.postValue(true);
                        }
                    }
                }
            });
        }
    }

    public LiveData<PagingData<Contact>> getContactsListLivedata() {
        return contactsListLivedata;
    }

    public void getAllContacts() {

        loading.postValue(true);

//        contactsRepository.getAllContacts(allContactsRequest.getValue());

        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);
        Pager<Integer, Contact> pager = new Pager<Integer, Contact>(
                new PagingConfig(/* pageSize = */ 25), new Function0() {
            @Override
            public PagingSource<Integer, Contact> invoke() {
                return contactsRepository.getAllContacts(allContactsRequest.getValue());
            }
        });

        contactsListLivedata = PagingLiveData.getLiveData(pager);
        PagingLiveData.cachedIn(contactsListLivedata, viewModelScope);
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

    public void onGivePermissionClick() {

        contactsPermissionNeeded.postValue(true);
    }

    public void onSearchBackClick() {

        searchMode.postValue(false);
    }

    public void onCall(Context context, String contactPhoneNumber) {

        if (contactPhoneNumber != null && !contactPhoneNumber.equals("0")) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            callIntent.setData(Uri.parse("tel:" + contactPhoneNumber));
            context.startActivity(callIntent);
        }
        else {
            errorMessageLiveDate.postValue(context.getString(R.string.phone_number_doesnt_exist));
        }
    }

    public void onMessage(Context context, String contactPhoneNumber) {

        if (contactPhoneNumber != null && !contactPhoneNumber.equals("0")) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("smsto:" + contactPhoneNumber)); // This ensures only SMS apps respond
            intent.putExtra("sms_body", "hello, it's me.");
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            }
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

        if (permissionHelper.hasContactsPermission()) {
            loading.postValue(true);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    contactsRepository.insertContactsToDatabase(contactsReader.startToGetContactsOnDevice());
                }
            }).start();
        }
    }

}
