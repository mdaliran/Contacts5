package com.momid.mainactivity.contacts;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.momid.mainactivity.contacts.view.ContactsClickListener;

public class SharedViewModel extends ViewModel {

    private ContactsClickListener contactsClickListener;
    public MutableLiveData<Boolean> readComplete = new MutableLiveData<>();

    public SharedViewModel() {

    }

    public ContactsClickListener getContactsClickListener() {
        return contactsClickListener;
    }

    public void setContactsClickListener(ContactsClickListener contactsClickListener) {
        this.contactsClickListener = contactsClickListener;
    }
}
