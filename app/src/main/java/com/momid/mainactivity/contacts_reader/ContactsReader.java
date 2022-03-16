package com.momid.mainactivity.contacts_reader;

import com.momid.mainactivity.contacts.Contact;

import java.util.List;

public interface ContactsReader {


    public void startToRead(ContactsReaderListener contactsReaderListener);

    public List<Contact> startToGetContactsOnDevice();
}
