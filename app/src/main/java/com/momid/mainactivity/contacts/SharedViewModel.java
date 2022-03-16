package com.momid.mainactivity.contacts;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {

    private ContactsClickListener contactsClickListener;
    public MutableLiveData<Boolean> dbRefresh = new MutableLiveData<>();

    public SharedViewModel() {

    }

    public ContactsClickListener getContactsClickListener() {
        return contactsClickListener;
    }

    public void setContactsClickListener(ContactsClickListener contactsClickListener) {
        this.contactsClickListener = contactsClickListener;
    }
}
