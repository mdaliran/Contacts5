package com.momid.mainactivity;

import androidx.lifecycle.MutableLiveData;

public class ContactsActivityState {

    public MutableLiveData<Boolean> loading = new MutableLiveData<>();
    public MutableLiveData<String> loadError = new MutableLiveData<>();
    public MutableLiveData<Boolean> searchMode = new MutableLiveData<>();
    public MutableLiveData<Boolean> contactsPermissionNeeded = new MutableLiveData<>();
    public MutableLiveData<Boolean> noContactExists = new MutableLiveData<>();
}
